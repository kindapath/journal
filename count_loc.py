#!/usr/bin/env python3
"""
Анализ доли переиспользуемого кода в KMP-проекте.
Запуск: python3 count_loc.py  (из корня проекта)
"""

import os
import re
from collections import defaultdict
from pathlib import Path

# Какие расширения считаем
EXTENSIONS = {".kt", ".sq"}

# Категории source sets
CATEGORIES = {
    "commonMain": "Общий код (все платформы)",
    "nonJsMain": "Общий код (нативные платформы)",
    "androidMain": "Android-специфичный",
    "jvmMain": "Desktop-специфичный (JVM)",
    "jsMain": "Web-специфичный (JS)",
    "iosMain": "iOS-специфичный",
}

# Точки входа — отдельные модули
ENTRY_POINTS = {
    "androidApp": "Android (точка входа)",
    "desktopApp": "Desktop (точка входа)",
    "webApp": "Web (точка входа)",
    "iosApp": "iOS (точка входа)",
}

def count_loc(filepath):
    """Считает строки кода без пустых строк и однострочных комментариев."""
    try:
        with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
            lines = f.readlines()
    except Exception:
        return 0

    count = 0
    in_block_comment = False
    for line in lines:
        stripped = line.strip()

        # Блочные комментарии
        if in_block_comment:
            if "*/" in stripped:
                in_block_comment = False
            continue
        if stripped.startswith("/*"):
            if "*/" not in stripped:
                in_block_comment = True
            continue

        # Пустые строки и однострочные комментарии
        if not stripped or stripped.startswith("//"):
            continue

        count += 1
    return count


def classify_file(filepath):
    """Определяет категорию файла по пути."""
    parts = Path(filepath).parts

    # Точки входа
    for ep in ENTRY_POINTS:
        if ep in parts:
            return ep

    # Source sets внутри модулей
    for cat in CATEGORIES:
        if cat in parts:
            return cat

    # Прочее (server, build scripts, etc.)
    return None


def main():
    root = Path(".")
    results = defaultdict(lambda: {"files": 0, "loc": 0})

    for ext in EXTENSIONS:
        for filepath in root.rglob(f"*{ext}"):
            # Пропускаем build, generated, test
            str_path = str(filepath)
            if any(skip in str_path for skip in ["/build/", "/generated/", "Test/", "test/"]):
                continue

            category = classify_file(filepath)
            if category is None:
                continue

            loc = count_loc(filepath)
            results[category]["files"] += 1
            results[category]["loc"] += loc

    # Вывод
    print("\n" + "=" * 70)
    print("АНАЛИЗ ДОЛИ ПЕРЕИСПОЛЬЗУЕМОГО КОДА")
    print("=" * 70)

    total_loc = sum(v["loc"] for v in results.values())
    total_files = sum(v["files"] for v in results.values())

    # Порядок вывода
    order = ["commonMain", "nonJsMain", "androidMain", "jvmMain", "jsMain", "iosMain",
             "androidApp", "desktopApp", "webApp", "iosApp"]

    print(f"\n{'Категория':<40} {'Файлы':>6} {'Строки':>8} {'Доля':>7}")
    print("-" * 65)

    for cat in order:
        if cat not in results:
            continue
        r = results[cat]
        label = CATEGORIES.get(cat, ENTRY_POINTS.get(cat, cat))
        pct = r["loc"] / total_loc * 100 if total_loc > 0 else 0
        print(f"  {label:<38} {r['files']:>6} {r['loc']:>8} {pct:>6.1f}%")

    print("-" * 65)
    print(f"  {'ИТОГО':<38} {total_files:>6} {total_loc:>8} {'100.0%':>7}")

    # Агрегаты
    shared_all = results["commonMain"]["loc"]
    shared_native = results["nonJsMain"]["loc"]
    android_specific = results["androidMain"]["loc"] + results["androidApp"]["loc"]
    desktop_specific = results["jvmMain"]["loc"] + results["desktopApp"]["loc"]
    web_specific = results["jsMain"]["loc"] + results["webApp"]["loc"]
    ios_specific = results["iosMain"]["loc"] + results.get("iosApp", {"loc": 0})["loc"]

    print(f"\n{'=' * 70}")
    print("ДОЛЯ ПЕРЕИСПОЛЬЗУЕМОГО КОДА ПО ПЛАТФОРМАМ")
    print("=" * 70)

    def platform_reuse(name, shared, platform_specific):
        total = shared + platform_specific
        pct = shared / total * 100 if total > 0 else 0
        print(f"  {name:<20} общий: {shared:>5} LoC | специфичный: {platform_specific:>5} LoC | доля общего: {pct:.1f}%")

    platform_reuse("Android", shared_all + shared_native, android_specific)
    platform_reuse("Desktop (JVM)", shared_all + shared_native, desktop_specific)
    platform_reuse("Web", shared_all, web_specific)
    platform_reuse("iOS", shared_all + shared_native, ios_specific)

    print(f"\n  Общий код (commonMain):           {shared_all} LoC ({shared_all/total_loc*100:.1f}% от общего объёма)")
    print(f"  Общий нативный (nonJsMain):       {shared_native} LoC ({shared_native/total_loc*100:.1f}% от общего объёма)")
    print(f"  Платформенно-специфичный код:      {total_loc - shared_all - shared_native} LoC ({(total_loc - shared_all - shared_native)/total_loc*100:.1f}% от общего объёма)")
    print()


if __name__ == "__main__":
    main()
