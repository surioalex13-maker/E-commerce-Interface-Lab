# Contributing Guidelines

This document outlines the best practices for contributing to the E-commerce API project, with emphasis on pair programming collaboration.

## Pair Programming Model

This project uses **pair programming** for all development. Both partners must actively participate and understand all implementations.

### Roles

#### Driver (Code Writer)
- Writes the code
- Handles keyboard and typing
- Implements features
- Follows the navigator's suggestions

#### Navigator (Code Reviewer)
- Reviews code in real-time
- Suggests improvements
- Points out potential issues
- Asks clarifying questions
- Maintains broader context

### Role Rotation

Rotate roles after each **completed task** or every **30 minutes** of active coding:

```
Task 1: Partner A drives, Partner B navigates
Task 2: Partner B drives, Partner A navigates
Task 3: Partner A drives, Partner B navigates
...
```

## Commit Conventions

### Commit Message Format

Follow the "Action + Result" format:

```
<Type>: <action phrase describing what was implemented>
```

### Types

- `feat:` - New feature implementation
- `fix:` - Bug fix
- `docs:` - Documentation updates
- `refactor:` - Code restructuring without changing functionality
- `test:` - Adding or updating tests
- `chore:` - Maintenance tasks (dependencies, config, etc.)

### Examples

```
feat: implemented product filtering by price range
feat: created ProductService with CRUD operations
feat: added global exception handler with error responses
fix: resolved getAllProducts() returning null values
docs: added comprehensive API documentation
refactor: extracted validation logic to separate method
```

### Co-authoring Commits

When both partners contributed meaningfully, mark co-authorship:

```
git commit -m "feat: implemented product filtering

Co-authored-by: Partner Name <email@example.com>"
```

## Code Review Checklist

Before committing, ensure:

- [ ] Code compiles without errors
- [ ] All imports are used
- [ ] No hardcoded values (magic numbers)
- [ ] Variable/method names are descriptive
- [ ] Javadoc comments are present
- [ ] Comments explain the "why", not the "what"
- [ ] No unnecessary code duplication
- [ ] Exception handling is proper
- [ ] All public methods are documented
- [ ] Both partners understand the implementation

## Branch Strategy

### Branch Naming Convention

```
<type>/<feature-name>
```

Types:
- `feat/` - New features
- `fix/` - Bug fixes
- `refactor/` - Code improvements
- `docs/` - Documentation changes

### Examples

```
feat/product-filtering
feat/error-handling
feat/api-endpoints
fix/null-pointer-exception
docs/api-reference
```

### Branch Lifecycle

1. Create feature branch from `main`
   ```bash
   git checkout -b feat/feature-name
   ```

2. Make commits with clear messages
   ```bash
   git commit -m "feat: implemented core functionality"
   ```

3. Push to remote
   ```bash
   git push origin feat/feature-name
   ```

4. Review code together
   ```bash
   git diff main...feat/feature-name
   ```

5. Merge to main (after both partners review)
   ```bash
   git checkout main
   git pull origin main
   git merge feat/feature-name
   git push origin main
   ```

6. Delete feature branch
   ```bash
   git branch -d feat/feature-name
   git push origin --delete feat/feature-name
   ```

## Code Standards

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Class | PascalCase | `ProductService` |
| Method | camelCase | `getProductById()` |
| Variable | camelCase | `productList` |
| Constant | UPPER_SNAKE_CASE | `MAX_PRODUCT_NAME_LENGTH` |
| Package | lowercase.dot.separated | `com.ws101.service` |

### Class Structure Order

```java
public class MyClass {
    // 1. Static constants
    private static final String CONSTANT = "value";
    
    // 2. Instance variables
    private String name;
    
    // 3. Constructors
    public MyClass() {}
    
    // 4. Public methods
    public void publicMethod() {}
    
    // 5. Protected methods
    protected void protectedMethod() {}
    
    // 6. Private methods
    private void privateMethod() {}
}
```

### Javadoc Format

```java
/**
 * Brief one-line description.
 * 
 * More detailed explanation of what this method does,
 * its responsibilities, and any important behavior.
 * 
 * @param paramName description of parameter including constraints
 * @return description of return value
 * @throws ExceptionType description of when exception is thrown
 * 
 * @see RelatedClass#relatedMethod()
 */
public ReturnType methodName(ParamType paramName) {
    // implementation
}
```

### Avoid Bad Practices

**Don't do this:**
```java
// Magic numbers
if (price > 10000) { ... }

// Unclear names
List<Product> l = getAllProducts();
Product p = l.get(0);

// Missing comments for complex logic
Collections.sort(products, (a, b) -> 
    a.getPrice().compareTo(b.getPrice()) * 
    (sortDescending ? -1 : 1));
```

