# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Build & Development Commands

```bash
# Build
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Run a single test class
./gradlew testDebugUnitTest --tests "com.valdesekamdem.taskflow.feature.home.viewmodel.HomeViewModelTest"

# Lint
./gradlew lintDebug

# Full check (lint + tests)
./gradlew check

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

## Coding style

- Use trailing comma
- Don't add unnecessary comments. E.g. `// --- Edit mode tests ---`

## Architecture

**MVVM + Clean Architecture**, single `:app` module, feature-based package structure under
`com.valdesekamdem.taskflow`.

### Presentation Layer

Every screen follows the same contract:

#### StateHolder Pattern

- **`StateHolder<UiState, UiEvent>`** — interface implemented by each ViewModel; exposes
  `StateFlow<UiState>` and an `onUiEvent(event)` handler.
- **`BindScreen`** — Composable that wires a `StateHolder` to its UI Composable. Screens do not call
  ViewModels directly.
- **`UiFactory`** — each feature registers its screens via `@IntoSet` Hilt multi-binding.
  `MainActivity` collects the `Set<UiFactory>` and passes it to `NavDisplay`.
- Import the specific event to avoid cognitive load when reading the code with multiple events.

```kotlin
// Preferred 
`import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent.TaskClicked`

TaskClicked(task)

// Not preferred
`import com.valdesekamdem.taskflow.feature.home.viewmodel.HomeUiEvent`

HomeUiEvent.TaskClicked(task)
```

#### UI

- Ui texts should be written in `res/values/strings.xml` file for localization
- When you create a UI components add its corresponding preview in the same file show the default
  case.
    - In the case the component can have many states, only add the preview for 2 main states.

### Navigation — Navigation 3 + Custom Navigator

- **`Screen`** — sealed, `@Serializable` hierarchy (`HomeScreen`, `TaskDetailScreen`,
  `EditTaskScreen`, `Back`).
- **`Navigator`** interface — single `goTo(screen: Screen)` method. ViewModels call this; they never
  touch backstack directly.
- **`RealNavigator`** — singleton; emits `NavigationEvent` via a `Channel`. `MainActivity` collects
  these events and mutates the `NavBackStack<NavKey>`.
- Don't navigate from the UI (composable), always navigate from the ViewModel using `goTo(screen)`.

### Data Layer

- **`TaskRepository`** interface (`feature/task/data/api`) — all data access related to tasks goes
  through this.
- **`RealTaskRepository`** — Room-backed implementation using `TaskDao`. Maps `TaskEntity` ↔ `Task`
  domain model.
- New entity should follow this pattern

#### Additional

- **`TaskFlowDatabase`** — single Room database (`"taskflow-database"`), with `InstantConverter` for
  `kotlinx.datetime.Instant`.
- **`Clock`** abstraction (`core/clock`) — injected everywhere time is needed; enables deterministic
  tests via `FakeClock`.

### Dependency Injection — Hilt

- `@HiltAndroidApp` on `TaskFlowApplication`, `@AndroidEntryPoint` on `MainActivity`,
  `@HiltViewModel` on ViewModels.
- Each feature has a `*DataModule` (binds repository interface → real impl) and a `*UiModule` (adds
  `UiFactory` to the Hilt set).
- Navigation bindings live in `NavigationModule` (SingletonComponent).

## Testing Conventions

Unit tests live in `app/src/test/`. The project avoids mocking libraries — use the **fake
implementations** provided or create new one if necessary:

| Fake                 | Location                   |
|----------------------|----------------------------|
| `FakeClock`          | `core/clock/fakes/`        |
| `FakeNavigator`      | `core/navigation/fakes/`   |
| `FakeTaskRepository` | `feature/task/data/fakes/` |

- A fake class should live in a `fakes` package and have a `Fake` prefix.

Flow/StateFlow assertions use **Turbine** (`app.cash.turbine`).

- Use the `StateHolder.test { }` extension in `feature/utils/ViewModelUtils.kt` to assert ViewModel
  state sequences.
- Use the `stateflow.test { }` from Turbine to assert Flow state sequences.
- Prioritize assessing data class instead of field-by-field.
- When assessing fields, use `with(awaitItem()) { }` to unpack the value of a data class.

Tests use `runTest` (coroutines-test) and `@get:Rule` with `DefaultLocaleRule` when locale-sensitive
formatting is involved.

Always consume all events. Don't add `cancelAndIgnoreRemainingEvents()` to avoid consuming all
events unless it's explicit asked.

## Git

- Add new files to VCS

## Big Gotchas

### Nested Scaffold inset double-consumption

`contentWindowInsets = WindowInsets(0)` on a child Scaffold only suppresses insets when that
Scaffold has **no** `topBar`/`bottomBar`. It does NOT prevent the parent Scaffold's `innerPadding`
from being applied twice. The correct fix is to add `Modifier.consumeWindowInsets(innerPadding)` to
the content area (e.g., the inner `NavDisplay`) so child Scaffolds know those insets are already
consumed.

### Navigator must never be injected into Composables

Navigation logic belongs in ViewModels. Composables receive `uiState` and `onUiEvent` only. A
Composable that holds a `Navigator` reference bypasses the ViewModel and makes navigation
untestable.

### `onBack` in an inner `NavDisplay` must not call `navigator.goTo(Back)`

`navigator.goTo(Back)` routes to the **outer** navigation, which quits the app rather than
returning to the Home tab. The inner `NavDisplay`'s `onBack` should fire a ViewModel event
(e.g. `BackPressed`) and let the ViewModel decide whether to pop the inner backstack or delegate
to the outer navigation.

### Tab navigation from a deep screen leaves the outer backstack stale

When `navigator.goTo(TabScreen)` is called from a full-screen overlay (e.g. `TaskDetailScreen`),
the tab event updates the inner backstack but the outer backstack (`[MainScreen, TaskDetailScreen]`)
is not touched — the overlay stays visible. Fix: emit `NavigationEvent.PopToRoot` on the outer
channel first so `BindNavigator` clears the outer stack back to `MainScreen` before the tab switch
is processed.

### Kotlin `Channel` has a single consumer

If outer navigation and tab navigation share the same `Channel`, whichever coroutine collects first
will consume events meant for the other. Use two separate channels — one for outer events
(`NavigationEvent`) and one for tab events (`TabNavigationEvent`).

### `entryProvider(lambdaRef)` does not compile in Navigation 3

`entryProvider` does not accept a lambda reference directly. Wrap the call:
```kotlin
entryProvider { tabEntryProvider() }  // correct
entryProvider(tabEntryProvider)        // compile error
```

### Turbine `test {}` does not expose `onUiEvent`

Inside Turbine's `Flow.test { }` block, `this` is `TurbineTestContext` — `onUiEvent` is not in
scope. Either call `viewModel.onUiEvent(...)` explicitly, or use the project's `StateHolder.test {}`
extension (from `ViewModelUtils.kt`) when you only need to fire events and check `uiState.value`
without collecting a sequence of states.