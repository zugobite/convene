# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added

- Nothing yet

### Changed

- Nothing yet

### Fixed

- Nothing yet

---

## [0.3.0] - 2026-02-17

### Added

#### Student Registration, Waitlists, and Automation (Section 2.3)

- Student: Register for events by Event ID (auto-register if space, auto-waitlist if full)
- Student: Cancel registration or waitlist entry for any event
- Student: View registration status across all events (registered/waitlisted with position)
- Automated waitlist promotion in a **separate background thread** when a registered student cancels
- Promotion thread outputs confirmation message matching spec format
- `EventManager.registerStudent()` with status returns: REGISTERED, WAITLISTED, DUPLICATE, CANCELLED, NOT_FOUND
- `EventManager.cancelRegistration()` with threaded promotion and status returns
- `EventManager.getEventsForStudent()` for cross-event student lookup
- Duplicate registration detection and graceful error handling

### Changed

- `StudentMenuController` stubs replaced with full registration, cancellation, and status implementations
- `EventManager` version bumped to 0.3.0 with registration and threading logic
- `StudentMenuController` version bumped to 0.3.0

---

## [0.2.0] - 2026-02-17

### Added

#### Event Management & State Control (Section 2.2)

- `Event` model class with unique ID, name, date, time, location, max participants, registered list, and waitlist queue
- `EventManager` service class with HashMap-based event store and O(1) lookup by ID
- Staff: Create events with validated fields (ID, name, date, time, location, capacity)
- Staff: Update event name, time, or location via interactive sub-menu
- Staff: Cancel events with confirmation prompt (soft-delete preserving history)
- Staff: View participants and waitlists with sort options (by name, date, or default order)
- Student: View all active events with sort options (by name, date, or default order)
- Event sorting by name (case-insensitive alphabetical) and date (chronological dd/mm/yyyy parsing)

#### Validation Enhancements

- `InputValidator.isValidDate()` for dd/mm/yyyy format with leap year handling
- `InputValidator.isValidTime()` for HH:mm 24-hour format validation
- `InputValidator.isPositiveInteger()` for capacity and ID validation
- Full input validation loops in event creation (re-prompts on invalid input)

### Changed

- `StaffMenuController` now accepts `EventManager` and implements real CRUD actions (replaces stubs)
- `StudentMenuController` now accepts `EventManager` and implements real event viewing (replaces stub)
- `Main.java` initialises shared `EventManager` and passes it to both controllers
- `InputValidator` version bumped to 0.2.0

---

## [0.1.0] - 2026-02-17

### Added

#### Project Structure

- Maven-compatible directory layout (`src/main/java/`) for vanilla Java
- Package structure: `com.zugobite.convene` with `models`, `enums`, `controllers`, `services`, `utils`, `data` sub-packages
- Runtime `data/` directory for file-based persistence
- `docs/` directory for project documentation

#### User Roles & Access Control (Section 2.1)

- `Role` enum with `STUDENT` and `STAFF` constants
- Abstract `User` base class with `showMenu()` and `hasPermission()` methods
- `Student` class extending `User` with student-specific menu and permissions
- `Staff` class extending `User` with staff-specific menu and permissions
- `MenuController` for role selection at startup with input validation
- `StudentMenuController` for student menu loop and action dispatch
- `StaffMenuController` for staff menu loop and action dispatch
- Role-based access control restricting students from event management actions

#### Utilities

- `InputValidator` with methods for non-empty strings, integer validation, and menu choice validation
- `ConsoleUtils` with formatted header banners, dividers, and "press enter" prompts

#### Console Application

- `Main` entry point with welcome banner, role selection, and menu routing
- Graceful exit handling with farewell message

#### Documentation & GitHub Meta

- `README.md` with badges, features, quick start, project structure, and usage examples
- `CHANGELOG.md` following Keep a Changelog format
- `CONTRIBUTING.md` with development setup, style guidelines, and PR process
- `LICENSE` - MIT License
- `.github/SECURITY.md` with vulnerability reporting policy
- `.github/ISSUE_TEMPLATE/bug_report.md` and `feature_request.md`
- `.github/PULL_REQUEST_TEMPLATE.md`
- `.github/CODEOWNERS` assigning repository ownership
- `.github/FUNDING.yml` for sponsorship configuration
- `docs/001-PROJECT_OVERVIEW.md` with architecture overview and class hierarchy
- `.gitignore` for Java, IDE, and OS files

---

[Unreleased]: https://github.com/zugobite/convene/compare/v0.3.0...HEAD
[0.3.0]: https://github.com/zugobite/convene/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/zugobite/convene/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/zugobite/convene/releases/tag/v0.1.0
