# Convene

A modern, console-based campus event management system built from scratch with vanilla Java, demonstrating OOP principles, multithreading, and file-based persistence.

[![Java](https://img.shields.io/badge/Java-17+-ED8B00.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Features

- **Role-Based Access Control** - Students and staff have separate permissions and menu interfaces
- **Event Management** - Create, update, cancel, and view campus events with full CRUD support
- **Capacity & Waitlists** - Automatic waitlisting when events reach capacity, with queue-based promotion
- **Automated Promotion** - Background thread promotes waitlisted students when spots open up
- **Search & Filter** - Find events by name (partial match) or date, with sortable results
- **Input Validation** - Strict validation for dates, times, IDs, and all user input
- **Data Persistence** - File-based save/load ensures data survives between sessions
- **Clean Console UI** - Formatted menus, dividers, and prompts for a polished terminal experience
- **Exception Handling** - Graceful error recovery for invalid input and file operations
- **Modular Design** - Clean package separation with reusable utility classes

## Table of Contents

- [Quick Start](#quick-start)
- [Documentation](#documentation)
- [Menu Commands & Actions](#menu-commands--actions)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Class Hierarchy](#class-hierarchy)
- [Development](#development)
- [Contributing](#contributing)
- [Disclaimer](#disclaimer)
- [Security](#security)
- [License](#license)

## Quick Start

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- A terminal / command-line interface

### Installation

```bash
# Clone the repository
git clone https://github.com/zugobite/convene.git
cd convene

# Compile all Java source files
find src -name "*.java" | xargs javac -d out

# Run the application
java -cp out com.zugobite.convene.Main
```

The application will launch in your terminal.

## Documentation

Extensive developer documentation is available in the [`docs/`](docs/) folder:

| Document                                                                | Description                                    |
| ----------------------------------------------------------------------- | ---------------------------------------------- |
| [001-APPLICATION_REQUIREMENTS.md](docs/001-APPLICATION_REQUIREMENTS.md)     | Full application requirements specification    |
| [002-PROJECT_OVERVIEW.md](docs/002-PROJECT_OVERVIEW.md)                     | Architecture, package map, and class hierarchy |

## Menu Commands & Actions

### Role Selection

| Option | Role    | Description                                      |
| ------ | ------- | ------------------------------------------------ |
| `1`    | Student | View events, register, cancel, check status      |
| `2`    | Staff   | Create, update, cancel events, view participants |

### Student Actions

| Option | Action                   | Description                                  |
| ------ | ------------------------ | -------------------------------------------- |
| `1`    | View Available Events    | List all upcoming campus events              |
| `2`    | Register for Event       | Register by Event ID (auto-waitlist if full) |
| `3`    | Cancel Registration      | Cancel registration or waitlist entry        |
| `4`    | View Registration Status | Check if Registered or Waitlisted            |
| `5`    | Search Events            | Search by event name or date                 |
| `6`    | Exit                     | Exit the application                         |

### Staff Actions

| Option | Action                        | Description                          |
| ------ | ----------------------------- | ------------------------------------ |
| `1`    | Create Event                  | Create a new campus event            |
| `2`    | Update Event                  | Update event name, time, or location |
| `3`    | Cancel Event                  | Cancel an existing event             |
| `4`    | View Participants & Waitlists | View registered and waitlisted users |
| `5`    | Search Events                 | Search by event name or date         |
| `6`    | Exit                          | Exit the application                 |

## Usage Examples

### Complete User Flow

```bash
# 1. Compile and run
find src -name "*.java" | xargs javac -d out
java -cp out com.zugobite.convene.Main

# 2. Select your role
# Choose 1 for Student or 2 for Staff

# 3. Enter your credentials
# Provide a User ID (e.g., S101) and your name

# 4. Navigate the menu
# Select options by entering the corresponding number

# 5. Register for an event (Student)
# Choose option 2 and enter the Event ID

# 6. Create an event (Staff)
# Choose option 1 and fill in event details
```

### Quick Start Commands

```bash
# Compile all source files
find src -name "*.java" | xargs javac -d out

# Run the application
java -cp out com.zugobite.convene.Main
```

## Project Structure

```
convene/
├── .github/
│   ├── ISSUE_TEMPLATE/                 # Issue templates
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   ├── PULL_REQUEST_TEMPLATE.md        # PR template
│   ├── SECURITY.md                     # Security policy
│   ├── CODEOWNERS                      # Code ownership rules
│   └── FUNDING.yml                     # Sponsorship info
├── src/main/java/com/zugobite/convene/
│   ├── Main.java                       # Application entry point
│   ├── enums/
│   │   └── Role.java                   # STUDENT / STAFF enum
│   ├── models/
│   │   ├── User.java                   # Abstract base class
│   │   ├── Student.java                # Student user type
│   │   ├── Staff.java                  # Staff user type
│   │   └── Event.java                  # Event domain model
│   ├── controllers/
│   │   ├── MenuController.java         # Role selection & routing
│   │   ├── StudentMenuController.java  # Student menu handler
│   │   └── StaffMenuController.java    # Staff menu handler
│   ├── services/
│   │   └── EventManager.java           # Event CRUD & sorting service
│   ├── utils/
│   │   ├── InputValidator.java         # Input validation helpers
│   │   └── ConsoleUtils.java           # Console formatting helpers
│   └── data/                           # File I/O persistence layer
├── data/                               # Runtime data files
├── docs/                               # Developer documentation
│   ├── 001-APPLICATION_REQUIREMENTS.md
│   └── 002-PROJECT_OVERVIEW.md
├── .gitignore                          # Git ignore rules
├── CHANGELOG.md                        # Version history
├── CONTRIBUTING.md                     # Contribution guidelines
├── LICENSE                             # MIT License
└── README.md
```

## Class Hierarchy

The application uses the following class structure:

| Class                   | Type       | Description                                         |
| ----------------------- | ---------- | --------------------------------------------------- |
| `User`                  | Abstract   | Base class with userId, name, role, and permissions |
| `Student`               | Concrete   | Student user with view/register/cancel actions      |
| `Staff`                 | Concrete   | Staff user with create/update/cancel actions        |
| `Role`                  | Enum       | STUDENT and STAFF role constants                    |
| `Event`                 | Model      | Event with ID, schedule, capacity, and waitlist     |
| `EventManager`          | Service    | Event store with CRUD, listing, and sorting logic   |
| `MenuController`        | Controller | Role selection and routing at startup               |
| `StudentMenuController` | Controller | Student menu loop and action dispatch               |
| `StaffMenuController`   | Controller | Staff menu loop and action dispatch                 |
| `InputValidator`        | Utility    | Validation for dates, times, IDs, and menus         |
| `ConsoleUtils`          | Utility    | Console formatting, banners, and prompts            |
| `Main`                  | Entry      | Application entry point                             |

For the complete class hierarchy and package map, see [docs/002-PROJECT_OVERVIEW.md](docs/002-PROJECT_OVERVIEW.md).

## Development

```bash
# Compile all source files
find src -name "*.java" | xargs javac -d out

# Run the application
java -cp out com.zugobite.convene.Main
```

### Architecture

This project follows an MVC-inspired architecture with clean package separation:

- **Models** define domain entities with encapsulated fields and behavior
- **Controllers** handle user interaction, menu flow, and input processing
- **Services** contain business logic for event management and registration
- **Utils** provide stateless helper methods for validation and formatting
- **Enums** define type-safe constants for roles and actions

### OOP & Technical Features

| Feature            | Implementation                                    |
| ------------------ | ------------------------------------------------- |
| Abstraction        | `User` abstract class with `showMenu()` contract  |
| Inheritance        | `Student` and `Staff` extend `User`               |
| Polymorphism       | Menu dispatch via `User` reference                |
| Encapsulation      | Private fields with public getters                |
| Collections        | `ArrayList`, `Queue`, `HashMap`                   |
| Multithreading     | Background waitlist promotion thread              |
| File I/O           | Persistent save/load for events and registrations |
| Exception Handling | Graceful recovery for all invalid input           |

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feat/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feat/amazing-feature`)
5. Open a Pull Request

### Guidelines

- Follow the existing code style
- Add Javadoc comments for new classes and methods
- Update documentation as needed
- Keep commits atomic and well-described

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

## Disclaimer

This project is created **purely for educational and portfolio demonstration purposes** to showcase Java development skills and software engineering knowledge.

**Important notices:**

- It implements **industry-standard patterns** documented in publicly available resources including:
  - Object-oriented design principles (abstraction, inheritance, polymorphism, encapsulation)
  - Java concurrency patterns for multithreading
  - MVC architectural separation
- This is **not intended for production use** without proper security audits and additional hardening
- The codebase demonstrates OOP principles, multithreading, file I/O, and modular design

## Security

If you discover a security vulnerability, please report it privately rather than opening a public issue. See [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
