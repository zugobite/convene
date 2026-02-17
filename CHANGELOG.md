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

[Unreleased]: https://github.com/zugobite/convene/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/zugobite/convene/releases/tag/v0.1.0
