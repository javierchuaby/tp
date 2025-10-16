package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Tag;

/**
 * Parses input arguments and creates a new {@link UntagCommand} object.
 *
 * <p>Expected format:</p>
 * <pre>
 *   untag INDEX TAG
 *   e.g., untag 1 Treasurer
 * </pre>
 *
 * <p>The {@code INDEX} is 1-based as shown in the current filtered list.
 * This parser converts it to 0-based for internal use.</p>
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of {@link UntagCommand}
     * and returns an {@code UntagCommand} object for execution.
     *
     * @param args Arguments following the {@code untag} command word.
     * @return A {@code UntagCommand} ready for execution.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    @Override
    public UntagCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmed.split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        int oneBasedIndex;
        try {
            oneBasedIndex = Integer.parseInt(parts[0]);
        } catch (NumberFormatException ex) {
            throw new ParseException("Index must be a positive integer.");
        }
        if (oneBasedIndex <= 0) {
            throw new ParseException("Index must be a positive integer.");
        }

        Tag tag = new Tag(parts[1].trim());
        return new UntagCommand(oneBasedIndex - 1, tag);
        // Bounds check against the filtered list happens during command execution.
    }
}
