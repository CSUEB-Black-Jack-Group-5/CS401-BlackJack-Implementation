# How do we use Git?
## Terminology
| Terminology Used   | Description                                                                        |
|--------------------|------------------------------------------------------------------------------------|
| Pull Request (PR)  | - A request to submit your changes to the staging branch                           |
| Local Staging Area | - Part of git that knows what the files your next commit will include              |
| Commit             | - A marker denoting the state of the repo at that point                            |
| Push               | - Actually submits the commits that you've made to the remote repository on github |
| Roll Back          | - Making use of commits to go back in time                                         |
| Branch             | - An alternate timeline from the main staging branch used for feature development  |
| Checkout           | - Checking out a branch is a way of viewing the code of that branch locally        |

## What is Git
Git is a program that tracks your project files and maintains a history of all the versions of that file.<br>
However, git requires you to tell it where a new version is in time.<br>

## Good practices
### Branches
1. We should be disciplined in working withing feature branches. If you have a feature you want to work on, *please* first<br>
   make sure you make a new branch for that feature
2. As you are programming, the more small commits you have the better. If you are programming without frequent commits, <br>
   this means you have less places to roll back to if you accidentally messed up part of the code
3. Once you feel like your feature is complete enough to share with the rest of the group you can push the code to github<br>
   and submit a _Pull Request_ notifying the rest of the group that you have something new that should be looked at for potential<br>
   merging into `staging`

### Pull Requests
#### What are they?
- Pull requests are a mechanism of review to ensure quality and accuracy of contributed code before the code ends up in the main staging branch.

#### Why are we using them
- In this project we are using pull requests as a way to ensure we all have some contributions to many parts of the codebase
- It also ensures that we all agree on a particular piece of code before allowing it to end up in staging

#### How do I make a pull request
- When you are in your checked out branch `i.e message-login-feature`, and you `push` to the repo Github's website will give<br>
  you an opportunity to make a pull request. It will be a green button that says ***Compare and Pull Request***
- Clicking this button will bring you to a place that lets you elaborate on what was changed in your pull request <br>
  (this is for us to see at a high level what was changed without looking at the changes directly)
- After submitting your pull request we will look at it and make sure it looks ok, and if any questions arise they can be expressed via<br>
  comments in the PR thread.
  - In this phase of making a PR if you want to look at the code in your editor (IntelliJ) you can `checkout` the branch that submitted the PR directly and look at it before deciding to agree on merging the PR

## Common Git Commands
1. git checkout \<branchname\>
   1. This command moves you into a particular branch to work in or view
   2. This can be used to view Pull Requests before merging them
2. git add \<file\>
   1. This command adds the file(s) supplied to a local staging area
   2. This local staging area contains all the changes that you wish to apply on the next `commit`
3. git commit -m "message here"
   1. This command takes the staged files and bundles them into a single commit
   2. The commit is now a point in history that can be tracked
   3. You can have many commits in your local repo before pushing
4. git push
   1. This command takes all the commits made previously and pushes them to the branch you have checked out