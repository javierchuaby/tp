package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TagCommand extends Command {
    public static final String COMMAND_WORD = "tag";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a tag to the person identified by the index.\n"
            + "Parameters: INDEX TAG\n"
            + "Example: " + COMMAND_WORD + " 1 friend";
    public static final String MESSAGE_SUCCESS = "Tag added: %1$s";

    private final int index;
    private final Tag tag;

    public TagCommand(int index, Tag tag) {
        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (index < 0 || index >= model.getFilteredPersonList().size()) {
            throw new CommandException("The person index provided is invalid");
        }
        Person personToTag = model.getFilteredPersonList().get(index);
        Set<Tag> updatedTags = new HashSet<>(personToTag.getTags());
        updatedTags.add(tag);
        Person updatedPerson = new Person(
            personToTag.getName(),
            personToTag.getPhone(),
            personToTag.getEmail(),
            personToTag.getAddress(),
            updatedTags,
            personToTag.isPresent()
        );
        model.setPerson(personToTag, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, tag));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand otherCommand = (TagCommand) other;
        return index == otherCommand.index && Objects.equals(tag, otherCommand.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, tag);
    }
}
