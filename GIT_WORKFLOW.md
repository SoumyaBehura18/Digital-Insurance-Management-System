
# Git Workflow and Branching Guidelines

This document describes the Git workflow for our project to ensure a clean, organized, and controlled development process.

---

## 1. Branch Overview

| Branch Name         | Purpose |
|--------------------|---------|
| `main`             | Production-ready code. Only **owner/admin** can push directly. |
| `integration`      | Latest consolidated development branch. All feature branches are merged here first for testing/review. |
| `personal-branch`  | Each developer’s own working branch for implementing features or fixes. |

---

## 2. General Rules

1. **Direct commits to `main` are prohibited**  
   - Only the owner/admin can push to `main`.
   - Developers must create personal branches for their work.

2. **Personal branches workflow**  
   - Create a branch from `integration-branch`:
     ```bash
     git checkout integration-branch
     git switch -c my-personal-branch
     ```
   - Make commits to your personal branch only.
   - Regularly pull from `integration-branch` to keep your branch updated:
     ```bash
     git pull origin integration-branch
     ```

3. **Merging into `integration-branch` branch**  
   - After finishing work on a personal branch:
     1. Push your branch to the remote:
        ```bash
        git push origin my-personal-branch
        ```
     2. Create a **Pull Request (PR)** to merge your branch into `integration-branch`.
     3. Resolve all conflicts, sync with `integration-branch`, and only then merge.

4. **Owner/Admin review**  
   - Owner reviews changes in `integration-branch`.
   - After approval, owner merges `integration-branch` into `main`.

5. **Updating integration branch**  
   - Always pull latest changes from `integration-branch` before merging:
     ```bash
     git checkout integration-branch
     git pull origin integration-branch
     ```
   - Resolve conflicts locally if needed, then push:
     ```bash
     git push origin integration-branch
     ```

6. **Conflict resolution policy**  
   - Conflicts must be resolved **before merging into `integration-branch`**.
   - After resolving conflicts, commit the changes and push.
   - Ensure your branch is synced with the latest `integration-branch` before PR.

---

## 3. Recommended Commands Summary

```bash
# Switch to integration branch
git checkout integration-branch
git pull origin integration-branch

# Create new personal branch from integration
git switch -c my-personal-branch

# Work on feature, add and commit changes
git add .
git commit -m "Feature description"

# Push personal branch
git push origin my-personal-branch

# Create Pull Request to integration branch
# Resolve conflicts and merge

# Owner merges integration to main
git checkout main
git merge integration-branch
git push origin main

 
