# Contributing to Convene

First off, thank you for considering contributing to Convene! Your help makes this project better for everyone.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Style Guidelines](#style-guidelines)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)

---

## Code of Conduct

This project and everyone participating in it is governed by our commitment to providing a welcoming and inclusive environment. By participating, you are expected to uphold this standard. Please report unacceptable behavior to the repository maintainers.

---

## Getting Started

Before you begin:

- Make sure you have a [GitHub account](https://github.com/signup)
- Ensure you have **Java 17+** installed (`java -version`)
- Familiarize yourself with the [project structure](README.md#project-structure)
- Check existing [issues](../../issues) to see if your contribution is already being discussed

---

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues. When filing a bug report, use our [bug report template](.github/ISSUE_TEMPLATE/bug_report.md) and include:

- A clear and descriptive title
- Steps to reproduce the behavior
- Expected vs actual behavior
- Your environment (OS, Java version)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. Use our [feature request template](.github/ISSUE_TEMPLATE/feature_request.md) and provide:

- A clear and descriptive title
- A detailed description of the proposed enhancement
- Why this enhancement would be useful
- Any alternatives you've considered

### Pull Requests

1. Fork the repository
2. Create a new branch from `main`
3. Make your changes
4. Submit a pull request

---

## Development Setup

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- A terminal / command-line interface
- Git

### Local Setup

```bash
# 1. Clone your fork
git clone https://github.com/YOUR_USERNAME/convene.git
cd convene

# 2. Compile all source files
find src -name "*.java" | xargs javac -d out

# 3. Run the application
java -cp out com.zugobite.convene.Main
```

### Project Structure

```
src/main/java/com/zugobite/convene/
├── Main.java              # Application entry point
├── enums/                 # Enumerations (Role)
├── models/                # Domain objects (User, Student, Staff)
├── controllers/           # Menu and input controllers
├── services/              # Business logic layer
├── utils/                 # Utility classes (validation, console)
└── data/                  # File I/O persistence layer
```

---

## Style Guidelines

### Java Code Style

- Use **4 spaces** for indentation (no tabs)
- Use **meaningful variable and method names** in camelCase
- Use **PascalCase** for class names
- Use **UPPER_SNAKE_CASE** for constants
- Add **Javadoc comments** for all public classes, methods, and fields
- Keep methods short and focused - one responsibility per method
- Prefer **early returns** over deep nesting

### Example

```java
/**
 * Validates that the given string is not null or empty.
 *
 * @param input the string to validate
 * @return true if the string is non-empty, false otherwise
 */
public static boolean isNonEmpty(String input) {
    return input != null && !input.trim().isEmpty();
}
```

### Package Conventions

- `models/` - Plain Java objects representing domain entities
- `enums/` - Enumeration types
- `controllers/` - Classes handling user interaction and menu flow
- `services/` - Business logic and operations on models
- `utils/` - Stateless utility/helper classes
- `data/` - File I/O and persistence operations

### Security Guidelines

- **Validate all user input** before processing
- **Never hardcode credentials** or sensitive data
- Use **exception handling** for all I/O operations
- **Sanitize file paths** to prevent directory traversal

---

## Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Types

| Type | Description |
|------|-------------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation changes only |
| `style` | Formatting, missing semicolons, etc. (no code change) |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `perf` | Performance improvement |
| `test` | Adding or updating tests |
| `chore` | Build process, tooling, or auxiliary changes |

### Examples

```
feat(models): add Event class with capacity management
fix(registration): resolve duplicate registration check
docs(readme): update installation instructions
refactor(controllers): extract common menu logic to base class
chore(gitignore): add IDE-specific excludes
```

---

## Pull Request Process

1. **Update documentation** if you're changing functionality
2. **Update the CHANGELOG.md** with your changes under `[Unreleased]`
3. **Ensure your code compiles** without errors
4. **Fill out the PR template** completely
5. **Request a review** from a maintainer
6. **Address feedback** promptly

### PR Checklist

- [ ] Code follows the project's style guidelines
- [ ] Self-review completed
- [ ] Javadoc comments added for public classes and methods
- [ ] Documentation updated (if applicable)
- [ ] No new compiler warnings generated
- [ ] CHANGELOG.md updated under `[Unreleased]`
- [ ] Changes compile and run successfully

---

## Questions?

Feel free to open an issue with the `question` label if you need help getting started.
