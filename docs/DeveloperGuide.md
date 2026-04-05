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
* depends on some classes in the `Model` component, as it displays `Person` and `Meeting` objects residing in the `Model`.

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

</div>


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

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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

**Value proposition**: manage contacts and log interactions with contacts faster than a typical mouse/GUI driven app

**Not in our scope**:
* InternLink does not send emails or messages to the contacts
* InternLink cannot automatically sync with LinkedIn or other platforms
* Internlink cannot manage the internship application


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | new user                                   | see usage instructions         | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | add a new person               |                                                                        |
| `* * *`  | user                                       | delete a person                | remove entries that I no longer need                                   |
| `* * *`  | user                                       | find a person by name          | locate details of persons without having to go through the entire list |
| `* * *`  | user                                       | list all the people            | browse my full directory                                               |
| `* * *`  | user                                       | edit a contact information easily | keep my records accurate                                            |
| `* * *`  | user                                       | add a new tag to one or more people | categorise contacts in different ways                             |
| `* * *`  | user                                       | delete a new tag to one or more people | keep tags up to date                                           |
| `* * *`  | user                                       | edit a new tag to one or more people | keep tags up to date                                             |
| `* * *`  | user                                       | filter contacts by one or multiple tags | find contacts that fall under specific categories             |
| `* *`    | user                                       | hide private contact details   | minimize chance of someone else seeing them by accident                |
| `* *`    | user                                       | star/favourite contacts I access regularly | find high-priority connections quickly                     |
| `* *`    | student who cannot remember exact details  | search using flexible keyword matching | still find the correct contact even with without remembering fully|
| `* *`    | student is waiting for a recruiter's reply | mark a contact for follow-up by a specific date | remember to check in                                  |
| `* *`    | student who tends to procrastinate follow-ups | see overdue follow-up contacts | remember to check in with my contact                                |
| `* *`    | user                                       | schedule meetings with a contact | remember to meet with my contact                                     |
| `* *`    | user                                       | edit meeting with a contact   | keep meeting details up to date                                         |
| `* *`    | user                                       | mark meetings as completed   | keep meeting details up to date                                          |
| `* *`    | user                                       | delete meetings with a contact   | keep meeting details up to date                                      |
| `* *`    | student who forgets details of conversations | add interaction notes with a timestamp   | recall what was discussed previously                       |
| `* *`    | student                                    | delete interaction notes       | remove this interaction from the log                                   |
| `* *`    | student                                    | edit interaction notes         | update the interaction log with correct information                    |
| `* *`    | student preparing for a follow-up email    | review past interaction log    | reference earlier conversations accurately                           |
| `* *`    | user                                       | delete contacts en masse       | remove irrelevant contacts quickly                                     |
| `* *`    | user who meets people regularly            | sort based on the last met date| see how long it has been since they contacted this person              |
| `* *`    | student managing many applications         | see a list of contacts that have not had interaction in the past X days   | maintain active communication |
| `*`      | user with many persons in the address book | sort contacts lexicographically | locate a person easily                                                 |
| `*`      | user who makes many typos                  | go to my last command quickly  | quickly edit the command                                               |
| `*`      | user                                       | undo multiple commands         | easily reverse my actions                                              |
| `*`      | user                                       | see the history of changes made in that session  | know what changes I made                             |

*{More to be added}*

**Use case: UC1 - Add a contact / meeting**

**MSS**

1. User requests to add a new contact / meeting by providing details.
2. InternLink checks whether the contact / meeting already exists.
3. InternLink saves the contact.
4. InternLink updates the contact list.

   Use case ends.

**Extensions**

* 2a. The contact / meeting already exists.
    * 2a1. InternLink notifies the user of the duplicate contact / meeting error.

      Use case resumes at step 1.


**Use case: UC2 - Delete contact(s) / meeting(s)**

**MSS**

1. User requests to delete one or more contact(s) / meeting(s) by index.
2. InternLink identifies the specified contact(s) / meeting(s) in the current displayed list.
3. InternLink deletes the specified contact(s) / meeting(s).
4. InternLink updates the contact list.

   Use case ends.

**Extensions**

* 3a. One or more of the specified indices are invalid contact(s) / meeting(s).
    * 3a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.


**Use case: UC3 - List contacts / meetings**

**MSS**

1. User requests to list contacts.
2. InternLink retrieves all contacts.
3. InternLink displays the contact list.

   Use case ends.

**Extensions**

* 2a. There are no contacts /meetings stored.
    * 2a1. InternLink displays an empty list.

      Use case ends.


**Use case: UC4 - Edit contact details**

**MSS**

1. User requests to edit a contact by index, providing the updated contact details.
2. InternLink identifies the specified contact in the current displayed list.
3. InternLink checks whether the updated contact would duplicate an existing contact.
4. InternLink updates the contact.
5. InternLink updates the contact list.

   Use case ends.

**Extensions**

* 2a. The specified index is invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

