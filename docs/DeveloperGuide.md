# Developer Guide

## 1. Introduction

This Developer Guide describes the architecture, design, and implementation details of **ClubTrack**, a desktop app for university club executives to manage members, track attendance, and award participation points quickly via a CLI-first workflow.

ClubTrack is adapted from **AddressBook Level 3 (AB3)** but extended with:

* compulsory `yearOfStudy` and `faculty` for members,
* attendance and points tracking,
* **switchable member lists** (each backed by its own JSON file),
* clearer separation between `find` (name-only, substring) and `search` (tag-prefix),
* updated UI (`PersonCard`) to display points and academic info,
* tighter validation on phone numbers (SG mobile: 8 digits, start with 8 or 9),
* updated tests to match the new constraints.

This guide assumes you are familiar with **Java 17**, **JavaFX**, **Gradle**, and the **AB3 architecture**.

---

## 2. Architecture

ClubTrack follows the classic AB3 4-layer architecture:

1. **UI** – shows data and accepts commands.
2. **Logic** – parses and executes commands.
3. **Model** – holds in-memory data and exposes filtered lists to UI.
4. **Storage** – reads/writes JSON files to disk.

The entry point is `MainApp`. On startup it:

1. Loads config / user prefs
2. Initialises storage
3. Creates the model
4. Creates the logic
5. Hands control to the UI

![Architecture Diagram](diagrams/ArchitectureDiagram.png)

### 2.1 Key Architectural Extension

In AB3, storage always pointed to **one** file (`addressbook.json`).
In ClubTrack, storage can point to **different files** depending on the **active list name**. The default file is:

```text
data/default.json
```

Whenever the user runs:

```text
switch Training_2025_10_20
```

the app starts reading/writing:

```text
data/Training_2025_10_20.json
```

All mutating commands (add, edit, present, clear, addpoints, etc.) save to **the file of the currently active list**.

---

## 3. UI Component

The UI layer is JavaFX-based and largely follows AB3.

**Main classes:**

* `UiManager` – entry point to the UI
* `MainWindow` – top-level container; holds the command box, result display, status bar, and member list
* `PersonListPanel` – shows the current list of members **for the active file**
* `PersonCard` – shows:

  * name
  * year of study (e.g. `Y2`)
  * faculty (e.g. `School of Computing`)
  * phone
  * address
  * email
  * **Points: X**
  * tags (as chips)

![UI Class Diagram](diagrams/UiClassDiagram.png)

**Why this change?**
Because we made `y/` and `f/` compulsory at the parser level, the UI can safely render them without guessing defaults. Points are shown explicitly to help exco track participation.

Whenever the model reloads a different list (due to `switch`), the observable list changes and the UI automatically refreshes.

---

## 4. Logic Component

The Logic component is responsible for:

1. Receiving the raw command string from the UI,
2. Parsing it using `AddressBookParser`,
3. Creating the corresponding `Command`,
4. Executing it on the `Model`,
5. Saving through `Storage` if the command mutates data,
6. Returning a `CommandResult`.

![Logic Class Diagram](diagrams/LogicClassDiagram.png)

**Key classes:**

* `Logic` (interface)
* `LogicManager` (concrete)
* `AddressBookParser`
* `Command` (abstract)
* Concrete commands:

  * `AddCommand`
  * `EditCommand`
  * `PresentCommand`, `AbsentCommand`, `AttendanceCommand`
  * `FindCommand` (**now: name-only, substring**)
  * `SearchCommand` (**now: tag-prefix filtering**)
  * `SwitchCommand` (**new**)
  * `PointsCommand`, `AddPointsCommand`, `MinusPointsCommand`
  * `ClearCommand` (clears **current** list only)

**Important change:**
After **every** successful mutating command, `LogicManager` asks `Storage` to save **to the JSON file of the current active list name**.

---

## 5. Model Component

The Model layer keeps application state and provides observable lists to the UI.

![Model Class Diagram](diagrams/ModelClassDiagram.png)

**Main responsibilities:**

* hold the current `AddressBook` (for the active list),
* expose `FilteredList<Person>` for UI,
* remember the **active list name** (e.g. `default`, `Training_2025_10_20`),
* hold user preferences.

### 5.1 Person model

We extended AB3’s `Person` to include club-specific and attendance fields:

```text
Person
 ├─ name : Name
 ├─ phone : Phone           // 8 digits, start with 8 or 9
 ├─ email : Email
 ├─ yearOfStudy : int       // 1..4
 ├─ faculty : String        // non-empty
 ├─ address : Address
 ├─ tags : Set<Tag>
 ├─ isPresent : boolean     // for attendance
 └─ points : Points         // value object
```

