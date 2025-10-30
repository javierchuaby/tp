package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FACULTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_YEAROFSTUDY;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a new member to the club membership list in ClubTrack.
 * This command allows club exco members to quickly register new members
 * with their contact information and optional role tags.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new member to the club membership list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_YEAROFSTUDY + "YEAR_OF_STUDY] "
            + "[" + PREFIX_FACULTY + "FACULTY] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jason Lee "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "jason@example.com "
            + PREFIX_ADDRESS + "Blk 123, #01-01\n"
            + "  " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alice Tan "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "alice@nus.edu.sg "
            + PREFIX_ADDRESS + "Kent Ridge "
            + PREFIX_YEAROFSTUDY + "2 "
            + PREFIX_FACULTY + "School of Computing "
            + PREFIX_TAG + "committee "
            + PREFIX_TAG + "treasurer";


    public static final String MESSAGE_SUCCESS = "New member added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This member already exists in the club membership list";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified member to the club.
     *
     * @param person The member to be added to the club membership list.
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    /**
     * Executes the add command to register a new member in the club.
     * Checks for duplicate members (based on normalized names) before adding.
     *
     * @param model The {@code Model} containing the club membership data.
     * @return A {@code CommandResult} with a success message displaying the new member's details.
     * @throws CommandException If the member already exists in the membership list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    /**
     * Checks if this AddCommand is equal to another object.
     * Two AddCommands are equal if they add the same member.
     *
     * @param other The object to compare with.
     * @return True if both commands add the same member, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public int hashCode() {
        return toAdd.hashCode();
    }

    /**
     * Returns a string representation of this AddCommand for debugging purposes.
     *
     * @return A string containing the member to be added.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("toAdd", toAdd)
            .toString();
    }
}
