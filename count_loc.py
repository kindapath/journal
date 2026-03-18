#!/usr/bin/env python3
"""
Анализ доли переиспользуемого кода в KMP-проекте.
Запуск: python3 count_loc.py  (из корня проекта)
"""

import csv
import os
from dataclasses import dataclass
from pathlib import Path


EXTENSIONS = {".kt", ".sq"}

CATEGORIES = {
    "commonMain": "Общий код (commonMain)",
    "nonJsMain": "Общий код (nonJsMain)",
    "androidMain": "Android-специфичный (androidMain)",
    "jvmMain": "Desktop-специфичный (jvmMain)",
    "jsMain": "Web-специфичный (jsMain)",
}

ENTRY_POINTS = {
    "androidApp": "Android (androidApp)",
    "desktopApp": "Desktop (desktopApp)",
    "webApp": "Web (webApp)",
}

SKIP_FRAGMENTS = {"/build/", "/generated/", "Test/", "test/", "/iosMain/", "/iosApp/"}


@dataclass
class CategoryStats:
    files: int = 0
    loc: int = 0


def count_loc(filepath: Path) -> int:
    """Считает строки кода без пустых строк и комментариев."""
    try:
        lines = filepath.read_text(encoding="utf-8", errors="ignore").splitlines()
    except Exception:
        return 0

    count = 0
    in_block = False
    for line in lines:
        stripped = line.strip()

        if in_block:
            if "*/" in stripped:
                in_block = False
            continue
        if stripped.startswith("/*"):
            if "*/" not in stripped:
                in_block = True
            continue

        if not stripped or stripped.startswith("//"):
            continue

        count += 1
    return count


def classify_file(filepath: Path) -> str | None:
    """Определяет категорию файла по пути."""
    str_path = filepath.as_posix()

    for ep in ENTRY_POINTS:
        if f"/{ep}/" in str_path:
            return ep

    for cat in CATEGORIES:
        if f"/src/{cat}/" in str_path:
            return cat

    return None


def collect_stats(root: Path) -> dict[str, CategoryStats]:
    results: dict[str, CategoryStats] = {}

    for ext in EXTENSIONS:
        for filepath in root.rglob(f"*{ext}"):
            str_path = filepath.as_posix()
            if any(skip in str_path for skip in SKIP_FRAGMENTS):
                continue

            category = classify_file(filepath)
            if category is None:
                continue

            if category not in results:
                results[category] = CategoryStats()

            results[category].files += 1
            results[category].loc += count_loc(filepath)

    return results


def print_report(results: dict[str, CategoryStats]) -> None:
    """Печатает отчёт и сохраняет CSV."""
    total_loc = sum(s.loc for s in results.values())
    total_files = sum(s.files for s in results.values())

    order = ["commonMain", "nonJsMain", "androidMain", "jvmMain", "jsMain",
             "androidApp", "desktopApp", "webApp"]

    print("\n" + "=" * 70)
    print("АНАЛИЗ ДОЛИ ПЕРЕИСПОЛЬЗУЕМОГО КОДА")
    print("=" * 70)
    print(f"\n{'Категория':<40} {'Файлы':>6} {'Строки':>8} {'Доля':>7}")
    print("-" * 65)

    def pct(val: int) -> float:
        return val / total_loc * 100 if total_loc > 0 else 0.0

    rows = []
    for cat in order:
        if cat not in results:
            continue
        s = results[cat]
        label = CATEGORIES.get(cat, ENTRY_POINTS.get(cat, cat))
        print(f"  {label:<38} {s.files:>6} {s.loc:>8} {pct(s.loc):>6.1f}%")
        rows.append([cat, label, s.files, s.loc, round(pct(s.loc), 1)])

    print("-" * 65)
    print(f"  {'ИТОГО':<38} {total_files:>6} {total_loc:>8} {'100.0%':>7}")

    # Агрегаты по платформам
    def loc(key: str) -> int:
        return results[key].loc if key in results else 0

    shared_all = loc("commonMain")
    shared_native = loc("nonJsMain")
    android_specific = loc("androidMain") + loc("androidApp")
    desktop_specific = loc("jvmMain") + loc("desktopApp")
    web_specific = loc("jsMain") + loc("webApp")

    print(f"\n{'=' * 70}")
    print("ДОЛЯ ПЕРЕИСПОЛЬЗУЕМОГО КОДА ПО ПЛАТФОРМАМ")
    print("=" * 70)

    platform_rows = []

    def platform_reuse(name: str, shared: int, specific: int):
        total = shared + specific
        p = shared / total * 100 if total > 0 else 0
        print(f"  {name:<20} общий: {shared:>5} LoC | специфичный: {specific:>5} LoC | доля общего: {p:.1f}%")
        platform_rows.append([name, shared, specific, round(p, 1)])

    platform_reuse("Android", shared_all + shared_native, android_specific)
    platform_reuse("Desktop (JVM)", shared_all + shared_native, desktop_specific)
    platform_reuse("Web", shared_all, web_specific)

    platform_specific_total = total_loc - shared_all - shared_native

    print(f"\n  Общий код (commonMain):           {shared_all} LoC ({pct(shared_all):.1f}% от общего объёма)")
    print(f"  Общий нативный (nonJsMain):       {shared_native} LoC ({pct(shared_native):.1f}% от общего объёма)")
    print(f"  Платформенно-специфичный код:      {platform_specific_total} LoC ({pct(platform_specific_total):.1f}% от общего объёма)")
    print()

    # CSV-экспорт: два отдельных файла
    def fmt(val: float) -> str:
        """Форматирует число с запятой как десятичный разделитель для RU-локали."""
        return str(round(val, 1)).replace(".", ",")

    path1 = Path("loc_categories.csv")
    with open(path1, "w", encoding="utf-8-sig", newline="") as f:
        w = csv.writer(f, delimiter=";")
        w.writerow(["Категория", "Файлы", "Строки кода", "Доля кода (%)"])
        for row in rows:
            w.writerow([row[1], row[2], row[3], fmt(row[4])])
        w.writerow(["ИТОГО", total_files, total_loc, fmt(100.0)])

    path2 = Path("loc_platforms.csv")
    with open(path2, "w", encoding="utf-8-sig", newline="") as f:
        w = csv.writer(f, delimiter=";")
        w.writerow(["Платформа", "Общий код (LoC)", "Специфичный код (LoC)", "Доля общего кода (%)"])
        for row in platform_rows:
            w.writerow([row[0], row[1], row[2], fmt(row[3])])

    print(f"CSV-отчёты сохранены: {path1}, {path2}")


def main():
    root = Path(".")
    results = collect_stats(root)
    print_report(results)


if __name__ == "__main__":
    main()