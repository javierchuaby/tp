package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UntagCommand extends Command {
    public static final String COMMAND_WORD = "untag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a tag from the person identified by the index.\n"
            + "Parameters: INDEX TAG\n"
            + "Example: " + COMMAND_WORD + " 1 friend";
    public static final String MESSAGE_SUCCESS = "Tag removed: %1$s";

    private final int index;
    private final Tag tag;

    public UntagCommand(int index, Tag tag) {
        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (index < 0 || index >= model.getFilteredPersonList().size()) {
            throw new CommandException("The person index provided is invalid");
        }
        Person personToUntag = model.getFilteredPersonList().get(index);
        Set<Tag> updatedTags = new HashSet<>(personToUntag.getTags());
        updatedTags.remove(tag);
        Person updatedPerson = new Person(
            personToUntag.getName(),
            personToUntag.getPhone(),
            personToUntag.getEmail(),
            personToUntag.getYearOfStudy(),
            personToUntag.getFaculty(),
            personToUntag.getAddress(),
            updatedTags,
            personToUntag.isPresent(),
            personToUntag.getPoints()
        );
        model.setPerson(personToUntag, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, tag));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UntagCommand)) {
            return false;
        }
        UntagCommand otherCommand = (UntagCommand) other;
        return index == otherCommand.index && Objects.equals(tag, otherCommand.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, tag);
    }
}