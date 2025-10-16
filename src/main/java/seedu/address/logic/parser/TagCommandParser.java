package seedu.address.logic.parser;

import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Tag;

public class TagCommandParser implements Parser<TagCommand> {
    @Override
    public TagCommand parse(String args) throws ParseException {
        String[] parts = args.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new ParseException("Invalid command format");
        }
        int index = Integer.parseInt(parts[0]) - 1;
        Tag tag = new Tag(parts[1]);
        return new TagCommand(index, tag);
    }
}