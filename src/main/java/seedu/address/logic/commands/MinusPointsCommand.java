package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Points;
import seedu.address.model.person.Tag;

/**
 * Subtracts points from a member identified by index in the displayed member list.
 * This command operates independently of attendance status and does not affect
 * the member's present/absent state or tags. Points will not go below zero.
 */
public class MinusPointsCommand extends Command {

    public static final String COMMAND_WORD = "minuspoints";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Subtracts points from the member identified by the index number.\n"
        + "Parameters: INDEX (must be a positive integer) POINTS (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1 3";

    public static final String MESSAGE_SUCCESS = "Subtracted %2$d points from %1$s. New total: %3$d points.";

    private final Index targetIndex;
    private final int pointsToSubtract;

    /**
     * Creates a MinusPointsCommand to subtract the specified number of points from the member at the given index.
     *
     * @param targetIndex Index of the member in the filtered member list to subtract points from.
     * @param pointsToSubtract Number of points to subtract (must be positive). The member's points will not go below
     *                         zero.
     */
    public MinusPointsCommand(Index targetIndex, int pointsToSubtract) {
        this.targetIndex = targetIndex;
        this.pointsToSubtract = pointsToSubtract;
    }

    /**
     * Executes the minus points command and returns the result message.
     * Points are subtracted from the target member's current total, but will not go below zero.
     *
     * @param model {@code Model} which the command should operate on.
     * @return {@code CommandResult} that describes the result of subtracting points.
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

        Points newPoints = personToUpdate.getPoints().subtract(pointsToSubtract);

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
            pointsToSubtract,
            newPoints.getValue()));
    }
}
