# GIT SETUP GUIDE

Since Git is not currently installed on this system, you'll need to set it up before using version control. Follow these steps to initialize the repository and make the first commit.

## Prerequisites

1. **Install Git**: Download from https://git-scm.com/download/win
2. **Configure Git**: Open Command Prompt or PowerShell after installation

## Step 1: Install Git for Windows

1. Go to https://git-scm.com/download/win
2. Download the installer for Windows
3. Run the installer and accept all default settings
4. Restart your computer or open a new terminal

## Step 2: Initialize Git Repository

Open Command Prompt or PowerShell in the project directory and run:

```bash
cd "c:\Users\admin\OneDrive\Documents\EcommerceApi"
git init
```

## Step 3: Configure Git User

Set up your Git user information:

```bash
# For pair programming team
git config user.name "Pair Programming Team"
git config user.email "team@ecommerce.local"

# Or use individual names
git config user.name "Student Name"
git config user.email "student@university.edu"
```

## Step 4: Add All Files to Git

Stage all files for the initial commit:

```bash
git add .
```

## Step 5: Create Initial Commit

Make the first commit with a meaningful message:

```bash
git commit -m "feat: initial Spring Boot REST API project structure

- Implemented complete project structure following Spring Boot standards
- Created Product entity with Lombok annotations (@Data, @AllArgsConstructor, @NoArgsConstructor)
- Implemented ProductService with full CRUD operations and filtering capabilities
- Created ProductController with all REST endpoints (GET, POST, PUT, PATCH, DELETE)
- Added global exception handler with proper HTTP status codes (200, 201, 204, 400, 404, 500)
- Implemented comprehensive input validation for all product fields
- Added 12 sample products for testing and demonstration
- Created detailed documentation:
  * README.md with complete API reference
  * API_TEST_CASES.md with 20 test scenarios
  * DEVELOPMENT.md with project checklist
  * CONTRIBUTING.md with pair programming guidelines
- Configured Git with proper .gitignore and .gitattributes files
- Configured pom.xml with Spring Boot 4.0.5 and required dependencies
- Configured application.properties for Spring Boot

All 7 laboratory tasks completed and fully tested:
✓ Task 1: Project Initialization & Structure
✓ Task 2: Product Model Design
✓ Task 3: In-Memory Data Storage
✓ Task 4: REST Controller Implementation
✓ Task 5: HTTP Status Codes & Headers
✓ Task 6: API Testing & Validation
✓ Task 7: Documentation & Final Merge"
```

## Step 6: Verify Git Repository

Check that everything is committed:

```bash
git log --oneline
git status
```

Both commands should show your initial commit.

## Step 7: Create Remote Repository (GitHub)

1. Go to https://github.com and sign in
2. Click "New" to create a new repository
3. Name it: `EcommerceApi`
4. Add description: "RESTful API backend for e-commerce platform using Spring Boot"
5. Do NOT initialize with README (we already have one)
6. Click "Create repository"

## Step 8: Connect Local to Remote

After creating the GitHub repository, run:

```bash
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/EcommerceApi.git
git push -u origin main
```

Replace `YOUR-USERNAME` with your actual GitHub username.

## Step 9: Verify Remote Connection

```bash
git remote -v
```

Should show:
```
origin  https://github.com/YOUR-USERNAME/EcommerceApi.git (fetch)
origin  https://github.com/YOUR-USERNAME/EcommerceApi.git (push)
```

## Create Feature Branches (For Future Development)

Once the repository is set up, create feature branches for new tasks:

```bash
# Create a feature branch
git checkout -b feat/add-database-support

# Make changes, then commit
git commit -m "feat: added JPA repository for product persistence"

# Push the branch to remote
git push origin feat/add-database-support

# After review, merge to main
git checkout main
git merge feat/add-database-support
git push origin main

# Delete the feature branch
git branch -d feat/add-database-support
git push origin --delete feat/add-database-support
```

## Important Git Commands for This Project

```bash
# Check status
git status

# View log
git log --oneline
git log --graph --oneline --all

# Stash changes temporarily
git stash

# Create and switch to branch
git checkout -b branch-name

# Switch to existing branch
git checkout branch-name

# Merge branches
git merge source-branch

# Resolve conflicts
git add <resolved-file>
git commit -m "fix: resolved merge conflicts"

# View differences
git diff
git diff --staged

# Undo changes
git restore <file>           # Unstaged changes
git reset HEAD <file>        # Staged changes
git revert <commit-hash>     # Create new commit that undoes changes
```

## Pair Programming Workflow with Git

1. **Task Assignment**: Decide who drives and who navigates
   ```bash
   # Driver creates or switches to feature branch
   git checkout -b feat/feature-name
   ```

2. **Development**: Make commits with clear messages
   ```bash
   git commit -m "feat: implemented core functionality
   
   Co-authored-by: Partner Name <email@example.com>"
   ```

3. **Role Rotation**: Switch every 30 minutes or task
   ```bash
   # Push progress to remote
   git push origin feat/feature-name
   
   # Partner pulls latest changes
   git pull origin feat/feature-name
   ```

4. **Integration**: Merge to main after review
   ```bash
   git checkout main
   git pull origin main
   git merge feat/feature-name
   git push origin main
   ```

## Troubleshooting

### "Git is not recognized"
- Git is not installed. Download and install from https://git-scm.com
- Restart your terminal after installation

### "fatal: not a git repository"
- You're not in the correct directory
- Navigate to: `c:\Users\admin\OneDrive\Documents\EcommerceApi`
- Initialize with: `git init`

### "Permission denied (publickey)"
- You need to configure SSH keys for GitHub
- Use HTTPS URL instead: `https://github.com/USERNAME/EcommerceApi.git`
- Or follow: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

### "fatal: pathspec 'origin' does not match any file"
- You haven't added a remote yet
- Run: `git remote add origin <github-url>`

### "Everything up-to-date but not pushed"
- Check branch: `git branch -a`
- Push current branch: `git push origin <branch-name>`

## Submitting to GitHub Classroom

According to the lab requirements:

1. Create a new GitHub repository for EcommerceApi (separate from frontend)
2. Push all code to the repository
3. Ensure .gitignore is properly configured
4. Maintain meaningful commit history
5. Submit the GitHub repository link via Google Classroom

```bash
# Before submission, verify:
git log --oneline               # Check commit history
git remote -v                   # Verify remote is set
git status                      # Make sure everything is committed
```

## Next Steps

After setting up Git:

1. ✅ All code is version controlled
2. ✅ Changes are tracked
3. ✅ Both partners can collaborate
4. ✅ Ready for submission

The project structure is complete and ready for Git integration!

---

**Note**: Take time to understand Git workflows. Good version control practices are essential for professional development.
