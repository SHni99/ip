package ui;
import java.util.Scanner;

import exceptions.TaskException;

public class Ui {

    private Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void welcomeMessage() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println("Hello I'm \n" + logo);

        System.out.println("What can I do for you?\n");
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void byeMessage() {
        System.out.println("Oh no! Pls don't leave me.. I'm your ONLY friend.. Rmb?? :(");
    }

    public void listMessage() {
        System.out.println("Take a look at ye DREAM goals for 2023");
    }

    public void listInfo(int size) {
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void error(String code) throws TaskException {
        switch (code) {
            case "todo":
            throw new TaskException("Please enter an to-do item");

            case "deadline":
            throw new TaskException("Enter an valid item followed by a deadline");

            case "event":
            throw new TaskException("Event item must include a start time and an end time");

            default:
            throw new TaskException("Sorry! Duke has no idea what it is as it is not an instruction");
        
        }
    }

    public void showLine() {
        System.out.println("_________________________________________");
    }

}