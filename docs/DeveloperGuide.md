---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TA Toolkit Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

### Ng Yu Soon's Acknowledgements
 * Used ChatGPT to generate documentation for `ViewCommand`, `ViewCommandParser`, `AddNoteCommand`, `AddNoteCommandParser`, `Note` and `Notes` classes.
 * Used ChatGPT to generate the format and structure in `ViewCommandTest`, `ViewCommandParserTest`, `AddNoteCommandTest`, `AddNoteCommandParserTest`, `NoteTest` and `NotesTest` classes.
 * Used ChatGPT to conform to better structure within Personal Portfolio Page.

### Tejas Garrepally's Acknowledgements
 * Used GitHub Copilot to generate some of the JavaDocs for `Attendance`
 * Used GitHub Copilot to generate some of the format and structure for `WeekTest`, `ClassGroupTest`
 * Used ChatGPT to replace the color scheme in `DarkTheme.css` with Nord Theme.

### Gautham Kailash's Acknowledgements
* Used GitHub Copilot to generate Javadocs for `AddCommand`, `AddCommandParser`, `MarkCommand`, and `MarkCommandParser` classes.

### Ivan Ang's Acknowledgements
* Used ChatGPT to assist in writing my Project Portfolio Page in `docs/team/hiivan.md`.

### Axel Teo's Acknowledgements
* Used ChatGPT to assist in writing my Project Portfolio Page in `docs/team/teojunda.md`.
--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280"></puml>

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S2-CS2103T-F14-3/tp/tree/master/src/main/java/seedu/tatoolkit/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103T-F14-3/tp/tree/master/src/main/java/seedu/tatoolkit/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `dc 1` to delete a person.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/ay2324s2-cs2103t-f14-3/tp/tree/master/src/main/java/seedu/tatoolkit/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, `SidePanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFX UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/ay2324s2-cs2103t-f14-3/tp/tree/master/src/main/java/seedu/tatoolkit/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/ay2324s2-cs2103t-f14-3/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/ay2324s2-cs2103t-f14-3/tp/tree/master/src/main/java/seedu/tatoolkit/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="450" />

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("dc 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `TaToolkitParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `TaToolkitParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `TaToolkitParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103T-F14-3/tp/tree/master/src/main/java/seedu/tatoolkit/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the TA Toolkit data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/ay2324s2-cs2103t-f14-3/tp/tree/master/src/main/java/seedu/tatoolkit/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both TA Toolkit data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `TaToolkitStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.tatoolkit.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Edit a `Person`

Edits the details of an existing `Person` identified by their `INDEX`in the displayed person list.
The commands are implemented in the `EditCommand` class which extend the `Command` class.

* Step 1. The `EditCommand` object's `execute()` method is called.
* Step 2. The `INDEX` is checked to be within the valid range of the displayed person list. If the `INDEX` given is invalid (i.e., out of range), a `CommandException` is thrown.
* Step 3. The `Person` at the given `INDEX` is referenced and `deletePerson()` is called to remove `originalPerson` from person list.
* Step 4. The field(s) to be edited are checked.
  * If there are no fields to be edited, a `CommandException` is thrown.
  * If any of the edited fields are invalid, a `CommandException` is thrown.
  * If the edited person is the same as the original person, a `CommandException` is thrown`.
  * If any of `Email`, `Phone`, `Telegram`, `Github` fields are duplicates with any existing person in person list, a `CommandException` is thrown.
* Step 5. The model object's `addPerson()` method is called. The input parameter is the `editedPerson` with the edited details.
* Step 6. The `Person` field(s) are edited.

The diagram below describes this behaviour concisely. It shows how a user’s command is processed and what message is ultimately shown if they decide to edit a person.

<puml src="diagrams/EditCommandActivityDiagram.puml" />

The sequence diagram below also shows the interaction between the various components during the execution of the `EditCommand`.

<puml src="diagrams/EditCommandSequenceDiagram.puml" />

#### Design considerations:

**Aspect: How editing a Person works:**

* **Alternative 1 (current choice):** Removes the `originalPerson` and adds the `editedPerson`.
    * Pros: Retains the sorted order of Persons by `Name` in the person list.
    * Cons: May have performance issues in terms of time complexity since it requires 2 operations (`deletePerson()` and `addPerson`).

* **Alternative 2:** Directly update the fields in the `originalPerson`
    * Pros: Better performance, since this only requires searching through the person list once.
    * Cons: The order of person list will be lost, since `Name` of a `Person` may be edited.

### View a `Person`

