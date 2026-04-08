---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of several parts such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, `MeetingListPanel`, and `StatusBarFooter`.

All these components, including the `MainWindow`, inherit from the abstract `UiPart` class, which captures the common functionality shared by GUI components.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder.
For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component:

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* switches between different views (`PersonListPanel` and `MeetingListPanel`) based on user interaction.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on the `Model` component, as some `UI` component (like `MeetingItem`) needs to find the `Person` referred by a `ParticipantID`.

The `CommandHistory` component:

* keeps track of commands that the user have entered in the `CommandHistory`.
* allows the user to shift through their previously entered commands via `prevCommand()` and `nextCommand()`.
* Usage:
  1. Add commands into the history using `add()`. Internally, this also resets the index to point at the end of the list.
  2. Go to the previous command using `prevCommand()`. Note that the current draft needs to be passed in. This is because the `CommandHistory` keeps track of the user draft (the text that the user is currently editing). Internally, this method simply shifts the index to the previous command.
  3. Go to the next command using `nextCommand()`. The current draft needs to be passed in as well. Internally, this method simply shifts the index to the next command and returns the command at that position OR the draft if `index == list.size()`.
* Example usage:
  ```
  CommandHistory ch = new CommandHistory();
  
  // Assume the user has entered these two commands.
  ch.add("first command");
  ch.add("second command");
  
  // userDraft is the text in the CommandBox.
  String userDraft = "draft command";
  
  userDraft = ch.prevCommand(userDraft); // userDraft becomes "second command".
  userDraft = ch.prevCommand(userDraft); // userDraft becomes "first command".
  userDraft = ch.nextCommand(userDraft); // userDraft becomes "second command".
  userDraft = ch.nextCommand(userDraft); // userDraft becomes "draft command".
  ```
