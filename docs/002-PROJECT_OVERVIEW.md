# Project Overview

## Architecture

Convene is a Java console-based Campus Event Management System built with vanilla Java (no external dependencies or build tools). The application follows object-oriented principles with a clear separation of concerns across well-defined packages.

### Application Flow

```
Main.java (Entry Point)
    │
    ├── ConsoleUtils.printHeader()            # Display welcome banner
    │
    ├── MenuController.selectRole()           # Prompt user for role selection
    │       │
    │       ├── Creates Student object        # If role = STUDENT
    │       └── Creates Staff object          # If role = STAFF
    │
    └── Role-based Menu Controller
            │
            ├── StudentMenuController         # Student menu loop
            │       ├── View Events
            │       ├── Register for Event
            │       ├── Cancel Registration
            │       ├── View Registration Status
            │       └── Search Events
            │
            └── StaffMenuController           # Staff menu loop
                    ├── Create Event
                    ├── Update Event
                    ├── Cancel Event
                    ├── View Participants & Waitlists
                    └── Search Events
```

---

## Package Map

```
com.zugobite.convene
├── Main.java                                 # Application entry point
│
├── enums/
│   └── Role.java                             # STUDENT, STAFF enum
│
├── models/
│   ├── User.java                             # Abstract base class (OOP: abstraction)
│   ├── Student.java                          # Student user (OOP: inheritance)
│   └── Staff.java                            # Staff user (OOP: inheritance)
│
├── controllers/
│   ├── MenuController.java                   # Role selection and routing
│   ├── StudentMenuController.java            # Student menu handler
│   └── StaffMenuController.java              # Staff menu handler
│
├── services/                                 # Business logic (future sections)
│
├── utils/
│   ├── InputValidator.java                   # Input validation utilities
│   └── ConsoleUtils.java                     # Console formatting utilities
│
└── data/                                     # File I/O persistence (future sections)
```

---

## Class Hierarchy

```
User (abstract)
├── userId: String
├── name: String
├── role: Role
├── showMenu(): void (abstract)
├── hasPermission(String): boolean (abstract)
│
├── Student
│   ├── showMenu() → Student-specific menu
│   └── hasPermission() → VIEW_EVENTS, REGISTER, CANCEL_REGISTRATION, VIEW_STATUS
│
└── Staff
    ├── showMenu() → Staff-specific menu
    └── hasPermission() → CREATE_EVENT, UPDATE_EVENT, CANCEL_EVENT, VIEW_PARTICIPANTS
```

---

## OOP Principles Demonstrated

| Principle         | Implementation                                                |
| ----------------- | ------------------------------------------------------------- |
| **Abstraction**   | `User` is abstract with `showMenu()` and `hasPermission()`    |
| **Inheritance**   | `Student` and `Staff` extend `User`                           |
| **Polymorphism**  | `Main` uses `User` reference; menu dispatch is polymorphic    |
| **Encapsulation** | Private fields with public getters in all model classes       |
| **Enums**         | `Role` enum with display name field and `toString()` override |

---

## Technical Stack

| Component       | Technology                                           |
| --------------- | ---------------------------------------------------- |
| Language        | Java 17+                                             |
| Build           | Manual `javac` compilation (Maven-compatible layout) |
| Persistence     | File I/O (`.txt` / `.dat` files)                     |
| Concurrency     | Java threads for waitlist promotion                  |
| Data Structures | `ArrayList`, `LinkedList` (Queue), `HashMap`         |
| Architecture    | MVC-inspired (Models, Controllers, Services)         |

---

## Requirements Reference

See [001-APPLICATION_REQUIREMENTS.md](001-APPLICATION_REQUIREMENTS.md) for the full application requirements specification.
