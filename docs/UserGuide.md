---
layout: page
title: User Guide
---

AddressBook Level 3 (AB3) is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, AB3 can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.

    * `add n/John Doe p/98765432 e/johnd@example.com` : Adds a contact named John Doe to the Address Book,
      with phone number `98765432` and email `johnd@example.com`.

    * `delete 3` : Deletes the 3rd contact shown in the current list.

    * `clear` : Deletes all contacts.

    * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a new person to the address book.

**Requirements:**  
A person must have a `NAME` and at least one of the following:
- `PHONE_NUMBER`
- `EMAIL`

**Format:** `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]...`

> 💡 **Tip:** A person can have **zero or more tags**.

**Examples:**
- `add n/John Doe e/johnd@example.com`
- `add n/Betsy Crowe p/1234567 e/betsycrowe@example.com t/friend t/criminal`


### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.


### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
  specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.


### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`


### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)


### Adding tags to one or more people : `addtag`
Add one or more tags to one or more people in the address book

Format: `addtag INDEX, [INDICES...] / TAG [/ TAG]`

* Adds the specified `TAG`(s) to the specified `INDEX`(s).
* The indices refers to the index numbers shown in the displayed person list (1-indexed).
* The indices **must all be positive integers** (1, 2, 3, ...), and be referring to a valid index in the address book.
* All indices must be separated from each other by a comma.

Examples:
* `addtag 5 / classmates` adds the `classmates` tag to contact index 5.
* `addtag 1, 2, 3 / friends / cs` adds the `friends` and `cs` tags to contact indices 1, 2 and 3.


### Deleting tags from one or more people : `deletetag`
Deletes one or more tags from one or more people in the address book

Format: `deletetag INDEX, [INDICES...] / TAG [/ TAG]`

* Deletes the specified `TAG`(s) from the specified `INDEX`(s).
* The indices refers to the index numbers shown in the displayed person list (1-indexed).
* The indices **must all be positive integers** (1, 2, 3, ...), and be referring to a valid index in the address book.
* All indices must be separated from each other by a comma.
* At least one of the `TAG`s must be already tagged on one of the specified contacts, otherwise the command will fail.

Examples:
* `deletetag 5 / classmates` deletes the `classmates` tag from contact index 5.
* `deletetag 1, 2, 3 / friends / cs` deletes the `friends` and `cs` tags from contact indices 1, 2 and 3.


### Starring a person : `star`
Stars / Favourites a person in the address book

Format: `star INDEX`

* Stars the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* This command is functionally the same as `addtag INDEX, [INDICES...] / STAR`,
  where addtag can star more people simultaneously

Examples:
* `list` followed by `star 2` stars the 2nd person in the address book.
* `find Betsy` followed by `star 1` stars the 1st person in the results of the `find` command.


### Unstarring a person : `unstar`
Unstars / Unfavourites a person in the address book

Format: `unstar INDEX`

* Unstars the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​
* This command is functionally the same as `deletetag INDEX, [INDICES...] / STAR`,
  where deletetag can unstar more people simultaneously

Examples:
* `list` followed by `unstar 2` unstars the 2nd person in the address book.
* `find Betsy` followed by `unstar 1` unstars the 1st person in the results of the `find` command.


### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action          | Format, Examples                                                                                                                    |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------|
| **Help**        | `help`                                                                                                                              |
| **Add**         | `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com t/friend t/colleague` |
| **Delete**      | `delete INDEX`<br> e.g., `delete 3`                                                                                                 |
| **Edit**        | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                     |
| **List**        | `list`                                                                                                                              |
| **Find**        | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                          |
| **Add tags**    | `addtag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g., `addtag 1, 2 / friends / cs`                                                   |
| **Delete tags** | `deletetag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g., `deletetag 1, 2 / friends / cs`                                             |
| **Star**        | `star INDEX`<br> e.g., `star 2`                                                                                                     |
| **Unstar**      | `unstar INDEX`<br> e.g., `unstar 2`                                                                                                 |
| **Clear**       | `clear`                                                                                                                             |
