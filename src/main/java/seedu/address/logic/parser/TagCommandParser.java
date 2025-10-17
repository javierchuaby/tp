package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Tag;

/**
 * Parses input arguments and creates a new {@link TagCommand} object.
 *
 * <p>Expected format:</p>
 * <pre>
 *   tag INDEX TAG
 *   e.g., tag 1 Treasurer
 * </pre>
 *
 * <p>The {@code INDEX} is 1-based as shown in the current filtered list. This parser converts it to 0-based
 * for internal use. The {@code TAG} must be non-blank; any additional whitespace after the index is trimmed.</p>
 */
public class TagCommandParser implements Parser<TagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of {@link TagCommand}
     * and returns a {@code TagCommand} object for execution.
     *
     * @param args Arguments following the {@code tag} command word.
     * @return A {@code TagCommand} ready for execution.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    @Override
    public TagCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmed.split("\\s+", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
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
        return new TagCommand(oneBasedIndex - 1, tag);
        // (Model bounds checking is performed during command execution.)
    }
}