Views the details of an existing `Person` identified by their `INDEX`in the displayed person list.
The commands are implemented in the `ViewCommand` class which extend the `Command` class.

* Step 1. The `ViewCommand` object's `execute()` method is called.
* Step 2. The `INDEX` is checked to be within the valid range of the displayed person list. If the `INDEX` given is invalid (i.e., out of range), a `CommandException` is thrown.
* Step 3. The `Person` at the given `INDEX` is referenced and then displayed to the user on the right side panel.

The diagram below describes this behaviour concisely. It shows how a user’s command is processed and what message is ultimately shown if they decide to view a person.

<puml src="diagrams/ViewCommandActivityDiagram.puml" />

The sequence diagram below also shows the interaction between the various components during the execution of the `ViewCommand`.

<puml src="diagrams/ViewCommandSequenceDiagram.puml" />

<div style="page-break-after: always;"></div>

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedTaToolkit`. It extends `TaToolkit` with an undo/redo history, stored internally as an `taToolkitStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedTaToolkit#commit()` — Saves the current TA Toolkit state in its history.
* `VersionedTaToolkit#undo()` — Restores the previous TA Toolkit state from its history.
* `VersionedTaToolkit#redo()` — Restores a previously undone TA Toolkit state from its history.

These operations are exposed in the `Model` interface as `Model#commitTaToolkit()`, `Model#undoTaToolkit()` and `Model#redoTaToolkit()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedTaToolkit` will be initialized with the initial TA Toolkit state, and the `currentStatePointer` pointing to that single TA Toolkit state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `dc 5` command to delete the 5th person in the TA Toolkit. The `dc` command calls `Model#commitTaToolkit()`, causing the modified state of the TA Toolkit after the `dc 5` command executes to be saved in the `taToolkitStateList`, and the `currentStatePointer` is shifted to the newly inserted TA Toolkit state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `ac n/David …​` to add a new person. The `ac` command also calls `Model#commitTaToolkit()`, causing another modified TA Toolkit state to be saved into the `taToolkitStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitTaToolkit()`, so the TA Toolkit state will not be saved into the `taToolkitStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoTaToolkit()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous TA Toolkit state, and restores the TA Toolkit to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial TaToolkit state, then there are no previous TaToolkit states to restore. The `undo` command uses `Model#canUndoTaToolkit()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoTaToolkit()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the TA Toolkit to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `taToolkitStateList.size() - 1`, pointing to the latest TA Toolkit state, then there are no undone TaToolkit states to restore. The `redo` command uses `Model#canRedoTaToolkit()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `ls`. Commands that do not modify the TA Toolkit, such as `ls`, will usually not call `Model#commitTaToolkit()`, `Model#undoTaToolkit()` or `Model#redoTaToolkit()`. Thus, the `taToolkitStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitTaToolkit()`. Since the `currentStatePointer` is not pointing at the end of the `taToolkitStateList`, all TA Toolkit states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire TA Toolkit.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `dc`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

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

* is a CS2100 TA in NUS School of Computing
* has a need to manage a significant number of contacts over different communication channels
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* prefers to use a separate app that is made to manage student contacts efficiently
* is reasonably comfortable using CLI apps

**Value proposition**:

* TA Toolkit is a desktop app that helps the user manage a large number of student contacts
across different communication channels efficiently. It is optimised for use via a Command
Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI)
* It categorises contacts into their tutorial group, allowing for easier management of contacts
* It allows the user to add, delete, edit, find and view student contacts
* Users are able to take notes on specific students and keep track of students' attendance
### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                                   | So that I can…​                                          |
|----------|---------|------------------------------------------------|----------------------------------------------------------|
| `* * *`  | TA      | add a student contact                          | keep track of my students when I need to                 |
| `* * *`  | TA      | delete a student contact                       | remove students from the database if they drop the class |
| `* * *`  | TA      | view all student contacts                      | see a list of all my students                            |
| `* *`    | TA      | view a students' detailed information          | see all the information related to a student             |
| `* *`    | TA      | edit a student contact                         | update a students’s details should they change           |
| `* *`    | TA      | assign student to a class                      | organise students by their class                         |
| `* *`    | TA      | take notes on students                         | keep track of their strengths and weaknesses             |
| `* *`    | TA      | delete notes on students                       | remove notes that are no longer relevant                 |
| `*`      | TA      | mark student as absent for a specific week     | be aware of who is missing lessons                       |
| `*`      | TA      | mark student as present for a specific week    | correct mistakes in attendance marking                   |
| `* `     | TA      | view all student contacts for a specific class | see a list of students in a project team                 |
| `* `     | TA      | view summary of all students attendance        | get a quick overview of class attendance                 |

