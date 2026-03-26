#!/usr/bin/env python3
# Подсчёт строк кода (LoC) в KMP-проекте и анализ доли переиспользования.
# Результат — два CSV-файла: по модулям и по платформам.
# Запуск: python3 count_loc.py (из корня проекта)

import csv
from dataclasses import dataclass
from pathlib import Path

EXTENSIONS = {".kt", ".sq"}

SOURCE_SETS = {"commonMain", "nonJsMain", "androidMain", "jvmMain", "jsMain"}
ENTRY_POINTS = {"androidApp", "desktopApp", "webApp"}

ALL_MODULES = [
    "commonMain", "nonJsMain",
    "androidMain", "jvmMain", "jsMain",
    "androidApp", "desktopApp", "webApp",
]

SKIP = {"/build/", "/generated/", "Test/", "test/", "/iosMain/", "/iosApp/", "/bin/", "server/", "buildSrc/"}


@dataclass
class Stats:
    files: int = 0
    loc: int = 0


def count_loc(path: Path) -> int:
    try:
        lines = path.read_text(encoding="utf-8", errors="ignore").splitlines()
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
            in_block = "*/" not in stripped
            continue

        if stripped and not stripped.startswith("//"):
            count += 1

    return count


def classify(path: Path) -> str | None:
    path_str = path.as_posix()

    for entry_point in ENTRY_POINTS:
        if path_str.startswith(f"{entry_point}/") or f"/{entry_point}/" in path_str:
            return entry_point

    for source_set in SOURCE_SETS:
        if f"/src/{source_set}/" in path_str:
            return source_set

    return None


def collect(root: Path) -> dict[str, Stats]:
    results: dict[str, Stats] = {}

    for extension in EXTENSIONS:
        for filepath in root.rglob(f"*{extension}"):
            if any(skip in filepath.as_posix() for skip in SKIP):
                continue

            module = classify(filepath)
            if module is None:
                continue

            if module not in results:
                results[module] = Stats()
            results[module].files += 1
            results[module].loc += count_loc(filepath)

    return results


def get_lines(results: dict[str, Stats], key: str) -> int:
    return results[key].loc if key in results else 0


def percentage(value: int, total: int) -> float:
    return value / total * 100 if total > 0 else 0.0


def export(results: dict[str, Stats]) -> None:
    total_loc = sum(s.loc for s in results.values())
    total_files = sum(s.files for s in results.values())

    with open("loc_modules.csv", "w", encoding="utf-8-sig", newline="") as file:
        writer = csv.writer(file, delimiter=";")
        writer.writerow(["Модуль", "Файлы", "Строки кода"])
        for module in ALL_MODULES:
            if module in results:
                s = results[module]
                writer.writerow([module, s.files, s.loc])
        writer.writerow(["ИТОГО", total_files, total_loc])

    shared_all = get_lines(results, "commonMain")
    shared_native = get_lines(results, "nonJsMain")

    platforms = [
        ("Android",       shared_all + shared_native, get_lines(results, "androidMain") + get_lines(results, "androidApp")),
        ("Desktop", shared_all + shared_native, get_lines(results, "jvmMain") + get_lines(results, "desktopApp")),
        ("Web",           shared_all,                  get_lines(results, "jsMain") + get_lines(results, "webApp")),
    ]

    with open("loc_platforms.csv", "w", encoding="utf-8-sig", newline="") as file:
        writer = csv.writer(file, delimiter=";")
        writer.writerow(["Платформа", "Переиспользуемый код", "Платформенный код", "Доля переиспользуемого кода (%)"])
        for name, shared, specific in platforms:
            pct = str(round(percentage(shared, shared + specific), 1)).replace(".", ",")
            writer.writerow([name, shared, specific, pct])

    print(f"Готово. Всего {total_loc} LoC в {total_files} файлах.")
    print("Сохранено: loc_modules.csv, loc_platforms.csv")


if __name__ == "__main__":
    export(collect(Path(".")))