–-
layout: page
title: Internlink User Guide
---
# Internlink User Guide
Hello fellow students, welcome to our Internlink User Guide!
### Table of Contents
<!-- TOC -->
* [Introduction](#introduction)
* [What is Internlink?](#what-is-internlink)
* [Who this guide is for](#who-this-guide-is-for)
* [Using this Guide](#using-this-guide)
* [Getting Started](#getting-started)
- [#1 Getting the correct Java version](#1-getting-the-correct-java-version)
    - [Checking your Java version](#checking-your-java-version)
- [#2 Downloading Internlink](#2-downloading-internlink)
- [#3 Running Internlink](#3-running-internlink)
* [User Interface](#user-interface)
* [Features](#features-)
* [Guide to Command Format](#guide-to-command-format)
* [Notes about Command Format](#notes-about-command-format)
* [Features - General features](#features---general-features)
    - [Viewing help (`help`)](#viewing-help--help)
    - [Clearing all entries (`clear`)](#clearing-all-entries--clear)
    - [Exiting the program (`exit`)](#exiting-the-program--exit)
* [Features - Managing contact information](#features---managing-contact-information)
    - [Adding a contact (`add`)](#adding-a-contact--add)
    - [Deleting a contact (`delete`)](#deleting-a-contact--delete)
    - [Editing a contact (`edit`)](#editing-a-contact--edit)
    * [Mass-tagging features](#mass-tagging-features)
        - [Adding tags to one or more contacts (`addtag`)](#adding-tags-to-one-or-more-contacts--addtag)
        - [Deleting tags from one or more contacts (`deletetag`)](#deleting-tags-from-one-or-more-contacts--deletetag)
        - [Editing existing tags (`edittag`)](#editing-existing-tags--edittag)
    * [⭐ Using stars (favourites)](#-using-stars-favourites)
        - [Starring contacts (`star`)](#starring-contacts--star)
        - [Unstarring contacts (`unstar`)](#unstarring-contacts--unstar)
* [Features - Filtering the contact list](#features---filtering-the-contact-list)
    - [Listing all contacts (`list`)](#listing-all-contacts--list)
    * [Finding contact information](#finding-contact-information)
        - [Locating contacts globally (global `find`)](#locating-contacts-globally-global-find)
        - [Locating contacts by specific fields (field `find`)](#locating-contacts-by-specific-fields-field-find)
        - [Finding contacts by tags (`findtag`)](#finding-contacts-by-tags--findtag)
* [Features - Managing meeting information](#features---managing-meeting-information)
    - [Adding a meeting (`addmeeting`)](#adding-a-meeting--addmeeting)
    - [Deleting a meeting (`deletemeeting`)](#deleting-a-meeting--deletemeeting)
    - [Editing a meeting (`editmeeting`)](#editing-a-meeting--editmeeting)
* [Features - Filtering the meeting list](#features---filtering-the-meeting-list)
    - [Listing all meetings (`listmeeting`)](#listing-all-meetings--listmeeting)
    * [Finding meeting information](#finding-meeting-information)
        - [Finding a meeting (`findmeeting`)](#finding-a-meeting--findmeeting)
* [Features - Managing data](#features--managing-data)
    - [Saving the data](#saving-the-data)
    - [Editing the data file](#editing-the-data-file)
* [FAQ](#faq)
* [Known issues](#known-issues)
* [Command summary](#command-summary)
* [Glossary](#glossary-)
<!-- TOC -->
---

<div style="page-break-after: always;"></div>
------------------------------------------------------------------------------
## Introduction
Welcome to Internlink! This guide will help you get started.

## What is Internlink?
Internlink is a **contact management app** built with students in mind. If you are an ambitious student who has a wide network of relations, this is the perfect tool for you.

Internlink helps you to:
* Store and organize contact information and label them conveniently with tags
* Keep track of your numerous connections with people such as classmates, seniors, mentors, and industry contacts
* Track interactions and open up avenues for future academic and career aspects
* Manage upcoming meetings so you don’t miss any important opportunities

With Internlink, you can manage your network of personal and business relations in school with ease, and focus on striving to reach the top.

[Back to Table of Contents](#table-of-contents)

### Who this guide is for
This guide is written for students who have at least some experience with using a **Command Line Interface (CLI)** and are seeking networking opportunities during their time in school. Our goal is to get you quickly set up with the necessary requirements so you can breeze through the hassle and start using Internlink as soon as possible.

[Back to Table of Contents](#table-of-contents)

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

[Back to Table of Contents](#table-of-contents)

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
**If Java is not installed or the version is below 17:**
> Download and install Java 17 by following the guides below:
> * [for Windows users](https://se-education.org/guides/tutorials/javaInstallationWindows.html)
> * [for Mac users](https://se-education.org/guides/tutorials/javaInstallationMac.html)
> * [for Linux users](https://se-education.org/guides/tutorials/javaInstallationLinux.html)


After installation, restart your terminal and verify the version again by repeating step 2.<br>
If installed correctly, when you [check your Java version again](#checking-your-java-version), you should see a Java version starting with `17` (e.g., `17.0.5`).

### 2. Downloading Internlink
Download the latest `Internlink.jar` file from [here](https://github.com/AY2526S2-CS2103T-T12-3/tp/releases).

### 3. Running Internlink
1. Create a new folder on your computer where you want to store the app and its data, and place `Internlink.jar` into this new folder.
2. Open a command terminal  (search for “PowerShell” or “Terminal” on your computer).
3. Navigate to the folder that you created (using `cd FILE_PATH`).
> 💡 **Tip:** If you have difficulty navigating in the terminal, type “cd ”, then drag the folder into the terminal window. Pressing Enter will automatically navigate to that folder (this works on most systems)!
4. Type `java -jar Internlink.jar` in the terminal and press Enter.
5. An application window should appear in a few seconds similar to the one below. Note how the app already contains some sample data.
   ![Ui](images/Ui.png)

Congratulations! You are now ready to use Internlink. Refer to the [Features](#features-) below for details of each command.


Alternatively, to get started, you can try out some of the suggested commands here.


* `list` : Lists all contacts.


* `add n/John Doe p/98765432 e/johnd@example.com` : Adds a contact named John Doe to the contact list, with phone number `98765432` and email `johnd@example.com`.


* `delete 1` : Deletes the 1st contact shown in the displayed contact list.


* `clear` : Deletes all contacts.


* `exit` : Exits the app.




[Back to Table of Contents](#table-of-contents)


--------------------------------------------------------------------------------------------------------------------

## User Interface

[Back to Table of Contents](#table-of-contents)

## Features

### Guide to Command Format
The command format consists of three main parts:

| Component    | Example       | Description                                     |
|--------------|---------------|-------------------------------------------------|
| Command Word | `add`         | Specifies the action to perform                 |
| Prefix       | `n/`          | Indicates which field is being set (e.g., name) |
| Parameter    | `George Best` | The value provided by the user                  |

For example, `add n/George Best` is a valid command formed using these three components.

[Back to Table of Contents](#table-of-contents)

### Notes about Command Format
> ❗ **Note:** If you get confused about the terms being used at any point, feel free to refer back to the [Guide to Command Format](#guide-to-command-format) section above to get a better understanding.

* Words in `UPPER_CASE` represent parameters.<br>
  e.g. `add n/NAME`, `NAME` should be replaced with the desired value. `add n/John Doe` adds a contact named `John Doe`.

* Prefixes/parameters in square brackets are optional.<br>
  e.g. `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* If a command has certain prefixes/parameters in round brackets, you must provide at minimum one of them for the command to succeed.<br>
  e.g. for `add`, (p/PHONE_NUMBER) (e/EMAIL) means that at least a phone number OR an email must be provided.

* Text followed by `…` can be used zero or more times (or **one or more times if the parameter is not optional**).
  e.g. `[t/TAG]…` can be ignored entirely, or used as `t/friend`, `t/friend t/family`, etc.

* Parameters **that come with prefixes** can be inputted in any order.<br>
  e.g. `n/NAME p/PHONE_NUMBER` and `p/PHONE_NUMBER n/NAME` are the same.<br> However, in the case of `deletetag`, the positions of indices and tags cannot be swapped.

* Commands without parameters (e.g. `help`, `list`, `exit`, `clear`) ignore any additional text after the command word.
  e.g. `help 123` is treated as `help`.

* In all commands that can take in multiple indices, indices are to be separated by commas, except for after the final index.
  e.g. for `addtag`, if you want to add a tag to contacts 1, 2 and 3, `addtag 1, 2, 3 / friends` is a valid command.

* If you are using a PDF version of this document, be careful when copying and pasting multi-line commands, as spaces surrounding line-breaks may be left out when copying over to the application.

> ❗ **Note:** In the feature sections below, **"displayed contact list"** refers to the contact list in its current state (whether full or filtered via commands like `find` and `findtag`), while **"entire contact list"** always refers to the full unfiltered list.

[Back to Table of Contents](#table-of-contents)

## Features - General features

### Viewing help : `help`

**Format:**
```
help
```

**Description:** You can use this command to display a message explaining how to access the help page.

![help message](images/helpcommand.png)

[Back to Table of Contents](#table-of-contents)

### Clearing all entries : `clear`

**Format:**
```
clear
```

**Description:** You can use this command to clear all entries from Internlink's data (both contacts and meetings).

![clear message](images/clearcommand.png)


> ⚠️ **Warning:** This action is irreversible. All entries will be permanently deleted, and there is no way to undo this command.

[Back to Table of Contents](#table-of-contents)

### Exiting the program : `exit`

**Format:**
```
exit
```

**Description:** You can use this command to exit Internlink.

> 💡 **Tip:** Worried about losing your data? Don’t worry — all changes are automatically saved after every edit.

[Back to Table of Contents](#table-of-contents)

## Features - Managing contact information

> ❗ **Note:** Internlink does not allow duplicate contacts. A contact is considered a duplicate only if the *name, phone number, and email* all match an existing entry.
> If you try to create a duplicate contact, the following error message will be shown in the command result box:
> `This person already exists in the address book`.

> ❗ **Note:** If a command requires an INDEX, but is given one that does not correspond to an existing contact’s index in the list, the following error message will be shown in the command result box:
> `The person index provided is invalid`.

### Adding a contact : `add`

**Format:**
```
add n/NAME (p/PHONE_NUMBER) (e/EMAIL) [t/TAG]...
```

**Description:** You can use this command to add a new contact to your contact list. The details of the given contact can also be seen in the command result box once they have been added.

An added contact is automatically be sorted into your list by alphabetical order.

![add message](images/addcommand.png)

> 💡 **Tip:** In a rush? Just provide a *name* and *one other contact detail* (phone number/email) to add the contact. You can fill in the rest later with the [`edit` command](#editing-a-contact--edit)

> 💡 **Tip:** Confused about the difference between `( )` and `[ ]` in the command? Refer to the [Notes about command format](#notes-about-command-format) section for a detailed explanation.

**Examples:**
- `add n/John Doe e/johndoe@example.com` adds a new contact with name `John Doe` and email `johndoe@example.com`.
- `add n/Betsy Crowe p/12345678 e/betsycrowe@example.com t/friend t/criminal` adds a new contact with name `Betsy Crowe`, phone number `12345678`, email `betsycrowe@example.com` and tags `friend` and `criminal`.

> 💡 **Tip:** Added a key contact and want to quickly spot them in your list? Use the [`star` command](#starring-a-contact--star) to mark them as favourites and bring them to the top.

[Back to Table of Contents](#table-of-contents)

### Deleting a contact : `delete`
> 💡 **Tip:** The examples below for `delete` use the [`find`](#locating-contacts-globally-global-find) and [`list`](#listing-all-contacts--list) commands as well. Click on each of the two command in the previous sentence to be brought to their respective section!

**Format:**
```
delete INDEX [, INDEX]...
```

**Description:** You can use this command to delete the contact at the specified `INDEX` number(s) from the displayed contact list. The details of the deleted contact(s) can also be seen in the command result box once completed.

![delete message](images/deletecommand.png)

**Examples:**
* `list` followed by `delete 2, 3` deletes the 2nd and 3rd contacts in the displayed contact list.
* `find Betsy` followed by `delete 1` deletes the 1st contact in the result list of the `find` command.

[Back to Table of Contents](#table-of-contents)

### Editing a contact : `edit`

**Format:**
```
edit INDEX (n/NAME) (p/PHONE) (e/EMAIL) (t/TAG)…
```

**Description:** You can use this command to edit the contact at the specified `INDEX` in the displayed contact list with all the new information given. The details of the edited contact can also be seen in the command result box once completed.

![edit message](images/editcommand.png)

* At least one of the optional fields must be provided.
* When editing tags, adding of tags is not cumulative. You must input all existing tags of the contact if you want to keep them after editing.

> 💡 **Tip:** Need to remove all tags from a contact at once? Use `t/` without specifying any tags to do so.

> 💡 **Tip:** Confused about the difference between `( )` and `[ ]` in the command? Refer to the [Notes about command format](#notes-about-command-format) section for a detailed explanation.

**Examples:**
*  `edit 1 p/91234567 e/johndoe@example.com` edits the phone number and email of the 1st contact to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` edits the name of the 2nd contact to be `Betsy Crower` and clears all existing tags.

[Back to Table of Contents](#table-of-contents)

### Mass-tagging features
> 💡 **Tip:** Recently joined a group with people who are already in your contact list? Perhaps consider using these next few commands to make updating your contacts easier.

Internlink introduces 3 new tag-related functions that all allow operation on one or more contacts at once.

### Adding tags to one or more contacts : `addtag`

**Format:**
```
addtag INDEX [,INDEX]... / TAG [/ TAG]...
```

**Description:** You can use this command to add the specified `TAG`s to the contacts at the specified `INDEX` numbers in the displayed contact list. It supports multi-index and multi-tag input, letting you add multiple tags to multiple people in a single command.

![addtag message](images/addtagcommand.png)

**Examples:**
* `addtag 5 / classmates` adds the `classmates` tag to contact index 5.
* `addtag 1, 2, 3 / friends / cs` adds the `friends` and `cs` tags to contact indices 1, 2 and 3.

[Back to Table of Contents](#table-of-contents)

### Deleting tags from one or more contacts : `deletetag`

**Format:**
```
deletetag INDEX [,INDEX]...  / TAG [/ TAG]...
```

**Description:** You can use this command to delete the specified `TAG`s from the contacts at the specified `INDEX` numbers in the displayed contact list.  It supports multi-index and multi-tag input, letting you delete multiple tags from multiple people in a single command.

![deletetag message](images/deletetagcommand.png)

* At least one of the `TAG`s must be already tagged on one of the specified contacts, otherwise the command will fail. Invalid tags will be ignored.

**Examples:**
* `deletetag 5 / classmates` deletes the `classmates` tag from contact index 5.
* `deletetag 1, 2, 3 / friends / cs` deletes the `friends` and `cs` tags from contact indices 1, 2 and 3.

[Back to Table of Contents](#table-of-contents)

### Editing existing tags : `edittag`

**Format:**
```
edittag (INDICES OR 'all') o/ OLDTAG n/ NEWTAG
```

**Description:** You can use this command to edit the specified existing/old tag for the specified contacts at the specified `INDEX` numbers in the displayed contact list to the given new tag, or all contacts in the displayed list in the case of `all`.

![edittag message indices](images/edittagcommandindices.png)
![edittag message global](images/edittagcommandglobal.png)

* Using the `all` keyword instead of specific indices will do a global edit (in the displayed contact list) of the given `OLDTAG`, while inputting specific indices only edits them for the given contacts.
* As long as one of the specified contacts has the given `OLDTAG`, the command will be recognized as valid.
* `edittag` operates on the displayed contact list, not the entire contact list.

**Examples:**
* `edittag 1, 2, 3 o/ cs n/ computer science` edits the tag `cs` for contacts 1, 2 and 3, and changes it to `computer science`.
* `edittag all o/ cs n/ computer science` edits the tag `cs` for all contacts in the displayed contact list, and changes it to `computer science`.

[Back to Table of Contents](#table-of-contents)


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

### Adding a meeting : `addmeeting`

Add meetings to one or multiple contacts at once

Format: `addmeeting INDICES d/DESCRIPTION dt/DATE`

* Dates must be in the YYYY-MM-DD format.

Example: `addmeeting 1, 2 d/ Casual icebreaker dt/ 2026-03-26`

### Deleting a meeting : `deletemeeting`

Remove meetings using indices

Format: `deletemeeting INDICES`

* Regarding index order: Meetings are sorted by date by default to give chronological overview.

Example: `deletemeeting 1`

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
| **Add**         | `add n/NAME [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com t/friend t/colleague`  |
| **Delete**      | `delete INDEX`<br> e.g., `delete 3`                                                                                                 |
| **Edit**        | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                      |
| **List**        | `list`                                                                                                                              |
| **Global Find** | `find <SEARCH SUBSTRING> [<OTHER SEARCH SUBSTRINGS>]...`<br> e.g., `find alex david`                                                |
| **Field Find**  | `find [n/NAME] [p/PHONE] [e/EMAIL]...`<br> e.g., `find n/ david p/ 9927 e/ charlotte`                                               |
| **Add tags**    | `addtag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g., `addtag 1, 2 / friends / cs`                                                   |
| **Delete tags** | `deletetag INDEX, [INDICES...] / TAG [/ TAG]`<br> e.g., `deletetag 1, 2 / friends / cs`                                             |
| **Edit tags**   | `edittag [INDICES OR 'all'] o/ OLDTAG n/ NEWTAG`<br> e.g., `edittag 1, 2, 3 o/ cs n/ computer science`                              |
| **Filter tags** | `filtertag / TAG [/ TAG]...`<br> e.g., `filtertag / schoolB / schoolC`                                                              |
| **Star**        | `star INDEX`<br> e.g., `star 2`                                                                                                     |
| **Unstar**      | `unstar INDEX`<br> e.g., `unstar 2`                                                                                                 |
| **Add meetings**| `addmeeting INDICES d/DESCRIPTION dt/DATE`<br> e.g., `addmeeting 1, 2 d/ Casual icebreaker dt/ 2026-03-26`                          |
| **Delete meetings**| `deletemeeting INDICES`<br> e.g., `deletemeeting 1`                                                                              |
| **Clear**       | `clear`                                                                                                                             |
| **Exit**        | `exit`                                                                                                                              |
