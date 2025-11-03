package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SwitchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link SwitchCommand} object.
 */
public class SwitchCommandParser implements Parser<SwitchCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SwitchCommand
     * and returns a SwitchCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public SwitchCommand parse(String args) throws ParseException {
        String trimmed = args.trim();

        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SwitchCommand.MESSAGE_USAGE));
        }

        if (trimmed.contains("*") || trimmed.contains("?") || trimmed.contains("<")
            || trimmed.contains(">") || trimmed.contains("|") || trimmed.contains("\"")
            || trimmed.contains(":") || trimmed.contains("\\") || trimmed.contains("/")) {
            throw new ParseException("List name cannot contain special characters: * ? < > | \" : \\ /");
        }

        return new SwitchCommand(trimmed);
    }
}
