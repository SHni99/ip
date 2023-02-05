package duke;

import duke.command.Command;
import duke.exceptions.TaskException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.tasklist.TaskList;
import duke.ui.Ui;


/**
 * Acts as the main class for execution of user inputs.
 */
public class Duke {
    private Storage storage;
    private TaskList tasks;
    private Parser parser;
    private Ui ui;
    private boolean exitStatus;

    /**
     * Initialises classes need to read and write the program.
     *
     * @param filePath file name in the form of string
     */
    public Duke(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath, this.ui);
        this.tasks = new TaskList(this.storage, this.ui);
        this.parser = new Parser();
        this.exitStatus = false;
    }

    /**
     * Allow users to add, mark and un-mark, delete, add task (to-do, deadline, event)
     * or show items in a list and will exit if the bye command is returned
     *
     * @param input instruction from the user
     * @return task description as result
     */
    public String userInputs(String input) {
        Ui.welcomeMessage();
        this.storage.loadFileData();

        try {
            Command c = this.parser.parse(input);
            assert c != null : "Command is null";
            this.exitStatus = c.isExit();
            this.storage.writeToFile();
            return c.execute(tasks, storage, ui);

        } catch (TaskException e) {
            return e.getMessage();
        } catch (IndexOutOfBoundsException e) {
            return "Nothing to mark/unmark/delete!";
        }

    }

    /**
     * Closes app upon receiving 'true'
     *
     * @return boolean of exitStatus
     */
    public boolean isExitApp() {
        return this.exitStatus;
    }

}
