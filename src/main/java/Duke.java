import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;
import java.lang.NullPointerException;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.time.LocalDate;

public class Duke {

    // Enum list to contain instructions to be entered by user to control system
    public enum Instructions {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE
    }

    private static Scanner sc = new Scanner(System.in);
    private static String DUKETXT = "./data/duke.txt";
    private static ArrayList<Task> list = new ArrayList<Task>(100);

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println("Hello from\n" + logo);

        userInputs();

    }

    // Allow users to add, mark and un-mark, delete, add tasks (to-do, deadline,
    // event) or show items in a list
    public static void userInputs() {

        System.out.println("Hello! I'm Duke");
        System.out.println("What can I do for you?");

        loadFileData();

        try {
            while (true) {
                String input = sc.nextLine();
                String[] part = input.split(" ");
                int index = 0;
                String first_word = part[0].toUpperCase();

                if (part[0].equals("mark") || part[0].equals("unmark") || part[0].equals("delete")) {
                    index = Integer.parseInt(part[1]) - 1;
                }

                if (!Arrays.stream(Instructions.values()).anyMatch(x -> (x.toString()).equals(first_word))) {
                    throw new TaskException("Sorry! Duke has no idea what it is as it is not an instruction");
                }

                Instructions instruction = Instructions.valueOf(first_word);

                switch (instruction) {
                    // Exit the system upon entering
                    case BYE:
                        System.out.println("Oh no! Don't give up pls.. you still haven't found a gf yet :(");
                        break;

                    // Display a list of tasks that shows its completion and types
                    case LIST:
                        System.out.println("Take a look at ye DREAM goals for 2023");
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(i + 1 + "." + list.get(i));
                        }
                        break;

                    // Mark to complete the task, the second bracket will show a cross
                    case MARK:
                        list.get(index).toBeMarked();
                        break;

                    // Un-mark to redo the completion of the task, the cross will be
                    // removed from the second bracket
                    case UNMARK:
                        list.get(index).toBeUnmarked();
                        break;

                    // Add task of type (To do)

                    case TODO:
                        if (input.length() < 5) {
                            throw new TaskException("Please enter an to-do item");
                        }
                        list.add(new Todo(input.substring(5, input.length())));
                        System.out.println("Now you have " + list.size() + " tasks in the list.");
                        break;

                    // Add task of type (Deadline)
                    case DEADLINE:
                        if (!input.contains("/by")) {
                            throw new TaskException("Enter an valid item followed by a deadline");
                        }
                        String[] deadline_part = input.substring(9, input.length()).split("/by ");
                        list.add(new Deadline(deadline_part[0], deadline_part[1]));
                        break;

                    // Add task of type (Event)
                    case EVENT:
                        boolean from = input.contains("/from");
                        boolean to = input.contains("/to");
                        if ((!from || !to) || !(from && to)) {
                            throw new TaskException("Event item must include a start time and an end time");
                        }
                        String[] event_part = input.substring(6, input.length()).split("/from ");
                        String[] range = event_part[1].split("/to ");
                        list.add(new Event(event_part[0], range[0], range[1]));
                        break;

                    // Delete task from the list according to its numbering on the list
                    case DELETE:
                        Task temp = list.get(index);
                        list.remove(index);
                        System.out.println("The Duke has removed this task: " + temp);
                        System.out.println("Now you have " + list.size() + " in the list.");
                        break;

                    // default will throw an exception in case switch-case is unable to find
                    // instruction
                    default:
                        throw new TaskException("Sorry! Duke has no idea what it is as it is not an instruction");
                }

                writeToFile();

            }
        } catch (TaskException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Object pointing to null, please check code");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Check if the index is within the size of the array");
        }
    }

    /**
     * Part of the code extracted from https:/
     * /www.codejava.net/java-se/file-io/how-to-read-and-write-text-file-in-java
     */

    public static void loadFileData() {
        try {
            File file = new File("./data");
            if (file.exists()) {
                File txtFile = new File(DUKETXT);
                FileReader fileReader = new FileReader(txtFile);
                readToFile(fileReader);
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile() {
        try {
            FileWriter writer = new FileWriter(DUKETXT);
            for (Task t : list) {
                writer.write(t.toString());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readToFile(FileReader file) {
        try {
            BufferedReader reader = new BufferedReader(file);
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Task class: parent class of Deadline, Event, To do
class Task {
    private final String name;
    private boolean checkMark;

    public Task(String name) {
        this.name = name;
        this.checkMark = false;
    }

    public void toBeMarked() {
        this.checkMark = true;
    }

    public void toBeUnmarked() {
        this.checkMark = false;
    }

    @Override
    public String toString() {
        return (checkMark ? "[X] " : "[] ") + name;
    }
}

// To-do class returns result that is type [T]
class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

// Deadline class returns result that is type [D] and a deadline
class Deadline extends Task {
    private final LocalDate date;
    private final String time;
    private final String[] period;

    public Deadline(String name, String frame) {
        super(name);
        this.period = frame.split(" ");
        this.date = LocalDate.parse(period[0]);
        this.time = period[1];
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: " + date.getDayOfMonth() + " "
                + date.getMonth() + " " + date.getYear() + ", " + time + " )";
    }
}

// Event class returns result that is type [E] and a starting time and an ending
// time
class Event extends Task {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String startTime;
    private final String endTime;
    private final String[] startingPeriod;
    private final String[] endingPeriod;

    public Event(String name, String startingTime, String endTime) {
        super(name);
        startingPeriod = startingTime.split(" ");
        endingPeriod = endTime.split(" ");
        if (startingPeriod[0].contains("/")) {
            this.startDate = LocalDate.parse(startingPeriod[0].replaceAll("/", "-"));
        } else {
            this.startDate = LocalDate.parse(startingPeriod[0]);
        }

        if (endingPeriod[0].contains("/")) {
            this.endDate = LocalDate.parse(endingPeriod[0].replaceAll("/", "-"));
        } else {
            this.endDate = LocalDate.parse(endingPeriod[0]);
        }
        
        this.startTime = startingPeriod[1];
        this.endTime = endingPeriod[1];
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + "(from: " + startDate.getDayOfMonth() + " " +
                startDate.getMonth() + " " + startDate.getYear() + ", " + startTime + " to: " +
                endDate.getDayOfMonth() + " " + endDate.getMonth() + " " + endDate.getYear() + ", "
                + endTime + " )";
    }
}

// Exception that return a custom message that handles errors in task
// instructions
class TaskException extends Exception {
    public TaskException(String message) {
        super(message);
    }
}
