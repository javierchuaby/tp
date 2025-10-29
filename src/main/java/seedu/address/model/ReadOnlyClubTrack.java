package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyClubTrack {

    /**
     * Returns an unmodifiable view of the members list.
     * This list will not contain any duplicate members.
     */
    ObservableList<Person> getPersonList();

}