We also changed **identity**:

* **Before (AB3):** name-based
* **Now:** **same person = same email OR same phone**

  * lets us store multiple people with the same name
  * still guards against accidental duplicates using contact info

---

## 6. Storage Component

The Storage layer reads/writes JSON using the usual AB3 JSON storage classes, but with an extra concept: **active list name = file name**.

![Storage Class Diagram](diagrams/StorageClassDiagram.png)

**Behaviour:**

* On startup, we try to read:

  ```text
  data/default.json
  ```

  If it does not exist, we create it with a sample ClubTrack.

* When user runs:

  ```text
  switch CCA_Showcase
  ```

  we from then on read/write:

  ```text
  data/CCA_Showcase.json
  ```

* `clear` only clears the **currently loaded** address book, and then saves it back to **the same file**.

This design keeps lists isolated. Each event/training/day can have its own file, but the rest of the architecture is unchanged.

---

## 7. Feature Implementation

### 7.1 `switch` feature

**Goal:** let exco maintain multiple independent member/attendance lists (e.g. Week 1 training, production night, ad-hoc event) without mixing data.

**Command format:**

```text
switch LIST_NAME
```

**Flow:**

1. `LogicManager` parses `LIST_NAME`
2. `Model` is told to “activate” that list
3. `Storage` tries to load `data/LIST_NAME.json`

  * if present → load
  * if missing → create new empty address book, save as `data/LIST_NAME.json`
4. UI refreshes because the model’s observable list changed

**Activity diagram:**

![Switch Activity Diagram](diagrams/SwitchActivityDiagram.png)

**Design considerations:**

* We store the active list name somewhere model-ish, so that **all** mutating commands (add, edit, present, points, clear) can save to the right file.
* We **reused** the same JSON structure from AB3 to minimise changes.
* This fits nicely with the CS2103T rule “clear should not silently clear all other files”.

---

### 7.2 `add` command (updated)

**Old (AB3):** name, phone, email, address were compulsory; others optional.
**New (ClubTrack):** we now make **year of study** (`y/`) and **faculty** (`f/`) compulsory.

**New format:**

```text
add n/NAME p/PHONE e/EMAIL y/YEAR_OF_STUDY f/FACULTY a/ADDRESS [t/TAG]...
```

**Validation:**

* `p/PHONE` → 8 digits, starts with 8 or 9
* `y/` → integer 1–4
* `f/` → non-empty
* all single-valued prefixes must not be repeated

**Why?**
Because the UI now always shows `Yx - Faculty`, so the model must **guarantee** those fields exist.

---

### 7.3 `find` vs `search`

We deliberately split them to reduce PE bugs.

#### 7.3.1 `find`

* **Purpose:** quick lookup by name only
* **Behaviour:** case-insensitive **contains** on name
* **Does NOT** search tags, faculty, year, or address
* **Examples:**

  ```text
  find john
  find alex david
  ```

  matches `John`, `Johnathan`, `Alex David`.

#### 7.3.2 `search`

* **Purpose:** structured filtering, especially tag-based
* **Behaviour:** tag **prefix** matching
* **Example:**

  ```text
  search t/com
  ```

  matches people tagged `committee`, `comms`, `community`.

This makes UG + DG consistent: **“find ≠ search”.**

---

### 7.4 Points features

We added a lightweight points system for participation:

* `addpoints INDEX pts/VALUE` – add VALUE to member’s points
* `minuspoints INDEX pts/VALUE` – subtract VALUE
* `points INDEX` – **show** member’s current points (does not edit)

This is model-only, so UI just renders `Points: X`.

---

## 8. Testing and Test Updates

Because we tightened phone + made y/f compulsory, several AB3 tests needed updating.

**What we changed:**

1. **Test constants**

  * `VALID_PHONE_AMY = "88888888"`
  * `VALID_PHONE_BOB = "99999999"`
  * Any other phone in test fixtures must be 8 digits starting with 8/9.

2. **Parser tests**

  * `AddCommandParserTest` must now include `y/` and `f/` in *all* valid examples.
  * Duplicate-prefix tests must list `y/` and `f/` in the expected error where relevant.

3. **Storage-related tests**

  * In tests where we trigger an `add` to force a save (e.g. in `LogicManagerTest`), the input must now contain **all compulsory prefixes**:

    ```text
    add n/Amy p/88888888 e/amy@example.com y/1 f/School of Computing a/Blk 123
    ```