* `CommandHistory` sequence diagram:
  <img src="images/CommandHistorySequenceDiagram.png" width="300" />

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`]([ModelClassDiagram.puml](diagrams/ModelClassDiagram.puml)https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" /> <img src="images/CloserModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object)
and all `Meeting` objects (which are contained in a `UniqueMeetingList` object).

* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.

* stores the currently 'selected' `Meeting` objects as a separate _filtered_ list which is exposed as an unmodifiable `ObservableList<Meeting>`that can be 'observed'.

* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.

* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

WIP: Fill in information here
--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of contacts
* frequently arranges meetings with people they know
* need to log interactions with contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
* **Productivity:** Enable users to easily manage their numerous relations in a fast, distraction-free CLI.
* **Organization:** Neatly organizes information like contacts, meetings, and interaction notes all in one place, with efficient filtering and retrieval.
* **Simplicity:** A lightweight app that avoids slow, feature-heavy GUIs. Runs directly in the terminal with minimal setup.
* **Privacy:** Fully local application. No risk of data leakage or delays from network issues.

**Not in our scope**:
* InternLink does not send emails or messages to the contacts
* InternLink cannot automatically sync with LinkedIn or other platforms
* Internlink cannot manage the internship application


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a        | I want to                                                   | So that I can                                             |
|----------|-------------|-------------------------------------------------------------|-----------------------------------------------------------|
| `* * *`  | general user| add a new person to the contact list                        | store new contacts                                        |
| `* * *`  | general user| delete a person from the contact list                       | remove entries that I no longer need                      |
| `* * *`  | general user| list out all people in the contact list                     | see my saved contacts                                     |
| `* * *`  | general user| view a person’s profile in the contact list with full details| access comprehensive details when needed                 |
| `* * *`  | general user| edit information of people in the contact list              | keep my records accurate                                  |
| `* * *`  | general user| add a meeting with people in the contact list               | keep track of scheduled interactions                      |
| `* * *`  | general user| delete meetings with people in the contact list             | remove outdated or cancelled meetings                     |
| `* * *`  | general user| find people in the contact list by one or multiple tags     | locate people in specific categories                      |
| `* *`    | general user| find a person in the contact list by name                   | locate details without going through the entire list      |
| `* *`    | general user| find people in the contact list by phone or email           | locate contacts even without names                        |
| `* *`    | new user    | see usage instructions                                      | refer to instructions when I forget how to use the app    |
| `* *`    | general user| add tags to people in the contact list en masse             | categorise contacts efficiently                           |
| `* *`    | general user| delete tags from people in the contact list en masse        | keep tags up to date efficiently                          |
| `* *`    | general user| edit a tag for people in the contact list en masse          | maintain accurate categorisation efficiently              |
| `* *`    | general user| star or favourite people in the contact list                | access important contacts quickly                         |
| `* *`    | general user| unstar or remove favourite from people in the contact list  | keep priorities updated                                   |
| `* *`    | general user| edit meetings with people in the contact list               | keep meeting details up to date                           |
| `* *`    | general user| find meetings by date or description                        | locate specific meetings easily                           |
| `* *`    | general user| delete people in the contact list en masse                  | remove multiple entries efficiently                       |
| `* *`    | general user| see overdue follow-ups for people in the contact list       | remember to follow up                                     |
| `*`      | general user| sort people in the contact list lexicographically           | locate contacts more easily                               |
| `*`      | general user| sort meetings by date                                       | view meetings in chronological order                      |
| `*`      | general user| go to my last command quickly                               | fix mistakes faster                                       |

## Use Cases

For all use cases below, the **System** is **Internlink** and the **Actor** is the **user**

**Use case: UC1 - Add a contact / meeting**

**MSS**

1. User requests to add a new contact / meeting by providing details.
2. InternLink checks whether the contact / meeting already exists.
3. InternLink saves the contact.
4. InternLink updates the contact list.

Use case ends.

**Extensions**

* 2a. The contact / meeting already exists.
    * 2a1. InternLink notifies the user of the duplicate person / meeting error.

      Use case resumes at step 1.


**Use case: UC2 - Delete contact(s) / meeting(s)**

**MSS**

1. User requests to delete one or more contact(s) / meeting(s) by index.
2. InternLink identifies the specified contact(s) / meeting(s) in the current displayed list.
3. InternLink deletes the specified contact(s) / meeting(s).
4. InternLink updates the contact list.

Use case ends.

**Extensions**

* 2a. One or more of the specified indices are invalid contact(s) / meeting(s).
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.


**Use case: UC3 - List contacts / meetings**

**MSS**

1. User requests to list contacts / meetings.
2. InternLink retrieves all contacts / meetings.
3. InternLink displays the contact / meeting list.

Use case ends.

**Extensions**

* 3a. There are no contacts / meetings stored.
    * 3a1. InternLink displays an empty list.

      Use case ends.


**Use case: UC4 - Edit contact details**

**MSS**

1. User requests to edit a contact by index, providing the updated contact details.
2. InternLink identifies the specified contact in the current displayed list.
3. InternLink checks whether the updated contact would duplicate an existing contact.
4. InternLink updates the contact.

Use case ends.

**Extensions**

* 2a. The specified index is invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

* 3a. The updated contact would duplicate an existing contact (same name, email and phone).
    * 3a1. InternLink notifies the user of the duplicate person error.

      Use case resumes at step 1.

**Use case: UC5 - Edit a meeting**

**MSS**

1. User requests to edit a meeting by index, providing the updated meeting details.
2. InternLink identifies the specified meeting in the current displayed meeting list.
3. Internlink identifies the specified contacts in the current displayed contact list.
4. InternLink checks whether the updated meeting would duplicate an existing meeting.
5. InternLink updates the meeting.

Use case ends.

**Extensions**

* 2a. The specified meeting index is invalid.
    * 2a1. InternLink notifies the user of the invalid meeting index error.

      Use case resumes at step 1.

* 3a. One or more specified participant indices are invalid.
    * 3a1. InternLink notifies the user of the invalid contact index error.

      Use case resumes at step 1.

* 3b. One or more specified contacts to remove are not participants in the meeting.
    * 3b1. The contacts were specified as added in the same command (e.g., `add/1 del/1`).
        * InternLink proceeds without those contacts.

          Use case resumes at step 4.

    * 3b2. The contacts were not added in the same command.
        * InternLink notifies the user of the error.

          Use case resumes at step 1.

* 4a. The updated meeting would duplicate an existing meeting (same description and date).
    * 4a1. InternLink notifies the user of the duplicate meeting error.

      Use case resumes at step 1.


**Use case: UC6 - Find contacts**

**MSS**

1. User requests to find contacts and provides search input.
2. InternLink evaluates the current displayed contact list for contacts that have the input as substrings.
3. InternLink filters the displayed contact list to include only matching contacts.
4. InternLink displays the filtered list of contacts.

Use case ends.

**Extensions:**

* 1a. User provides prefix-based input (e.g. `n/NAME`, `e/EMAIL`).
    * 1a1. InternLink restricts the search to the specified fields based on the provided prefixes.

      Use case resumes at step 2.

* 1b. User provides keyword-based input (without prefixes).
    * 1b1. InternLink treats the entire input as a single substring that is searched in contacts' name, phone, and email.

      Use case resumes at step 2.

* 4a. No contacts in the displayed contact list match the search criteria.
    * 4a1. InternLink displays an empty contact list.

      Use case ends.

**Use case: UC7 - Find meetings**

**MSS**

1. User requests to find meetings and provides search inputs (e.g. description, date, or participant indices).
2. InternLink evaluates the current meeting list based on the provided inputs:
    - If participant indices are provided, meetings containing all specified participants are considered matches.
    - If description or date prefixes are provided (e.g. `d/`, `dt/`), meetings whose fields contain the specified substrings are considered matches.
3. InternLink filters the meeting list to include meetings that match any of the specified prefixes.
4. InternLink displays the filtered list of meetings.

Use case ends.

**Extensions:**

* 2a. One or more specified participant indices are invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

* 3a. A meeting does not satisfy any of the specified prefix conditions.
    * 3a1. InternLink excludes the meeting from the filtered meeting list.

      Use case resumes at step 4.

* 4a. No meetings in the displayed meeting list match the specified criteria.
    * 4a1. InternLink displays an empty meeting list.

      Use case ends.

**Use case: UC8 - Assign tags to contacts**

**MSS**

1. User requests to assign one or more tags to one or more contacts by index.
2. InternLink identifies the specified contact(s).
3. InternLink adds the specified tag(s) to the selected contact(s).
4. InternLink updates the contact list.

Use case ends.

**Extensions**

* 2a. One or more specified indices are invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

* 3a. One or more specified tags already exist on the selected contacts.
    * 3a1. InternLink ignores the duplicate tags and adds only new tags, if any.
    * 3a2. InternLink reports the assignment of all specified tags.

      Use case resumes at step 4.

**Use case: UC9 - Find contacts by tag**

**MSS**

1. User requests to find contacts using one or more tags.
2. InternLink checks the current displayed contact list for contacts containing at least one of the specified tags.
3. InternLink filters the contact list to show matching contacts.
4. InternLink displays the matching contacts.

Use case ends.

**Extensions**

* 2a. None of the specified tags exist in the displayed contact list.
    * 2a1. InternLink notifies the user that no matching tags were found.

      Use case resumes at step 1.

* 2a. Some specified tags are invalid.
    * 2a1. InternLink ignores the invalid tags and proceeds searching for valid ones.
    * 2a2: Internlink reports all results match at least one of the given tags.

      Use case resumes at step 3.

**Use case: UC10 - Delete tags from contacts**

**MSS**

1. User requests to delete one or more tags from one or more contacts by index.
2. InternLink identifies the specified contact(s).
3. InternLink removes the specified tag(s) from the selected contact(s).
4. InternLink updates the contact list.

Use case ends.

**Extensions**

* 2a. One or more specified indices are invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

* 3a. None of the specified tags exist on any of the selected contacts.
    * 3a1. InternLink notifies the user that the tags are not found.

      Use case resumes at step 1.

* 3b. Some specified tags do not exist on the selected contacts.
    * 3b1. InternLink ignores tags that are not present on the selected contacts and removes only applicable tags, if any.
    * 3b2. InternLink reports the attempted deletion of all specified tags.

      Use case resumes at step 4.

**Use case: UC11 - Star or unstar a contact**

**MSS**

1. User requests to star or unstar a contact by contact indexes.
2. InternLink identifies the specified contacts.
3. InternLink updates the contact’s starred status (starred or unstarred).
4. InternLink updates the contact list.

Use case ends.

**Extensions**

* 2a. The specified index is invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.
    * 
* 2a. One or more specified contacts are already starred / unstarred.
    * 2a1. All specified contacts are already in the target state (starred / unstarred).
        * InternLink notifies the user that all selected contacts are already starred / unstarred.

          Use case resumes at step 1.

    * 2a2. Only some specified contacts are already in the target state.
        * InternLink ignores those contacts and updates only the valid ones.

          Use case resumes at step 3.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Search and filtering operations (including multi-tag filtering) should complete within 1 second for up to 100 contacts.
5. The system should load the application within 2 seconds with 1000 contacts.
6. Each contact should be able to support up to 20 tags.
7. The application should automatically save changes after every successful command.
8. The application should prevent data corruption even if the program closes unexpectedly.
9. The application should try to recover all non-corrupted lines in the event of a data corruption.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS.

* **Contact**: A person stored in the contact list, representing an individual such as a friend, colleague, or acquaintance.

* **Participant**: A contact associated with a meeting. Participant cards can be seen under a meeting, representing the individuals involved in that meeting.

* **Tag**: A label assigned to a contact to categorize or organize them. Examples: `friends`, `cs`, `school`, `project`. A contact may have multiple tags.

* **Meeting**: An event scheduled in the application, which may involve zero or more contacts. Each meeting contains details such as a description and a date.

* **Contact List**: The collection of all contacts currently stored in the application.

* **Displayed Contact List**: The current filtered view of contacts shown to the user (e.g., after using `find` or `findtag`).

* **Meeting List**: The collection of all meetings stored in the application.

* **Displayed Meeting List**: The current filtered view of meetings shown to the user (e.g., after using `findmeeting`).

* **Index**: A number assigned to each item in a displayed list (contacts or meetings), used to reference that item in commands.

* **Prefix**: A marker used in commands to indicate a specific field (e.g., `n/`, `p/`, `e/`, `t/`, `d/`, `dt/`).

--------------------------------------------------------------------------------------------------------------------
## Appendix: Effort

### Overview

This project required a moderate to high level of effort. Compared to AB3, which manages only a single entity type (Person), adding an additional entity type of Meetings
required us to design and integrate a new set of features while ensuring compatibility with the existing system.

### Extending the Architecture

One major challenge was implementing support for multiple entity types within a structure originally designed for only one. This included:
- Designing and implementing a new Meeting entity
- Extending the model, logic, and storage components to support meetings
- Creating a separate UI view for meetings and enabling seamless switching between Contacts and Meetings

### Entity Relationships

Another key challenge was creating references between entities, specifically linking contacts to meetings. Meetings can involve multiple contacts, which required us to:
- Store and manage references to existing Person objects within Meeting objects, and ensure it remains consistent when contacts are edited or deleted
- Adapt the storage layer so that these relationships can be saved and reconstructed accurately from JSON

### Understanding and Adapting AB3

A further difficulty was understanding and adapting the internal architecture of AB3. Much of the effort involved reverse-engineering how entities are handled across layers (Model, Logic, Storage), especially:
- How data is parsed and validated
- How commands interact with the model
- How data is persisted in JSON format

We had to replicate and adapt these mechanisms for the new Meeting entity, including modifying the JSON storage structure (`InternlinkData.json`) to support multiple entity types while maintaining data integrity.

### Conclusion

Overall, while AB3 provided a strong foundation, the effort required to extend it into a multi-entity system with additional UI functionality was considerable. As a team of four, we worked collaboratively to design, implement, and refine the application to the best of our abilities.

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts and meetings. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

---

### Getting started

1. Launch the application as seen above.


2. Use the help command: `help`
   Expected: A help message is displayed that provides a link to the user guide.

---

### Adding and managing contacts

1. Add a new contact with minimal required fields: `add n/Alice Tan p/91234567`
   Expected: A new contact named Alice Tan is added with the phone number shown.


2. Try adding a contact without a name: `add p/91234567`
   Expected: Error message indicating invalid command format.


3. Add another contact with tags: `add n/Bob Lee e/bob@example.com t/friend t/cs`
   Expected: Contact is added with email and tags.


4. Edit an existing contact: `edit 1 p/98765432 e/alice@new.com`
   Expected: Contact 1’s phone and email are updated.


5. Try editing without specifying any fields: `edit 1`
   Expected: Error message indicating that at least one field must be provided.

---

### Working with tags and favourites

1. Add tags to multiple contacts: `addtag 1, 2 / friends / cs`
   Expected: Tags are added to both contacts.


2. Attempt to use an invalid index: `addtag 0 / friends`
   Expected: Error message indicating invalid index.


3. Rename a tag: `edittag 1, 2 o/cs n/computer science`
   Expected: Tag is updated for the specified contacts.


4. Try editing a tag without specifying the old tag: `edittag 1, 2 n/computer science`
   Expected: Error message due to missing old tag.


5. Remove a tag: `deletetag 1 / friends`
   Expected: Tag is removed from contact 1.


6. Try an incorrectly formatted delete tag command: `deletetag / friends 1`
   Expected: Error message indicating invalid format.


7. Mark a contact as starred: `star 2`
   Expected: Contact 2 is marked as starred.


8. Try starring with an invalid index: `star 0`
   Expected: Error message indicating invalid index.


9. Remove starred marking: `unstar 2`
   Expected: Contact 2 is no longer marked as starred.

---

### Searching and filtering contacts

1. List all contacts: `list`
   Expected: Full contact list is displayed.


2. Search for contacts globally: `find Alice`
   Expected: Contacts matching "Alice" are shown.


3. Try searching without a keyword: `find`
   Expected: Error message indicating missing search term.


4. Search using specific fields: `find n/Alice p/9876`
   Expected: Contacts matching the name or phone are shown.


5. Try mixing global and field search: `find Alice n/Bob`
   Expected: Error message due to invalid command format.


6. Search by tags: `findtag / friends`
   Expected: Contacts with the tag are displayed.


7. Try searching without specifying tags: `findtag`
   Expected: Error message indicating missing tags.

---

### Managing meetings

1. Click the tab to switch from the Contacts view to the Meetings view.


2. Create a meeting with contacts: `addmeeting 1, 2 d/Project meeting dt/2026-05-26`
   Expected: Meeting is added with the given details.


3. Try using an invalid date format: `addmeeting d/Project meeting dt/26-05-2026`
   Expected: Error message indicating invalid date format.


4. Edit the meeting: `editmeeting 1 d/Updated meeting dt/2026-06-01`
   Expected: Meeting details are updated.


5. Try editing with an invalid date: `editmeeting 1 dt/01-06-2026`
   Expected: Error message indicating invalid date format.


6. List all meetings: `listmeeting`
   Expected: All meetings are displayed.


7. Search for meetings: `findmeeting d/project`
   Expected: Matching meetings are shown.


8. Try searching with an invalid date: `findmeeting dt/01-06-2026`
   Expected: Error message indicating invalid date format.


9. Delete a meeting: `deletemeeting 1`
   Expected: Meeting at index 1 is deleted.


10. Try deleting with an invalid index: `deletemeeting 999`
    Expected: Error message indicating invalid index.

---

### Cleaning up

1. Delete a contact: `delete 1`
   Expected: Contact at index 1 is removed.


2. Try deleting with an invalid index: `delete 999`
   Expected: Error message indicating invalid index.


3. Clear all data: `clear`
   Expected: All contacts and meetings are removed.


4. Exit the application: `exit`
   Expected: Application closes successfully.

---

### Saving data

#### Missing file test

1. Close the application.


2. Delete `data/InternlinkData.json`.


3. Re-launch the application.

Expected: The application starts with sample data and logs that the file is missing.

#### Corrupted file test

1. Close the application.


2. Open `data/InternlinkData.json` and introduce invalid JSON (e.g. remove a closing bracket).


3. Re-launch the application.

Expected: The application detects the corrupted file, clears all data, and starts with an empty dataset,
logging that the datafile cannot be read.
