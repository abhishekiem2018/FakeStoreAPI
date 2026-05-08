# FakeStore API Automation

## Framework Used
- Java
- Rest Assured
- TestNG
- Maven

## Why?
Rest Assured is lightweight, easy for API validation, and supports schema validation.

## Covered
- Cart CRUD
- Positive tests
- Negative tests
- Authentication
- Data-driven testing
- Schema validation

## Run
mvn clean test

## Future Enhancements
- Parallel execution
- Extent Reports
- CI/CD integration
- - Contract validation

Note:
FakeStoreAPI behaves as a mock/demo API and does not persist created resources.
POST, PUT, and DELETE endpoints simulate responses without actual database operations.
Therefore, CRUD validations were focused on response assertions and contract validation rather than true persistence verification.