* 4a. The updated contact would duplicate an existing contact.
    * 4a1. InternLink notifies the user of the duplicate contact error.

      Use case resumes at step 1.

**Use case: UC5 - Edit a meeting**

**MSS**

1. User requests to edit a meeting by index, providing the updated meeting details.
2. InternLink identifies the specified meeting.
3. InternLink checks whether the updated meeting would duplicate an existing meeting.
4. InternLink updates the meeting.
5. InternLink updates the meeting list.

   Use case ends.

**Extensions**

* 2a. The specified meeting index is invalid.
    * 2a1. InternLink notifies the user of the invalid meeting index error.

      Use case resumes at step 1.

* 3a. One or more specified participant indices are invalid.
    * 3a1. InternLink notifies the user of the invalid contact index error.

      Use case resumes at step 1.

* 3b. At least one specified contacts to remove is not a participant in the meeting.
    * 3b1. InternLink notifies the user of the error.

      Use case resumes at step 1.

* 4a. The updated meeting would duplicate an existing meeting.
    * 4a1. InternLink notifies the user of the duplicate meeting error.

      Use case resumes at step 1.

    
Use case: UC5 - Find contacts

**MSS**

1. User requests to find contacts and provides search predicates as input.
2. InternLink checks the current displayed contact list for contacts that match.
3. InternLink filters the current displayed contact list based on those that match.
4. InternLink displays the matching contacts.

Use case ends.

Extensions:

* 1a. User provides prefix-based input (e.g. `n/NAME`, `e/EMAIL`).
    * 1a1. InternLink filters contacts based on the specified fields.
    
      Use case resumes from step 5.

* 1b. User provides a keyword-based input (without prefixes).
    * 1b1. InternLink searches for contacts that match the keyword in all of its fields.
      
      Use case resumes from step 5.

* 4a. No contacts match the search criteria.
    * 4a1. InternLink displays an empty filtered contact list.
    
      Use case ends.

Use case: UC6 - Find meetings

**MSS**

1. User requests to find meetings and provides search input (e.g. description, date, or participant indices).
2. InternLink checks the current displayed meeting list for meetings that match the specified input.
3. If participant indices are provided, InternLink filters the meeting list to show only meetings that contain **all** specified participants.
4. InternLink further filters the meeting list to satisfy any other specified criteria, such as description or date.
5. InternLink displays the matching meetings.

Use case ends.

Extensions:

* 2a. One or more specified participant indices are invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case ends.

* 3a. A meeting contains only some, but not all, of the specified participants.
    * 3a1. InternLink does not include that meeting in the filtered meeting list.

      Use case resumes at step 4.

* 5a. No meetings match the specified criteria.
    * 5a1. InternLink displays an empty filtered meeting list.

      Use case ends.

**Use case: UC7 - Assign tags to contacts**

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

**Use case: UC8 - Find contacts by tag**

**MSS**

1. User requests to find contacts using one or more tags.
2. InternLink checks the current displayed contact list for contacts containing at least one of the specified tags.
3. InternLink filters the contact list to show matching contacts.
4. InternLink displays the matching contacts.

   Use case ends.

**Extensions**

* 2a. None of the specified tags exist in the displayed contact list.
    * 2a1. InternLink notifies the user that no matching tags were found.

      Use case ends.

* 3a. Some specified tags are invalid.
    * 3a1. InternLink ignores the invalid tags and proceeds with valid ones.

      Use case resumes at step 3.

**Use case: UC9 - Delete tags from contacts**

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

      Use case ends.

* 3b. Some specified tags do not exist on the selected contacts.
    * 3b1. InternLink ignores the invalid tags and removes only valid ones.

      Use case resumes at step 3.

**Use case: UC10 - Star or unstar a contact**

**MSS**

1. User requests to star or unstar a contact by index.
2. InternLink identifies the specified contact.
3. InternLink updates the contact’s starred status (starred or unstarred).
4. InternLink updates the contact list.

   Use case ends.

**Extensions**

* 2a. The specified index is invalid.
    * 2a1. InternLink notifies the user of the invalid index error.

      Use case resumes at step 1.

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
10. All contacts and interaction logs should be persistently stored between sessions.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS.
* **Contact**: A person stored in the addressbook, typically a recruiter, company representative, or professional connection whom the student met during networking or career events.
* **Tag**: A label assigned to a contact to categorize or organize them. Examples: ```Google```, ```SWE```, ```CareerFair2026```, ```Referral```. A contact may contain multiple tags.
* **Multi-tagging**: The ability to assign multiple tags to a single contact so that the contact can be categorized under multiple attributes such as company, role, or networking event.
* **Interaction Log**: A timestamped record of communication or interaction between the student and a contact. Examples include: interview, email, phone call, referral, career fair conversation.
* **Interaction Notes**: A single record inside the interaction log, containing: interaction type, timestamp, optional notes.
* **Meeting**: An upcoming event / meeting with a contact.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
