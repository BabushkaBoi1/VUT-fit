# ITS Project 1

- **Author:** Vojtěch Hájek (xhajek51)
- **Date:** 2022-04-17

## Artifact coverage matrix

| Artifact Description                      | 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10  | 11  | 12  | 13  | 14  | 15  | 16  | 17  | 18  | 19  | 20  | 21  |
|-------------------------------------------|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|
| Web page home.                            |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |
| Web page for adding.                      | x   | x   | x   |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |     |     |     |
| Web page for list view.                   | x   |     | x   |     |     |     |     |     |     | x   |     |     |     |     |     |     |     | x   | x   | x   | x   |
| Web page for editing.                     |     |     |     | x   | x   |     |     |     |     |     | x   |     | x   |     |     |     |     |     |     |     |     |
| Web page method.                          |     | x   | x   | x   | x   | x   | x   | x   | x   |     |     |     |     |     |     |     |     | x   | x   | x   | x   |
| Web page tool.                            | x   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Web page use case.                        |     |     |     |     |     |     |     |     |     | x   | x   |     |     | x   |     | x   |     |     |     |     |     |
| Web page test case.                       |     |     |     |     |     |     |     |     |     |     |     | x   |     |     | x   |     | x   |     |     |     |     |
| Web page requirement.                     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Web page Evaulation scenario.             |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |
| Action for rename.                        |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |
| Action for delete.                        |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     | x   |     |     |     |     |
| Action for clicking on button.            | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   | x   |     |     |     |     |
| Action for clicking on folder.            |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |
| Action for clicking on page.              |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |
| Action for adding relation.               |     |     | x   |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |
| Action for deleting relation.             |     |     |     |     | x   |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |     |     |
| Action for editing.                       |     |     |     | x   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Action for filling fields.                | x   |     |     |     |     | x   |     |     |     | x   |     | x   |     | x   | x   | x   |     |     |     |     |     |
| Action for finding in search bar.         |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |
| Action for adding to contest.             |     |     |     |     |     |     |     |     |     |     |     | x   |     | x   | x   |     |     |     |     |     |     |
| Set state private.                        |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |     |     |     | x   |     |
| Set state published.                      |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |
| Checking error.                           |     | x   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Checking relation to tool.                |     |     | x   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Checking relation to requirement.         |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |     |
| Checking relation to Evaluation Scenario. |     |     |     |     |     |     |     |     |     | x   | x   |     |     |     |     |     |     |     |     |     |     |
| Checking contest to test case.            |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |     |
| Checking contest to requirement.          |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   |     |     |     |     |     |     |
| Checking info                             |     |     |     | x   |     |     | x   | x   |     |     |     |     |     |     |     |     |     |     |     |     |     |
| Checking visibility.                      |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   | x   |



## Matrix Feature-Test

| Featured file           | 1   | 2   | 3   | 4   | 5   | 6   | 7   | 8   | 9   | 10  | 11  | 12  | 13  | 14  | 15  | 16  | 17  | 18  | 19  | 20  | 21  | 
|-------------------------|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|-----|
| use_case.feature        |     |     |     |     |     |     |     |     |     | x   | x   | x   | x   | x   | x   | x   | x   |     |     |     |     |
| method_tool.feature     | x   | x   | x   | x   | x   | x   | x   | x   | x   |     |     |     |     |     |     |     |     |     |     |     |     |
| unlogged_user.feature   |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     |     | x   | x   | x   | x   |

