package seedu.tatoolkit.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.tatoolkit.commons.core.index.Index;
import seedu.tatoolkit.commons.util.ToStringBuilder;
import seedu.tatoolkit.logic.Messages;
import seedu.tatoolkit.logic.commands.exceptions.CommandException;
import seedu.tatoolkit.model.Model;
import seedu.tatoolkit.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the TA Toolkit.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "dc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);
        assert !model.hasPerson(personToDelete) : "Person should not exist after deletion";
        updateLastViewedPersonIfNecessary(personToDelete, model);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    private void updateLastViewedPersonIfNecessary(Person personToDelete, Model model) {
        model.getLastViewedPerson()
                .filter(lastViewedPerson -> lastViewedPerson.equals(personToDelete))
                .ifPresent(lastViewedPerson -> model.resetLastViewedPerson());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
