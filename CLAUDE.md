# CLAUDE.md — Journal

A journaling app built with Kotlin Multiplatform + Compose Multiplatform, targeting Android, iOS, Desktop, and Web.

## Architecture

This project follows **MVVM + Clean Architecture** with feature-based modularization.

**Layer flow:** UI (Compose screens) → ViewModel (StateFlow) → UseCases → Repository → DataSources

**Key principles:**
- Shared business logic lives in `commonMain`; platform-specific code uses `expect`/`actual`
- Features are isolated into modules with `api` (public contracts), `impl` (implementation), and `schema` (database definitions) submodules
- Dependency injection via Koin — each platform initializes its own DI graph at startup
- Offline-first: local SQLDelight database synced to Supabase via PowerSync on native platforms; Web falls back to direct Supabase access

## Module Layout

- `shared/` — App shell: root composable, navigation, theme, DI initialization
- `features/*/` — Feature modules following the api/impl/schema pattern
- `common/ui/` — Reusable Compose components shared across features
- `common/network/` — HTTP client setup and backend configuration
- `data/database/` — Database drivers, sync infrastructure, PowerSync schema
- `androidApp/`, `iosApp/`, `desktopApp/`, `webApp/` — Platform entry points
- `server/` — Ktor backend stub (not actively used yet)

## Guidelines

### Adding Features

Create a new module group under `features/` following the existing pattern:
- `api` — Public interfaces, models, and contracts other modules can depend on
- `impl` — Presentation (ViewModels, screens), domain (use cases), and data (repositories, data sources)
- `schema` — SQLDelight schema and migration definitions

Register the feature's DI module in the shared Koin setup. Wire navigation in the shared navigation graph.

### Platform Code

Write shared code in `commonMain` by default. Use platform-specific source sets (`androidMain`, `iosMain`, `jvmMain`, `jsMain`) only when a platform API or library demands it.

Use `nonJsMain` only when JS/Web is the sole platform that lacks support for a given technology (e.g., PowerSync). In that case, put the real implementation in `nonJsMain` and a fallback or no-op in `jsMain`.

### State & UI

ViewModels expose UI state via `StateFlow`. Compose screens collect state and dispatch user actions through ViewModel methods. Keep ViewModels in common code, free of platform-specific imports.

Build screens from small, focused composable functions. Shared UI components belong in `common/ui/` so features can reuse them without duplication.

### Database & Sync

Define table schemas in the feature's `schema` module using SQLDelight `.sq` files. The PowerSync schema in `data/database` must stay in sync with SQLDelight definitions — when you add or change a table, update both places.

PowerSync handles bidirectional sync between the local database and Supabase. The `SyncManager` abstraction allows the real sync on native platforms and a no-op on Web.

### Backend & Networking

Supabase is the primary backend — PostgREST for queries, Realtime for subscriptions. The Ktor HTTP client is configured per-platform with appropriate engines.

API configuration (`ApiConfig`) is generated at build time from `local.properties` (gitignored). See `local.properties.example` for the required keys. The generation task lives in `common/network/build.gradle.kts`. Do not hardcode secrets in source code.

### Dependencies & Build

All dependency versions live in `gradle/libs.versions.toml`. Always declare new dependencies there and reference them via the version catalog. Never hardcode versions in `build.gradle.kts` files.

### Conventions

- Package root: `com.kindaboii.journal`
- Use `kotlinx.serialization` for all serialization
- Use `kotlinx.datetime` for date/time — avoid `java.time` in shared code
- Coroutines and Flow are the standard async primitives
- Prefer composition over inheritance in UI code
- Only commit when the user explicitly asks — never autonomously
- Always `git pull` before committing to avoid conflicts with remote changes
- Commit message format: `type: short description` — types: `feat`, `fix`, `refactor`, `chore`, `docs`

## Build Commands

```bash
./gradlew :androidApp:installDebug         # Android
./gradlew :desktopApp:run                  # Desktop
./gradlew :webApp:jsBrowserDevelopmentRun  # Web dev server
# iOS — open iosApp/ in Xcode
```
