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
 * This command registers a member with contact details, year of study, faculty,
 * and optional tags (e.g., roles/committees).
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    // in AddCommand.java
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new member to the club membership list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_YEAROFSTUDY + "YEAR_OF_STUDY "
            + PREFIX_FACULTY + "FACULTY "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jason Lee "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "jason@example.com "
            + PREFIX_YEAROFSTUDY + "2 "
            + PREFIX_FACULTY + "SoC "
            + PREFIX_ADDRESS + "Blk 123, #01-01 "
            + PREFIX_TAG + "committee "
            + PREFIX_TAG + "treasurer";


    public static final String MESSAGE_SUCCESS = "New member added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "This member already exists in the club membership list";

    private final Person toAdd;

    /**
     * Creates an {@code AddCommand} to add the specified {@code Person}.
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddCommand)) {
            return false;
        }
        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