4. **Typical persons**

  * `TypicalPersons` → change phones to valid SG numbers.

---

## 9. Editing the data file

ClubTrack stores data in:

```text
[JAR location]/data/<listName>.json
```

* Default list:

  ```text
  data/default.json
  ```
* After `switch clubtrack` (or any other name):

  ```text
  data/clubtrack.json
  ```

Advanced users may edit these JSON files manually.

> ⚠️ **Caution:** If the JSON is malformed or fields are missing, ClubTrack will start that list empty the next time it is loaded. Always back up your `data/` folder first.

---

## 10. Non-Functional Requirements

1. Runs on any mainstream OS with Java 17 or above.
2. Should support up to **200 members per list** without noticeable latency.
3. Data is saved after **every** mutating command.
4. Should work fully **offline**.
5. Default data file is **`data/default.json`**.
6. Switching lists must not affect the data of other lists.
7. UI should remain responsive while loading a different list.

---

## 11. Glossary

* **Active list** – the currently selected member list (one JSON file).
* **Tag prefix** – partial tag string used for `search`, e.g. `t/com` → `committee`.
* **Attendance** – a boolean flag (`isPresent`) stored inside `Person`.
* **Points** – integer counter for participation.
* **Exco** – executive committee member.

---

## 12. Acknowledgements

* Project structure, Gradle setup, JSON storage utilities, and some diagrams are **adapted from** the official **AddressBook Level 3 (AB3)** project by the CS2103T teaching team.
* Some command patterns (parser + command + command test) follow AB3’s recommended structure.
* Original AB3 documentation: credit to CS2103T teaching team.

---

## Appendix A – Instructions for Manual Testing

### A.1 Launch and shutdown

1. Ensure you have Java 17.
2. Run `java -jar ClubTrack.jar`.
3. Verify that a `data/default.json` file is created (if it didn’t exist).
4. Close the window → app should exit cleanly.

### A.2 Adding a member

1. Test command:

   ```text
   add n/Amy Bee p/88888888 e/amy@example.com y/2 f/School of Computing a/Blk 123
   ```

2. Expected: new card appears with:

  * name “Amy Bee”
  * `Y2 - School of Computing`
  * phone `88888888`
  * `Points: 0`

3. Error case:

   ```text
   add n/Bob p/123 e/bob@example.com y/2 f/SOC a/Tampines
   ```

   Expected: error about phone constraints.

### A.3 Switching lists

1. Run:

   ```text
   switch Training_2025_10_20
   ```

2. Expected:

  * status message says list switched / created
  * list view becomes empty (for a new list)

3. Add someone here:

   ```text
   add n/John p/91234567 e/john@example.com y/1 f/SoC a/UTown
   ```

4. Switch back:

   ```text
   switch default
   ```

   Expected: you see the original list.
   Switch again to `Training_2025_10_20` → John is still there.

### A.4 Searching

1. Add a person with tag `Logistics` and one with tag `LogiCore`.

2. Run:

   ```text
   search t/log
   ```

   Expected: both appear (prefix match).

3. Run:

   ```text
   find log
   ```

   Expected: **does not** return them unless “log” appears in their **name**.

### A.5 Clearing current list

1. Switch to a test list:

   ```text
   switch TestList
   add n/Test p/91234567 e/test@example.com y/1 f/SoC a/Biz
   clear
   ```
2. Expected: current list becomes empty; other lists are **not** affected.

---

## Appendix B – Effort

**Team size:** 5

1. **What was harder than AB3?**

  * Supporting **multiple JSON files** (one per list) without changing too much of AB3’s storage code.
  * Making `y/` and `f/` compulsory → required a wave of changes in parser tests, typical persons, and logic tests.
  * Tightening phone validation → broke many upstream tests.

2. **What we reused:**

  * AB3 command–parser–storage structure
  * AB3 diagram style and sectioning
  * AB3 test patterns

3. **What we added:**

  * `SwitchCommand` + model/state to remember active list name
  * UI changes to display year/faculty/points
  * Tag-prefix search

---

## Appendix C – Planned Enhancements

**Team size:** 5

1. **List existing lists**
   Rationale: since we now create JSON files on demand, users should be able to run something like `lists` to see available roster files.

2. **Per-event attendance history**
   Rationale: currently attendance is a simple boolean. We plan to support per-session logs (date → present/absent).

3. **Role-based tags**
   Rationale: Some tags are “roles” (President, Secretary). We can display them differently in the UI.

4. **Import from CSV**
   Rationale: many clubs keep members in Google Sheets. A CSV import would reduce data entry.
