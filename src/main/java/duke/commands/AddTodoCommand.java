package duke.commands;

import duke.exceptions.DukeException;
import duke.storage.Storage;
import duke.tasks.Task;
import duke.tasks.TaskList;
import duke.tasks.Todo;

public class AddTodoCommand extends Command {

    /**
     * Adds a Todo task to TaskList
     * @param description Description about the Todo task
     * @param tasks List of current tasks
     * @param storage For storing of tasks into file
     * @throws DukeException If input format is wrong
     */
    public static String execute(String description, TaskList tasks, Storage storage) {

        Task task = new Todo(description);
        tasks.addTask(task);
        storage.saveTask(task);

        StringBuilder output = new StringBuilder();
        output.append("This task has been added successfully:\n"
                + task.toString() + "\n"
                + "Now you have " + tasks.size() + " tasks in the list\n");

        return output.toString();
    }
}
