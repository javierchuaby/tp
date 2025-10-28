package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;
import seedu.address.model.person.Tag;

/**
 * Adds points to a member identified by index in the displayed member list.
 * This command operates independently of attendance status and does not affect
 * the member's present/absent state or tags.
 */
public class AddPointsCommand extends Command {

    public static final String COMMAND_WORD = "addpoints";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Adds points to the member identified by the index number.\n"
        + "Parameters: INDEX (must be a positive integer) POINTS (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1 5";

    public static final String MESSAGE_SUCCESS = "Added %2$d points to %1$s. New total: %3$d points.";

    private final Index targetIndex;
    private final int pointsToAdd;

    /**
     * Creates an AddPointsCommand to add the specified number of points to the member at the given index.
     *
     * @param targetIndex Index of the member in the filtered member list to add points to.
     * @param pointsToAdd Number of points to add (must be positive).
     */
    public AddPointsCommand(Index targetIndex, int pointsToAdd) {
        this.targetIndex = targetIndex;
        this.pointsToAdd = pointsToAdd;
    }

    /**
     * Executes the add points command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return {@code CommandResult} that describes the result of adding points.
     * @throws CommandException If the target index is invalid (out of bounds).
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
        }

        Person personToUpdate = lastShownList.get(targetIndex.getZeroBased());
        Set<Tag> preservedTags = personToUpdate.getTags();

        Points newPoints = new Points(personToUpdate.getPoints().getValue() + pointsToAdd);

        Person updatedPerson = new Person(
            personToUpdate.getName(),
            personToUpdate.getPhone(),
            personToUpdate.getEmail(),
            personToUpdate.getYearOfStudy(),
            personToUpdate.getFaculty(),
            personToUpdate.getAddress(),
            preservedTags,
            personToUpdate.isPresent(),
            newPoints
        );

        model.setPerson(personToUpdate, updatedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
            personToUpdate.getName(),
            pointsToAdd,
            newPoints.getValue()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPointsCommand)) {
            return false;
        }

        AddPointsCommand otherCommand = (AddPointsCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
            && pointsToAdd == otherCommand.pointsToAdd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, pointsToAdd);
    }
}
