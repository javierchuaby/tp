package seedu.address.testutil;

import seedu.address.model.ClubTrack;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private ClubTrack clubTrack;

    public AddressBookBuilder() {
        clubTrack = new ClubTrack();
    }

    public AddressBookBuilder(ClubTrack clubTrack) {
        this.clubTrack = clubTrack;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withPerson(Person person) {
        clubTrack.addPerson(person);
        return this;
    }

    public ClubTrack build() {
        return clubTrack;
    }
}
