---
layout: page
title: Internlink User Guide
---
# Internlink User Guide
Hello fellow students, welcome to our Internlink User Guide!
### Table of Contents
<!-- TOC -->
* [Internlink User Guide](#internlink-user-guide)
   * [Table of Contents](#table-of-contents)
* [Introduction](#introduction)
  * [What is Internlink?](#what-is-internlink)
  * [Who this guide is for](#who-this-guide-is-for)
* [Using this Guide](#using-this-guide)
* [Quick start](#quick-start)
* [Features](#features)
 * [Features - General features](#features-general-features)
   - [Viewing help (`help`)](#viewing-help--help)
   - [Clearing all entries (`clear`)](#clearing-all-entries--clear)
   - [Exiting the program (`exit`)](#exiting-the-program--exit)
   - [Saving the data](#saving-the-data)
   - [Editing the data file](#editing-the-data-file)
 * [Features - Managing contact information](#features---managing-contact-information)
   - [Adding a person (`add`)](#adding-a-person--add)
   - [Deleting a person (`delete`)](#deleting-a-person--delete)
   - [Editing a person (`edit`)](#editing-a-person--edit)
   - [Adding tags to one or more people (`addtag`)](#adding-tags-to-one-or-more-people--addtag)
   - [Deleting tags from one or more people (`deletetag`)](#deleting-tags-from-one-or-more-people--deletetag)
   - [Editing existing tags (`edittag`)](#editing-existing-tags--edittag)
   - [Starring a person (`star`)](#starring-a-person--star)
   - [Unstarring a person (`unstar`)](#unstarring-a-person--unstar)
 * [Features - Searching for contact information](#features---searching-for-contact-information)
   - [Listing all features (`list`)](#listing-all-persons-list)
   - [Locating persons globally (global `find`)](#locating-persons-globally--global-find)
   - [Locating persons by specific fields (field `find`)](#locating-persons-by-specific-fields--field-find)
   - [Finding contacts by tags (`findtag`)](#finding-contacts-by-tags--findtag)
 * [Features - Managing meeting information](#features---managing-meeting-information)
   - [Adding a meeting (`addmeeting`)](#adding-a-meeting--addmeeting)
   - [Deleting a meeting (`deletemeeting`)](#deleting-a-meeting--deletemeeting)
   - [Editing a meeting (`editmeeting`)](#editing-a-meeting--editmeeting)
 * [Features - Searching for meeting information](#features---searching-for-meeting-information)
   - [Finding a meeting (`findmeeting`)](#finding-a-meeting--findmeeting)
* [FAQ](#faq)
* [Known issues](#known-issues)
* [Command summary](#command-summary)
<!-- TOC -->
---

<div style="page-break-after: always;"></div>
------------------------------------------------------------------------------
## Introduction
Welcome to Internlink! This guide will help you get started.

### What is Internlink?
Internlink is a **contact management app** built with students in mind. If you are an ambitious student who has a wide network of relations, this is the perfect tool for you.

Internlink helps to:
* Store and organise contact information and label them conveniently with tags
* Keep track of your numerous connections with people such as classmates, seniors, mentors, and industry contacts
* Track interactions and open up avenues for future academic and career aspects
* Manage upcoming meetings so you don’t miss any important opportunities

With Internlink, you can manage your network of personal and business relations in school with ease, and focus on striving to reach the top.

### Who this guide is for
This guide is written for students who have at least some experience with using a Command Line Interface (CLI) and are seeking networking opportunities during their time in school. Our goal is to get you quickly set up with the necessary requirements so you can breeze through the hassle and start using Internlink as soon as possible.

--------------------------------------------------------------------------------------------------------------------
## Using this Guide

<div markdown="block" class="alert alert-info">

**For Novice users**

* You can jump to the [Getting Started](#getting-started) section to get started on Internlink.
</div>

<div markdown="block" class="alert alert-success">

**For Experienced users**

* You can jump to the [Table of Contents](#table-of-contents) to start navigating the guide.
</div>

<div markdown="block" class="alert alert-warning">

**For Advanced users**

* You can jump to the [Command Summary](#command-summary) for a quick summary of all the commands and their formats.
</div>

—---------------------------------------------------------------------------------------------------------------------
## Getting Started
### 1. Getting the correct Java version
Internlink requires **Java 17** to run. Please follow the instructions according to your computer's operating system.

### **Checking your Java version:**
1. Open a command terminal  (search for “PowerShell” or “Terminal” on your computer).
2. Execute this command using the steps below: `java -version`.
> * Type `java -version` and press Enter
> * If Java is installed, you'll see the version number (e.g., `java version "17.0.1"`)
> * The first number before the first period (`.`) should be 17 or higher
>
**If Java is not installed or the version is below 17:**<br>
Download and install Java 17 by following the guides below:
> * [for Windows users](https://se-education.org/guides/tutorials/javaInstallationWindows.html)
> * [for Mac users](https://se-education.org/guides/tutorials/javaInstallationMac.html)
> * [for Linux users](https://se-education.org/guides/tutorials/javaInstallationLinux.html)<br>

After installation, restart your terminal and verify the version again by repeating step 2.<br>
If installed correctly, when you [check your Java version again](#checking-your-java-version), you should see a Java version starting with `17` (e.g., `17.0.5`).

### 2. Downloading Internlink
Download the latest `Internlink.jar` file from [here](https://github.com/AY2526S2-CS2103T-T12-3/tp/releases).


### 3. Running Internlink
1. Create a new folder on your computer where you want to store the app and its data, and place `Internlink.jar` into this new folder.
2. Open a command terminal  (search for “PowerShell” or “Terminal” on your computer).
3. Navigate to the folder that you created. (using `cd FILE_PATH`).
> 💡 **Tip:** If you have difficulty finding the folder location in the terminal, type “cd ”, then drag the folder into the terminal window. Pressing Enter will automatically navigate to that folder! (this works on most systems)
3. Type this command in the terminal and press Enter: `java -jar Internlink.jar`
4. An application window should appear in a few seconds similar to the one below. Note how the app already contains some sample data.
   ![Ui](images/Ui.png)

Congratulations! You are now ready to use Internlink. Refer to the [Features](#features) below for details of each command.

[Back to Table of Contents](#table-of-contents)


--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

## Features - General features

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

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
 
> ⚠️ **Caution:** If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.


## Features - Managing contact information

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

### Editing existing tags : `edittag`

Rename existing tags across multiple contacts in batch

Format: `edittag [INDICES OR 'all'] o/ OLDTAG n/ NEWTAG`

* Using the `all` keyword instead of specific indices will do a global edit of the given `OLDTAG`, while inputting specific indices only edits them for the given contacts.
* As long as one of the specified contacts has the given `OLDTAG`, the command will be valid.
* `edittag` operates on the current list, not the whole address book.
* Indices are to be separated by commas.

Examples:
* `edittag 1, 2, 3 o/ cs n/ computer science` edits the tag `cs` for contacts 1, 2 and 3, and changes it to `computer science`.
* `edittag all o/ cs n/ computer science` edits the tag `cs` for all contacts in the current list, and changes it to `computer science`.

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

## Features - Searching for contact information

### Listing all persons : `list`

Shows a list of all persons currently stored in Internlink's data

Format: `list`

> 💡 **Tip:** This command is a very convenient way to clear all current filters and obtain the full contact list quickly.

### Locating persons globally: global `find`

Global find can take in multiple parameters and will output all contacts that has any of the fields that fit the parameters

Format: `find <SEARCH SUBSTRING> [<OTHER SEARCH SUBSTRINGS>]...`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe` in all fields except tags (name, email, phone)
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Locating persons by specific fields: field `find`

Field find can take in multiple parameters of the same type and only search within that field. BUT, field find cannot be used with global find concurrently.

Format: `find [n/NAME] [p/PHONE] [e/EMAIL]...`

Example: `find n/ david p/ 9927 e/ charlotte`

The above example will filter all contacts whose name contains `david` OR whose phone number contains `9927` OR whose email contains `charlotte`.

### Finding contacts by tags : `findtag`

Display only contacts with specific tags for easier management

Format: `findtag / TAG [/ TAG]...`

* `findtag` operates on the **entire address book** rather than the current filtered list.
* All contacts containing **at least one** of the given tags will be filtered.

Examples:
* `findtag / classmates` will filter all contacts that contain the `classmates` tag.
* `findtag / schoolB / schoolC` will filter all contacts that contain at least one of the `schoolB` or `schoolC` tags.

## Features - Managing meeting information

### Adding a meeting : `addmeeting`

Add meetings to one or multiple contacts at once

Format: `addmeeting INDICES d/DESCRIPTION dt/DATE`

* Dates must be in the YYYY-MM-DD format.

Example: `addmeeting 1, 2 d/ Casual icebreaker dt/ 2026-03-26`

### Deleting a meeting : `deletemeeting`

Remove meetings using indices

Format: `deletemeeting INDICES`

* Regarding index order: Meetings are sorted by date by default to give chronological overview.

Example: `deletemeeting 1` deletes the meeting with index 1 in the meeting list.

### Editing a meeting : `editmeeting`

Edit a meeting's details (description, date, contacts involved)

Format: `editmeeting <MEETING_INDEX> [d/DESCRIPTION] [dt/DATE] [add/PERSON_INDEX,...] [del/PERSON_INDEX,...]`

* Descriptions and dates are in the same format as `addmeeting`.
* The `add/` and `del/` prefixes accept multiple person indices each.
* At least one of `DESCRIPTION`, `DATE`, `PERSON_INDEX` (either `add/` or `del/`) are required.
* The person indices refer to indices from the **current filtered person list**.

Examples:
* `editmeeting 2 d/Project meeting` edits the meeting with index 2 in the meeting list, changing the description to `Project meeting`. All other details are left unchanged.
* `editmeeting 1 d/Casual icebreaker dt/2026-05-01 add/5 del/1` edits the meeting with index 1 in the meeting list, changing the description to `Casual icebreaker`, date to `2026-05-01`, adds person with index 5 to the meeting contacts and delete person with index 1 from the meeting contacts.

## Features - Searching for meeting information

### Listing all meetings : `listmeeting`

Shows a list of all meetings currently stored in Internlink's data.

Format: `listmeeting`

> 💡 **Tip:** Similar to `list`, this command is a very convenient way to clear all current filters and obtain the full meeting list quickly.

### Finding a meeting : `findmeeting`

Find meetings by searching for specific field substrings

Format: `findmeeting [d/DESCRIPTION] [dt/DATE] [i/PERSON_INDEX,...]`

* Descriptions and dates are in the same format as `addmeeting`.
* For descriptions and dates, meetings that contain the substrings are filtered (the strings do not have to exactly match the actual meeting details).
* Multiple person indices can be searched for, and only meetings including **all specified indices** will be filtered.
* All meetings satisfying **at least one** of `DESCRIPTION`, `DATE` or `PERSON_INDEX` are filtered (i.e it is an OR search).
* Search parameters are case-insensitive.
* The person indices refer to indices from the **current filtered person list**.

Examples:
* `findmeeting d/project` searches for all meetings that contain `project` in their description.
* `findmeeting d/meeting dt/2026 i/1,2,3` searches for all meetings that contain `meeting` in their description OR contain `2026` in their date OR contains all of persons 1, 2 and 3 from the current filtered person list.

</div>

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

| Action              | Format, Examples                                                                                                                                                         |
|---------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Help**            | `help`                                                                                                                                                                   |
| **Add**             | `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​` <br> e.g. `add n/James Ho p/22224444 e/jamesho@example.com t/friend t/colleague`                                       |
| **Delete**          | `delete INDEX`<br> e.g. `delete 3`                                                                                                                                       |
| **Edit**            | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`<br> e.g.`edit 2 n/James Lee e/jameslee@example.com`                                                           |
| **List persons**    | `list`                                                                                                                                                                   |
| **Global Find**     | `find <SEARCH SUBSTRING> [<OTHER SEARCH SUBSTRINGS>]...`<br> e.g. `find alex david`                                                                                      |
| **Field Find**      | `find [n/NAME] [p/PHONE] [e/EMAIL]...`<br> e.g. `find n/ david p/ 9927 e/ charlotte`                                                                                     |
| **Add tags**        | `addtag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g. `addtag 1, 2 / friends / cs`                                                                                         |
| **Delete tags**     | `deletetag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g. `deletetag 1, 2 / friends / cs`                                                                                   |
| **Edit tags**       | `edittag [INDICES OR 'all'] o/ OLDTAG n/ NEWTAG`<br> e.g. `edittag 1, 2, 3 o/ cs n/ computer science`                                                                    |
| **Find tags**       | `findtag / TAG [/ TAG]...`<br> e.g. `findtag / schoolB / schoolC`                                                                                                        |
| **Star**            | `star INDEX`<br> e.g. `star 2`                                                                                                                                           |
| **Unstar**          | `unstar INDEX`<br> e.g. `unstar 2`                                                                                                                                       |
| **Add meetings**    | `addmeeting INDICES d/DESCRIPTION dt/DATE`<br> e.g. `addmeeting 1, 2 d/ Casual icebreaker dt/ 2026-03-26`                                                                |
| **Delete meetings** | `deletemeeting INDICES`<br> e.g. `deletemeeting 1`                                                                                                                       |
| **Edit meetings**   | `editmeeting MEETING_INDEX [d/DESCRIPTION] [dt/DATE] [add/PERSON_INDEX...] [del/PERSON_INDEX...]`<br> e.g. `editmeeting 1 d/Casual icebreaker dt/2026-05-01 add/5 del/1` |
| **List meetings**   | `listmeeting`                                                                                                                                                             |
| **Find meetings**   | `findmeeting [d/DESCRIPTION] [dt/DATE] [i/PERSON_INDEX,...]`<br> e.g. `findmeeting d/meeting dt/2026 i/1,2,3`                                                            |
| **Clear**           | `clear`                                                                                                                                                                  |
| **Exit**            | `exit`                                                                                                                                                                   |