### Use cases

(For all use cases below, the **System** is the `TA Toolkit` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Adding a student**

**MSS**

1. User enters command to add a student
2. TA Toolkit adds the student to the list of students
3. TA Toolkit displays a success message along with the student’s contact details

    Use case ends.

**Extensions**

* 1a. The add student command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 2a. A field of the new student (Email, Phone number, Telegram, Github) already exists in the list of students.
    * 2a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC02 - Deleting a student**

**MSS**

1. User enters command to delete a student
2. TA Toolkit deletes the student’s contact and displays a success message

   Use case ends.

**Extensions**

* 1a. TA Toolkit does not contain the student specified
    * 1a1. TA Toolkit shows an error message.

      Use case ends.

---

**Use case: UC03 - Update a student**

**MSS**

1. User enters the command to update a student’s details
2. TA Toolkit modifies the values that user intended to replace
3. TA Toolkit displays a success message along with the student’s updated contact details

    Use case ends.

**Extensions**

* 1a. The update student command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 2a. Student does not exist in the list of students.
    * 2a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 2b. The updated student contact is the same as the original student contact.
    * 2a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 2c. The updated student contact shares a duplicate field (Email, Phone number, Github, Telegram) with another contact.
    * 2c1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC04 - Viewing students overview**

**MSS**

1. User requests to view all student
2. TA Toolkit shows a list of all members
3. TA Toolkit displays a success message

    Use case ends.

---

**Use case: UC05 - View a student detailed information (contact details, attendance, notes)**

**MSS**

1. User requests to view the detailed information of a student
2. TA Toolkit shows the detailed information of the student
3. TA Toolkit displays a success message

   Use case ends.

**Extensions**

* 1a. The view student command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1b. The requested student is invalid.
    * 1b1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC06 - Add note on student**

**MSS**

1. User requests to add a note regarding a student
2. TA Toolkit adds the note to the student’s list of notes
3. TA Toolkit displays a success message

   Use case ends.

**Extensions**

* 1a. The add note command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1b. The requested student is invalid.
    * 1b1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1c. The note to be added is invalid.
    * 1c1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC07 - Delete notes for a student**

**MSS**

1. User requests to delete a set of notes for a student
2. TA Toolkit removes the note from the student’s list of notes
3. TA Toolkit displays a success message

   Use case ends.

**Extensions**

* 1a. The delete note command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1b. The requested student is invalid.
    * 1b1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1c. The set of notes requested to be deleted is invalid.
    * 1c1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC08 - Mark students as present for a week**

**MSS**

1. Use requests to mark a set of students as present for a week
2. TA Toolkit marks the set of students as present for that week
3. TA Toolkit displays a success message

   Use case ends.

**Extensions**

* 1a. The mark student command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1b. The requested set of students is invalid.
    * 1b1. TA Toolkit shows an error message.

      Use case resumes at step 1.

* 1c. The requested week to be marked is invalid.
    * 1c1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC09 - Mark students as absent for a week**

Similar to UC08 - Marking students as absent rather than present.

---

**Use case: UC10 - List all students by class**

**MSS**

1. User requests to list all students from a specific class
2. TA Toolkit displays a list of all members from that class
3. TA Toolkit displays a success message

   Use case ends.

**Extensions**

* 1a. The list student command format is invalid.
    * 1a1. TA Toolkit shows an error message.

      Use case resumes at step 1.

---

**Use case: UC11 - View summary of students attendance**

**MSS**

1. User requests to view a summary of all students attendance
2. TA Toolkit displays a summary of all students attendance
3. TA Toolkit displays a success message

   Use case ends.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should
be able to accomplish most of the tasks faster using commands than using the mouse.
4.  All functions within the application must provide responses to user queries within a timeframe of 2 seconds.
5.  The application does not support concurrent usage by multiple users.
6.  The application does not offer support for languages aside from English.
7.  The application should be able to handle most common user input errors and provide meaningful error messages.
8.  The application should work without internet connection.
9.  The application's GUI should effectively organise and display data, facilitating easy comprehension of application details for users.

### Glossary

* **Absent**: A attendance status to indicate that a student did not attend a class.
* **Alphanumeric**: A String consisting of only letters (a-z, A-Z) or numbers or both.
* **API**: Application Programming Interface
* **Command**: Commands are necessary to use TA Toolkit. A command has to be typed into the Command Box and entered to be executed.
* **Command terminal**: A command terminal is a text-based interface through which users can interact with a computer program by typing commands.
* **CLI**: Command Line Interface: A way of interacting with a computer program where the user issues commands to the
  program in the form of successive lines of text (command lines). It emphasises text-based user interaction over graphical user interfaces.
* **Email**: A unique identifier for an email account.
* **Index**: A number representing the position of an item in a list.
* **Github ID**: A unique identifier for a Github account. E.g. johnDoe
* **GUI**: Graphical User Interface: A mode of interacting with a computer program that relies on visual elements such as windows, icons, buttons, and menus.
* **Hard disk**: A component of a computer system responsible for long-term storage of data.
* **IETF**: Internet Engineering Task Force
* **JSON file**: A JSON (JavaScript Object Notation) file is a structured data file format used for storing and transmitting data between the hard disk and TA Toolkit.
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **MSS**: Main Success Scenario: The main flow of events in a use case.
* **Note**: A String that can be associated to a person to provide additional details about them.
* **OOP**: Object-Oriented Programming
* **Phone number**: A sequence of digits that is dialled on a telephone to contact a person.
* **Present**: A attendance status to indicate that a student attended a class.
* **TA**: Teaching Assistant
* **Telegram handle**: A unique identifier for a Telegram account. E.g. @johnDoe

--------------------------------------------------------------------------------------------------------------------

# **Appendix**

## Instructions for manual testing

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar TAToolkit.jar` command to run the application.<br>
      Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

### Adding a person

Prerequisite: There is no person in TA Toolkit with the same email, phone number, telegram, and github as the person to be added.

Test case: `ac n/John Doe c/T42 p/98765432 e/johnd@example.com t/johndoe g/johnDoeGithub`

Expected Output in the Displayed Person List: The new person is added into the list.

Expected Output in the Result Display: A message to inform user that new person has been added along with the person’s details.

Test case: `ac`

Expected Output in the Result Display: An error message is shown, providing details on the correct format.

### Deleting a person

Prerequisite: There is at least 1 person in the Displayed Person List.

Test case: `dc 1`

Expected Output in the Displayed Person List: First contact is deleted from the list.

Expected Output in the Result Display: Details of the deleted person is shown in the status message.

Test case: `dc 0`

Expected: No person is deleted. Error details shown in the Result Display.

### Update a person

Prerequisite: There is at least 1 person in the Displayed Person List. This updated information must be different from the person to be updated.

Test case: `uc 1 n/Ryan Lim Jun Jie`

Expected Output in the Displayed Person List: The first person in the list has their name changed to “Ryan Lim Jun Jie”, and retains the rest of their details. The persons in the Displayed Person List are reordered.

Expected Output in the Result Display: Details of the updated person is shown in the status message.

### View a person

Prerequisite: There is at least 1 person in the Displayed Person List.

Test case: `view 1`

Expected Output in the Side Panel Display: All details related to the first person in the list are displayed.

Expected Output in the Result Display: Details of the viewed person is shown in the status message.

### Find a person by matching name

Prerequisite: There are only 2 persons named "Alex Yeoh" and "Bernice Yu" respectively in TA Toolkit.

Test case: `find Alex`

Expected Output in the Displayed Person List: The details of "Alex Yeoh" are shown.

Expected Output in the Result Display: Message states “1 persons listed”.

Test case: `find Zachary`

Expected Output in the Displayed Person List: The list is empty.

Expected Output in the Result Display: Message states “0 persons listed”.

### Mark attendance

Prerequisite: There is at least 1 person in the Displayed Person List.

Test case: `ma w/1 abs/1`

Expected Output in the Result Display: A message that informing that the first person in list has been marked as absent.

### List persons in a class/ View attendance for a class

Prerequisite: There is at least 1 person with the class "T42". They should be marked as absent.

Test case: `ls T42`

Expected Output in the Displayed Person List: All persons from the class "T42" are displayed.

Expected Output in the Result Display: Message states the number of persons displayed.

Expected Output in the Side Panel Display: The list of absentees from the class "T42" are displayed by week.

### Adding a note to a person

Prerequisite: There is at least 1 person in the Displayed Person List. The same person is being viewed in the Side Panel Display.

Test case: `an 1 note/very hardworking!`

Expected Output in the Result Display: Message states the details of the first person in the list, as well as the note added.

Expected Output in the Side Panel Display: The new note is displayed under the notes section of the person.

### Deleting a note from a person

Prerequisite: There is 1 person with 1 note in the Displayed Person List. The same person is being viewed in the Side Panel Display.

Test case: `dn 1 i/1`

Expected Output in the Result Display: Message states the details of the first person in the list, as well as the note deleted.

Expected Output in the Side Panel Display: The first note is removed from the notes section of the person.

### Clearing the application data

Test case: `clear`

Expected Output in the Displayed Person List: Nothing is displayed.

Expected Output in the Side Panel Display: Nothing is displayed.

Expected Output in the Result Display: A message stating that app data has been cleared.

### Help

Test case: `help`

Expected Output: A window shows up and provides the link to the User Guide.

### Exit

Test case: `exit`

Expected Output: TA Toolkit closes.

--------------------------------------------------------------------------------------------------------------------

## Appendix: Planned Enhancements

Team Size: 5

### Better name validation

**Background**: Currently, TA Toolkit only allows contacts' name to contain alphanumeric characters and spaces. Furthermore,
names "John Doe" and "John  Doe" are considered different names due to the extra whitespace. 

**Issue**: The strict restrictions will prevent some valid names from being accepted.
For example, "Ravi S/O Ramasamy" is rejected as it contains a '/' character.
As for the extra whitespace between words in a name, it might confuse the user when searching for a contact.

**Enhancement**: We plan on changing the parameter prefixes to use the '=' character instead of the '/' character.
Furthermore, we will loosen the restrictions on names to allow the '/' character.
This will allow the TA Toolkit to accept legal names containing the '/' character.
We will also trim the extra whitespace between words in a name to prevent confusion when searching for a contact.

### Better email validation

**Background**: Currently, our system validates email addresses using the format local-part@domain, with specific restrictions for both parts to identify and reject invalid emails.

**Issue**: This validation method does not strictly adhere to the IETF standards for email addresses.
Consequently, our application may incorrectly deem invalid emails as valid.
For instance, an email like abc@12.34 may pass validation despite having a domain that violates IETF standards.

**Enhancement**: To address this issue, we aim to enhance our email validation process to align more closely with IETF standards, as specified in [RFC5322](https://datatracker.ietf.org/doc/html/rfc6854).

### Better identification of unique students

**Background**: Currently, TA Toolkit does not have a unique identifier to identify unique students.
There is only weak checking done to ensure students do not have duplicate email, phone number, telegram, github.

**Issue**: TAs might have difficulty differentiating between students with the same name.
This is a problem when looking at the attendance overview, when the TA sees a name as absent. If 2 people share that name, the TA does not know which of them is absent.

**Enhancement**: We plan on adding Student ID as a field for students, which will be the unique identifier for student contacts.
When displaying the attendance overview, the Student ID will accompany the student's name to uniquely identify him.

### More comprehensive attendance taking

**Background**: Currently, TA Toolkit only allows TAs to mark their students as present or absent.

**Issue**: This introduces confusion to the TA, when marking attendance for students with other attendance status.
For example, some students might be absent with valid reasons, like being on medical leave.

**Enhancement**: We plan on introducing more attendance statuses that TAs can mark their students with, like being absent with valid reason.

### Handling duplicate keys in JSON object

**Background**: Currently, TA Toolkit does not check for duplicate keys in JSON objects used for storage of student contact details.

**Issue**: This could lead to unexpected behaviour if the storage file is corrupted.
For example, if the JSON object representing the student contact details contains 2 `name` keys, the application should throw an error that the data is corrupted.
However, the application reads the value of the last duplicate key.

**Enhancement**: We plan on introducing checks to ensure that there are no duplicate keys for any of the JSON objects in the storage file.
This will cause TA Toolkit to throw an error and reset the data if the storage file is invalid.

### Errors Improvement

**Background**: Currently, some of TA Toolkit's error messages are not specific enough for the user to understand what went wrong.
A very large index, one that doesn't fit in an integer, might be mistakenly input by the user.
A few examples include:
1. When updating a contact with a very large index, the error message is "Invalid command format".
2. When marking attendance with a very large number in the index field, the error message is "Index is not a non-zero unsigned integer".

**Issue**: The error message provided is not consistent and not specific enough for the user to understand what went wrong.

**Enhancement**: We plan on improving the error messages to provide more specific information to the user. For example,
if a user tries to use a command with a very large index that doesn't fit in an integer, the error message will be "The person index provided is invalid.".

### UI Improvements

**Background**: Currently, the background of TA Toolkit behind the Person cards are black in colour and does not match the Nord theme.

**Issue**: This could introduce some confusion to the user as the background colour does not match the theme of the application. 
This could also lead to a poor user experience as the user might think that a bug has occurred.

**Enhancement**: We plan on changing the background colour of the application to match the Nord theme, which will provide a more consistent user experience.
