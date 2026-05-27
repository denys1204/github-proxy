# GitHub Proxy API

A simple RESTful proxy application that consumes the GitHub API (v3). It retrieves a list of non-fork repositories for a given GitHub user, including branch names and their latest commit SHAs.

## Tech Stack
* **Java:** 25
* **Framework:** Spring Boot 4 (Spring Web)
* **Build Tool:** Gradle with Kotlin DSL
* **Testing:** WireMock (for integration testing without real GitHub API calls)

## Architecture Overview
The application adheres to strict minimalism as per the requirements:
* Single-package structure without complex layering.
* Uses standard Controller -> Service -> Client flow.
* No external databases or security layers.

## Prerequisites
* **JDK 25** must be installed on your machine.
* *Note: You don't need to install Gradle manually, the project uses Gradle Wrapper.*

## How to Run

To run the application locally, use the Gradle wrapper from the root directory of the project.

On Windows:
```bash
.\gradlew.bat bootRun
```

On Linux/macOS:
```bash
./gradlew bootRun
```

The application will start on http://localhost:8080.

## How to Test

The project includes integration tests that mock the GitHub API using WireMock, ensuring fast and reliable execution without hitting GitHub rate limits.

To run the tests:

On Windows:
```bash
.\gradlew.bat test
```

On Linux/macOS:
```bash
./gradlew test
```

## API Documentation

### Get User Repositories
Retrieves all repositories for a specific user that are not forks.

Endpoint:
`GET /api/v1/github/{username}`

Example using `curl`:
```bash
curl -H "Accept: application/json" http://localhost:8080/api/v1/github/octocat
```

```markdown
Success Response (200 OK):
```

```json
[
  {
    "repositoryName": "repo-name",
    "ownerLogin": "username",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "5c15d852dbb5059bb29d201a938682e0ee37b600"
      }
    ]
  }
]
```

```markdown
Error Response (404 Not Found):
```

Returned if the GitHub user does not exist.
```json
{
  "status": 404,
  "message": "User not found"
}
```
