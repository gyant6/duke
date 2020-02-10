/**
 * Handles the loading, storing and saving list of tasks
 */
package duke.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import duke.exceptions.DukeException;
import duke.tasks.*;

public class Storage {

    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Looks for a file of existing list of tasks to be loaded into an ArrayList.
     * Creates a new file if file is not found
     * @return ArrayList of tasks
     * @throws DukeException If file cannot be found
     */
    public ArrayList<Task> load() throws DukeException {
        ArrayList<Task> taskList = new ArrayList<>();
        File f = new File(filePath);

        if (f.exists()) {

            try {
                Scanner sc = new Scanner(f);
                while (sc.hasNext()) {
                    String currentTask = sc.nextLine();
                    taskList.add(parseTask(currentTask));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("File not found exception");
            }
        } else {
            try {
                //System.out.println("storage" + System.getProperty("user.dir"));
                //File f1 = new File(System.getProperty("user.dir"), "src/main/java/duke/data/text.txt");
                //f1.createNewFile();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("create new file error");
            }
            throw new DukeException("Task list not found! Creating new task list");
        }

        return taskList;
    }

    private Task parseTask(String task) {
        String[] taskDetails = task.split(" \\| ");

        Character taskType = taskDetails[0].charAt(0);
        boolean taskIsDone = taskDetails[1].equals("1");
        Task t = null;

        switch (taskType) {
            case 'T':
                t = new Todo(taskDetails[2]);
                break;
            case 'D':
                try {
                    Date date = Deadline.simpleDateFormat.parse(taskDetails[3]);
                    t = new Deadline(taskDetails[2], date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    System.out.println("Error reading deadline date from file");
                }
                break;
            case 'E':
                LocalDateTime localDateTime = LocalDateTime.parse(taskDetails[3], Event.dateTimeFormatter);
                t = new Event(taskDetails[2], localDateTime);
                break;
        }

        if (t != null && taskIsDone) {
            t.markAsDone();
        }
        return t;
    }

    /**
     * Appends the current task to existing list of tasks
     * @param t Task to be added to list
     */
    public void saveTask(Task t) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            String toAdd = formatTaskForSaving(t);
            fw.write(toAdd);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in saveTask");
        }
    }

    /**
     * Updates existing task list when there is a completion or deletion
     * @param taskList Existing task list
     */
    public void updateTasks(ArrayList<Task> taskList) {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            for (Task task : taskList) {
                String toAdd = formatTaskForSaving(task);
                fw.write(toAdd);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in updateTasks");
        }
    }

    private String formatTaskForSaving(Task t) {
        StringBuilder sb = new StringBuilder();

        int isDone = 0;
        if (t.isDone()) {
            isDone = 1;
        }

        if (t instanceof Todo) {
            sb.append("T | " + isDone + " | " + t.getDescription());
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            sb.append("D | " + isDone + " | " + d.getDescription() + " | " + d.getDeadline());
        } else if (t instanceof Event) {
            Event e = (Event) t;
            sb.append("E | " + isDone + " | " + e.getDescription() + " | " + e.getDateTime());
        }
        sb.append("\n");
        return sb.toString();
    }
}
