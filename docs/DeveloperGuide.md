# Developer Guide

## 1. Introduction

This Developer Guide describes the architecture, design, and implementation details of **ClubTrack**, a desktop app for university club executives to manage members, track attendance, and award participation points quickly via a CLI-first workflow.

ClubTrack is adapted from AB3 but extended with:

* compulsory `yearOfStudy` and `faculty` for members,
* attendance and points tracking,
* switchable “member lists” (e.g. per event) backed by separate JSON files,
* richer search over tags,
* updated UI (`PersonCard`) to display points and academic info.

This guide assumes you are familiar with Java 17, JavaFX, Gradle, and the AB3 architecture.

---

## 2. Architecture

The overall architecture follows the standard 4-layered AB3 style:

* **UI**: displays data to the user and captures user input.
* **Logic**: parses and executes user commands.
* **Model**: holds in-memory data (members, filtered list, current list name).
* **Storage**: reads/writes data to JSON files on disk.

The main class is `MainApp`, which:

1. Initializes config and storage,
2. Creates the `Model`,
3. Creates the `Logic`,
4. Starts the `Ui`.

![Architecture Diagram](images/ArchitectureDiagram.png)

**Key extension:** the Storage layer can now **load/save different JSON files** depending on the *active list name* (switched via the `switch` command). The default file is `default.json` under `data/`.

---

## 3. UI Component

The UI is built with JavaFX and FXML, similar to AB3.

**Key classes:**

* `UiManager` – entry point for UI.
* `MainWindow` – main app window that contains the command box, result display, and member list panel.
* `PersonListPanel` – shows the list of members corresponding to the *current* list selected.
* `PersonCard` – displays:

  * name, phone, email
  * address
  * **year of study** (e.g. `Y2`)
  * **faculty** (e.g. `School of Computing`)
  * **points** (shown as `Points: X`)
  * tags

![UI Component](diagrams/UiClassDiagram.png)

The UI listens to changes in the `FilteredList<Person>` exposed by the Model. When `switch` changes the active list and the Model reloads that list, the UI is automatically refreshed.

---

## 4. Logic Component

The **Logic** component:

1. Receives a raw command string from the UI,
2. Uses `AddressBookParser` to parse it,
3. Instantiates the corresponding `Command`,
4. Executes it on the `Model`,
5. Returns a `CommandResult` to the UI.

![Logic Component](diagrams/LogicClassDiagram.png)

**Important classes:**

* `LogicManager` – main entry point for logic.
* `AddressBookParser` – dispatches to individual command parsers.
* `Command` – base abstract class.
* Concrete commands:

  * `AddCommand`
  * `EditCommand`
  * `PresentCommand` / `AbsentCommand` / `AttendanceCommand`
  * `SearchCommand` (tag/prefix-based)
  * `FindCommand` (name-only, substring)
  * **`SwitchCommand`** (new)
  * `PointsCommand`, `AddPointsCommand`, `MinusPointsCommand`

On successful commands that mutate data, `LogicManager` tells `Storage` to save **the current list** to its corresponding JSON file.

---

## 5. Model Component

The **Model** holds application state:

* `AddressBook` (current list of members)
* `FilteredList<Person>` (for display)
* *Active list name* (e.g. `"default"`, `"Training_2025_10_20"`)
* `UserPrefs`

![Model Component](diagrams/ModelClassDiagram.png)

### 5.1 Person model

`Person` was extended from AB3 to support club-specific fields:

* `Name name`
* `Phone phone` (now: **must be 8 digits and start with 8 or 9**)
* `Email email`
* `int yearOfStudy`
* `String faculty`
* `Address address`
* `Set<Tag> tags`
* `boolean isPresent`
* `Points points`

`Points` is a value object that is **immutable** and supports `addPoint()`, `subtractPoint()`, and `getValue()`.

**Identity semantics:**
We updated `Person#isSamePerson` to **not** use name anymore (to allow duplicate names). Identity is now determined by **email or phone** – if either matches, it is considered the same person. This prevents accidental duplicates by contact, but allows two “John Tan” entries.

---

## 6. Storage Component

The Storage layer reads and writes data in JSON.

![Storage Component](diagrams/StorageClassDiagram.png)

**What changed:**

* Previously, AB3 always stored in `addressbook.json`.
* **Now**, ClubTrack stores in `data/<activeListName>.json`.

  * On startup: load `data/default.json` (if missing, create a new one).
  * After `switch Training_2025_10_20`: subsequent saves go to `data/Training_2025_10_20.json`.
  * `clear` clears only the **current** list, then saves back to the **current** file.