**Do this:**
```java
// Named constants
private static final double PREMIUM_THRESHOLD = 10000.0;
if (price > PREMIUM_THRESHOLD) { ... }

// Descriptive names
List<Product> allProducts = getAllProducts();
Product firstProduct = allProducts.get(0);

// Explain complex logic
// Sort products by price, descending if requested
Collections.sort(products, (a, b) -> 
    a.getPrice().compareTo(b.getPrice()) * 
    (sortDescending ? -1 : 1));
```

## Testing Requirements

All code must be tested before committing:

- [ ] Feature works as intended
- [ ] Error cases handled properly
- [ ] Input validation working
- [ ] No regressions in existing functionality
- [ ] Both partners verified the functionality

### Testing Checklist for Each Endpoint

```
GET /api/v1/products
  ✓ Returns all products
  ✓ Status code 200 OK
  ✓ Response contains expected fields

POST /api/v1/products
  ✓ Creates new product
  ✓ Returns 201 Created
  ✓ ID auto-generated
  ✓ Validates required fields
  ✓ Rejects invalid data (Status 400)

PUT /api/v1/products/{id}
  ✓ Updates complete product
  ✓ Returns 200 OK
  ✓ Validates all fields
  ✓ Returns 404 for non-existent ID

PATCH /api/v1/products/{id}
  ✓ Updates only specified fields
  ✓ Other fields unchanged
  ✓ Returns 200 OK
  ✓ Validates provided fields

DELETE /api/v1/products/{id}
  ✓ Removes product
  ✓ Returns 204 No Content
  ✓ Returns 404 for non-existent ID

GET /api/v1/products/filter
  ✓ Filters by category
  ✓ Filters by name
  ✓ Filters by price range
  ✓ Returns 400 for invalid parameters
```

## Merge Conflicts Resolution

If conflicts occur:

1. **Communicate**: Discuss with your partner
2. **Understand**: Review both versions carefully
3. **Resolve**: Choose the correct code (usually not just "ours" or "theirs")
4. **Test**: Verify the merge works correctly
5. **Commit**: Use meaningful commit message

```bash
git status  # See conflicting files
# Edit files to resolve conflicts
git add <resolved-file>
git commit -m "fix: resolved merge conflicts in ProductService"
```

## Documentation Requirements

Every public component must have documentation:

### For Classes
- Purpose and responsibility
- Relationship to other classes
- Usage examples if complex

### For Methods
- What it does
- Parameters and constraints
- Return values
- Exceptions thrown
- Examples for non-obvious usage

### For Complex Logic
- Explain the "why" not the "what"
- Link to requirements or external resources
- Note any gotchas or assumptions

## Pull Request Checklist

Before merging to `main`:

- [ ] Both partners reviewed the code
- [ ] All tests pass
- [ ] No compiler warnings
- [ ] Follows code standards
- [ ] Javadoc is complete
- [ ] Commit messages are clear
- [ ] No merge conflicts
- [ ] Feature branch is up to date with main
- [ ] No debug code or comments left behind

## Git Workflow Example

```bash
# 1. Ensure you're on main and up to date
git checkout main
git pull origin main

# 2. Create feature branch
git checkout -b feat/product-filtering

# 3. Make changes and commit (with role rotation)
git add src/main/java/com/ws101/service/ProductService.java
git commit -m "feat: implemented price range filtering"

# 4. Continue development with role switch
git add src/main/java/com/ws101/controller/ProductController.java
git commit -m "feat: added filter endpoint

Co-authored-by: Partner Name <email@example.com>"

# 5. Push to remote
git push origin feat/product-filtering

# 6. Review together
git log --oneline feat/product-filtering~3..feat/product-filtering
git diff main...feat/product-filtering

# 7. Merge to main
git checkout main
git merge feat/product-filtering
git push origin main

# 8. Cleanup
git branch -d feat/product-filtering
git push origin --delete feat/product-filtering
```

## Common Mistakes to Avoid

1. **Committing large changes without messages** 
   → Always write clear commit messages

2. **One person driving all the time**
   → Rotate roles to ensure both partners understand

3. **Not syncing before merging**
   → Always `git pull` before merging

4. **Merging without review**
   → Review together before merging to main

5. **Not testing before committing**
   → Test every feature before commit

6. **Pushing directly to main**
   → Always use feature branches

7. **Unclear commit messages**
   → Use "Action + Result" format

8. **Forgetting to update documentation**
   → Document as you code

## Questions or Issues?

If you have questions about contribution guidelines:
1. Check this document first
2. Discuss with your pair programming partner
3. Reference the DEVELOPMENT.md file
4. Check the README.md for related information

---

**Remember**: Good pair programming requires communication, clear code, and shared understanding. Take your time to explain and understand each implementation.
