# Application Requirements Specification

> **Convene - Campus Event Management System**
>
> This document defines the complete functional, technical, and submission requirements for the Convene platform.

## 1. Overview

Convene is a **Java console-based Campus Event Management System** that allows students and staff to create, manage, view, and attend campus events.

The system simulates real-world behavior including:

- Role-based access control
- Event capacity management
- Waitlists with automatic promotion
- Automated background processing using threads
- File-based data persistence

## 2. Functional Requirements

### 2.1 User Roles and Access Control

The system supports two user types with distinct permission sets:

#### Staff Users

| Action                         | Permitted |
| ------------------------------ | --------- |
| Create events                  | Yes       |
| Update event details           | Yes       |
| Cancel events                  | Yes       |
| View registered participants   | Yes       |
| View waitlists                 | Yes       |
| Register for events            | No        |

#### Student Users

| Action                         | Permitted |
| ------------------------------ | --------- |
| View available events          | Yes       |
| Register for events            | Yes       |
| Cancel registration            | Yes       |
| View registration status       | Yes       |
| Create, update, or cancel events | No      |

#### System Behavior

- Prompt the user to select a role (Student or Staff) at startup
- Restrict students from creating, updating, or canceling events
- Validate role selection before proceeding to the main menu

### 2.2 Event Management and State Control

Each event must contain the following attributes:

| Field               | Type    | Format / Constraint     |
| ------------------- | ------- | ----------------------- |
| Event ID            | Integer | Unique, positive        |
| Event Name          | String  | Non-empty               |
| Event Date          | String  | `dd/mm/yyyy`            |
| Event Time          | String  | `HH:mm` (24-hour)       |
| Location            | String  | Non-empty               |
| Maximum Participants | Integer | Positive                |

#### Staff Capabilities

- Create new events with all required fields
- Update an event's name, time, or location
- Cancel events
- View all events with:
  - Current registered participant count
  - Current waitlist count
- Sort events by:
  - Event Name (alphabetical)
  - Event Date (chronological)

#### Event State

Each event must maintain:

- A **Registered Participants** list (ordered collection)
- A **Waitlist** queue (FIFO ordering)

### 2.3 Student Registration, Waitlists, and Automation

#### Registration Flow

- Students register for events using an Event ID
- If space is available → student is **registered** automatically
- If the event is full → student is **waitlisted** automatically
- Students can cancel a registration or waitlist entry at any time

#### Automated Waitlist Promotion

When a registered student cancels:

1. The first student in the waitlist is promoted automatically
2. Promotion is handled in a **separate thread**
3. A confirmation message is displayed to the console

**Example output:**

```
Registration cancelled. Student S102 has been promoted from the waitlist to the event.
```

### 2.4 Search, Validation, Error Handling, and Persistence

#### Search Functionality

Users can search events by:

| Search Criteria        | Match Type              |
| ---------------------- | ----------------------- |
| Event Name             | Partial or full match   |
| Event Date             | Exact match             |

Search results display full event details including registered count and waitlist count.

#### Validation Requirements

| Field               | Validation Rule                    |
| ------------------- | ---------------------------------- |
| Event ID            | Numeric, unique, positive integer  |
| Event Date          | Format: `dd/mm/yyyy`              |
| Event Time          | Format: `HH:mm` (24-hour)         |
| Maximum Participants | Positive integer                  |
| Text fields         | Non-empty after trimming           |

#### Error Handling

The system handles the following gracefully:

- Invalid input types (non-numeric where numeric expected)
- References to non-existent events
- Duplicate registration attempts

#### Data Persistence

- Save events, registrations, and waitlists to files on modification
- Automatically load saved data on application startup
- Persist data in `.txt` or `.dat` format

## 3. Technical Requirements

The system must demonstrate the following concepts:

| Concept                 | Implementation                                  |
| ----------------------- | ------------------------------------------------ |
| Classes and Objects     | `User`, `Student`, `Staff`, `Event`, etc.        |
| Inheritance             | `Student` and `Staff` extending `User`           |
| Polymorphism            | Role-specific menu dispatch via base class       |
| Encapsulation           | Private fields with public getters               |
| Collections             | `ArrayList`, `Queue`, `Map`                      |
| Modular Design          | Reusable methods across packages                 |
| Exception Handling      | Try-catch for I/O, parsing, and validation       |
| File I/O                | Read/write events and registrations to files     |
| Multithreading          | Background thread for waitlist promotion         |

## 4. Submission Requirements

The final submission must include:

| Requirement                  | Details                                |
| ---------------------------- | -------------------------------------- |
| Source files                 | All `.java` files                      |
| Data files                   | Any `.txt` / `.dat` persistence files  |
| Application type             | Fully working, console-based (no GUI)  |
| Code quality                 | Properly structured and commented      |
| Package format               | ZIP file under 50MB                    |

The application must:

- Compile successfully with `javac`
- Run successfully with `java`
- Be fully console-based (no GUI components)
- Contain original, well-documented code

## 5. System Expectations

The application must be:

- **Logical** - clear control flow and predictable behavior
- **Well-structured** - clean package hierarchy and separation of concerns
- **Aligned with OOP** - proper use of abstraction, inheritance, and polymorphism
- **Fully operational** - all features working end-to-end
- **Easy to navigate** - intuitive menus and clear prompts
- **Maintainable** - modular design enabling future extension