This aligns with the new user-facing behaviour: “switching lists” = “working with a different file”.

---

## 7. Implementation Details

### 7.1 `switch` feature

**Purpose:** allow exco to maintain multiple *contexts* (event rosters, performance nights, special training sessions) without mixing them into one huge list.

**User command:**

```text
switch LIST_NAME
```

**Behaviour:**

1. Logic parses the `LIST_NAME`.
2. Model is asked to “activate” that list.
3. Storage tries to load `data/LIST_NAME.json`.

  * If it exists → load into Model.
  * If it does not exist → create a new empty address book and use that file moving forward.
4. UI refreshes to show members from that list.

**Activity diagram:**

![Switch Activity Diagram](diagrams/SwitchActivityDiagram.png)

**Key design points:**

* We keep the “current list name” in the Model (or a model-like component), so that every mutating command knows which file to save to.
* We reuse the same `AddressBook` structure; only the backing file changes.
* `clear` now clears only that active file.

---

### 7.2 `find` vs `search`

We intentionally separated concerns:

#### 7.2.1 `find`

* **New behaviour**: find only matches **names**.
* It performs **substring / contains** matching on the name.
* Does **not** look at tags, faculty, or year anymore.
* This keeps `find` simple and predictable for the user.

**Example:**

```text
find john
find alex david
```

Matches `John`, `Johnathan`, `Alex David`.

#### 7.2.2 `search`

* Designed for **structured filtering**, especially tags.
* New behaviour: search by **tag prefix**.
* Command:

  ```text
  search t/com
  ```

  will match tags like `committee`, `community`, `comms`, as long as the tag **starts with** the given prefix.
* You can also combine name and tag filters if needed.

This split keeps the UX clear: **“find is for names, search is for tags.”**

---

### 7.3 `add` command changes

We made the following changes to `AddCommandParser`:

1. **Year of Study (`y/`) and Faculty (`f/`) are now compulsory.**
2. We **no longer default** to `Y1` + `School of Computing` inside the parser.
3. We validate:

  * `y/` must be an integer 1–4 (parser throws otherwise),
  * `f/` must be non-empty,
  * `p/` must be a valid Singapore mobile number (8 digits, starts with 8 or 9).

This ensures that the UI can safely display `Y2, School of Computing` without guessing.

**Example:**

```text
add n/John Doe p/91234567 e/johnd@example.com y/2 f/School of Computing a/Blk 123, #01-01 t/committee
```

---

## 8. Developer Notes on Tests

Because we tightened validation on **phone**, many existing AB3-style tests that used short or fake numbers will fail. To fix:

* Update test constants in `CommandTestUtil`:

  * `VALID_PHONE_AMY = "88888888"`
  * `VALID_PHONE_BOB = "99999999"`
* Update `TypicalPersons` to use valid 8-digit numbers starting with 8 or 9.
* Update parser tests to include `y/` and `f/` since add now requires them.

This keeps tests consistent with the more realistic constraints we introduced.

---

## 9. Editing the data file

ClubTrack data are stored automatically as **JSON** files under:

```text
[JAR location]/data/<listName>.json
```

* The default list is stored in:

  ```text
  [JAR location]/data/default.json
  ```

* When you run:

  ```text
  switch Training_2025_10_20
  ```

  ClubTrack will load/save at:

  ```text
  [JAR location]/data/Training_2025_10_20.json
  ```

Advanced users may edit these files directly.

> ⚠️ **Caution:** Invalid JSON or mismatched field names will cause ClubTrack to start with an empty list for that file. Always back up before editing.

---

## 10. Non-Functional Requirements (NFRs)

1. Runs on any mainstream OS with Java 17+.
2. Should handle up to 200 members per list without noticeable lag.
3. Saves after **every** mutating command.
4. Works offline.
5. CLI-first and keyboard-friendly.

---

## 11. Glossary

* **Active list**: the currently selected member list (e.g. `default`, `Training_2025_10_20`) – determines which JSON file is read/written.
* **Points**: an integer score attached to a member, used for participation.
* **Tag prefix search**: a search where `t/com` matches tags starting with `com...`.

---

That’s the full DG section you can drop into your `DeveloperGuide.md` and it will reference your existing images in `diagrams/`.
