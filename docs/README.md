# Walter User Guide

Walter is a task management chatbot that helps you handle your schedule with precision. Whether you are adding todos, deadlines, or events, Walter ensures there are "no loose ends."

## Features

### Notes about the command format:

*   Words in `UPPER_CASE` are the parameters to be supplied by the user.
    *   e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter which can be used as `todo Cook breakfast`.
*   Parameters must be in the specified order.
*   Extraneous parameters for commands that do not take in parameters (such as `list`, `bye`) will be ignored.

---

### Adding a todo task: `todo`

Adds a task with no specific deadline.

Format: `todo DESCRIPTION`

Example: `todo buy groceries`

### Adding a deadline task: `deadline`

Adds a task that needs to be done by a specific date/time.

Format: `deadline DESCRIPTION /by DATE_TIME`

*   `DATE_TIME` can be in strict format `d/M/yyyy HHmm` (e.g., `2/12/2019 1800`) or natural language (e.g., `today`, `tomorrow`, `mon`).
*   If time is omitted in natural language, it defaults to `2359`.

Example: `deadline submit report /by tomorrow 1800`

### Adding an event task: `event`

Adds a task that starts and ends at specific times.

Format: `event DESCRIPTION /from START_TIME /to END_TIME`

Example: `event project meeting /from Mon 1400 /to Mon 1600`

### Listing all tasks: `list`

Shows a list of all tasks currently in your schedule.

Format: `list`

### Marking a task as done: `mark`

Marks the specified task as completed.

Format: `mark INDEX`

*   Marks the task at the specified `INDEX`.
*   The index refers to the index number shown in the displayed task list.
*   The index must be a positive integer 1, 2, 3, ...

Example: `mark 1`

### Unmarking a task as not done: `unmark`

Marks the specified task as not completed yet.

Format: `unmark INDEX`

*   Unmarks the task at the specified `INDEX`.
*   The index refers to the index number shown in the displayed task list.
*   The index must be a positive integer 1, 2, 3, ...

Example: `unmark 1`

### Deleting a task: `delete`

Removes the specified task from the list.

Format: `delete INDEX`

*   Deletes the task at the specified `INDEX`.
*   The index refers to the index number shown in the displayed task list.
*   The index must be a positive integer 1, 2, 3, ...

Example: `delete 2`

### Finding tasks by keyword: `find`

Finds tasks whose descriptions contain the given keyword.

Format: `find KEYWORD`

Example: `find book`

### Exiting the program: `bye`

Exits the program.

Format: `bye`

---

## Data Archiving

Walter's data is saved automatically in the hard disk at `data/walter.txt` after any command that changes the data. There is no need to save manually.

---

## FAQ

**Q**: How do I transfer my data to another computer?
**A**: Install the app on the other computer and overwrite the `data/walter.txt` file it creates with the file that contains the data from your previous installation.

---

## Command Summary

| Action | Format | Examples |
|--------|--------|----------|
| **Todo** | `todo DESCRIPTION` | `todo buy milk` |
| **Deadline** | `deadline DESCRIPTION /by DATE_TIME` | `deadline return book /by 2/12/2019 1800` |
| **Event** | `event DESCRIPTION /from START_TIME /to END_TIME` | `event meeting /from today 1400 /to today 1500` |
| **List** | `list` | `list` |
| **Mark** | `mark INDEX` | `mark 1` |
| **Unmark** | `unmark INDEX` | `unmark 1` |
| **Delete** | `delete INDEX` | `delete 2` |
| **Find** | `find KEYWORD` | `find laundry` |
| **Bye** | `bye` | `bye` |