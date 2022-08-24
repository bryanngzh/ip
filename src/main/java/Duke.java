import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class Duke {
    /** Using ArrayList to store Task */
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void printLine() {
        System.out.println("\t" + "____________________________________________________________");
    }

    public static void greet() {
        printLine();
        String logo = "\t" + "  ____        _        \n"
                + "\t" + " |  _ \\ _   _| | _____ \n"
                + "\t" + " | | | | | | | |/ / _ \\\n"
                + "\t" + " | |_| | |_| |   <  __/\n"
                + "\t" + " |____/ \\__,_|_|\\_\\___|\n";
        System.out.println("\t" + " Hello from\n" + logo);
        System.out.println("\t" +" Hello! I'm Duke\n");
        System.out.println("\t" +" What can I do for you?");
        printLine();
    }

    public static void exit() {
        printLine();
        System.out.println("\t" + " Bye. Hope to see you again soon!");
        printLine();
    }

    public static ToDo addTodo(String input) throws DukeException{
        if (input.length() == 0) {
            throw new DukeException("\t OOPS!!! The description of a todo cannot be empty.");
        }
        ToDo todo = new ToDo(input);
        return todo;
    }

    public static Deadline addDeadline(String input) throws DukeException {
        if (input.length() == 0) {
            throw new DukeException("\t OOPS!!! The description of a deadline cannot be empty.");
        }
        String[] inputArray = input.split(" /by ", 2);
        if (inputArray.length == 1) {
            throw new DukeException("\t OOPS!!! You need to add a deadline.");
        }
        String description = inputArray[0];
        String by = inputArray[1];
        Deadline deadline = new Deadline(description, LocalDate.parse(by));
        return deadline;
    }

    public static Event addEvent(String input) throws DukeException {
        if (input.length() == 0) {
            throw new DukeException("\t OOPS!!! The description of a event cannot be empty.");
        }
        String[] inputArray = input.split(" /at ", 2);
        if (inputArray.length == 1) {
            throw new DukeException("\t OOPS!!! You need to add a duration.");
        }
        String description = inputArray[0];
        String at = inputArray[1];
        Event event = new Event(description, LocalDate.parse(at));
        return event;
    }

    public static void addTask(String input, Commands type) throws DukeException {
        Task task = null;
        switch (type) {
            // To-do
            case TODO:
                task = addTodo(input);
                break;
            // Deadline
            case DEADLINE:
                task = addDeadline(input);
                break;
            // Event
            case EVENT:
                task = addEvent(input);
                break;
            default:
                break;
        }
        tasks.add(task);
        printLine();
        System.out.println("\t" + " Got it. I've added this task:");
        System.out.println("\t\t" + " " + task);
        if (tasks.size() == 1) {
            System.out.println(String.format("\t Now you have %d task in the list.", tasks.size()));
        } else {
            System.out.println(String.format("\t Now you have %d tasks in the list.", tasks.size()));
        }
        printLine();
    }

    public static void listTasks() throws DukeException {
        if (tasks.size() == 0) {
            throw new DukeException("\t You do not have any tasks.");
        }
        printLine();
        System.out.println("\t" + " Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t" + " " + (i + 1) + ". " + tasks.get(i));
        }
        printLine();
    }

    public static Task getTask(int index) throws DukeException {
        int numTasks = tasks.size();
        if (numTasks == 0) {
            throw new DukeException("\t You do not have any tasks.");
        } else if (index < 1) {
            throw new DukeException("\t Task number starts from one.");
        } else if (index > numTasks){
            if (numTasks == 1) {
                throw new DukeException(String.format("\t You only have %d task.", numTasks));
            } else {
                throw new DukeException(String.format("\t You only have %d tasks.", numTasks));
            }
        } else {
            return tasks.get(index - 1);
        }
    }

    public static void markAsDone(int taskNumber) throws DukeException {
        Task task = getTask(taskNumber);
        task.markAsDone();
        printLine();
        System.out.println("\t" + " Nice! I've marked this task as done:");
        System.out.println("\t " + task);
        printLine();
    }

    public static void markAsNotDone(int taskNumber) throws DukeException {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        printLine();
        System.out.println("\t" + " OK! I've marked this task as not done yet:");
        System.out.println("\t " + task);
        printLine();
    }

    public static void deleteTask(int taskNumber) throws DukeException {
        Task task = getTask(taskNumber);
        tasks.remove(taskNumber - 1);
        int numTasks = tasks.size();
        printLine();
        System.out.println("\t Noted. I've removed this task:");
        System.out.println("\t " + task);
        if (tasks.size() == 1) {
            System.out.println(String.format("\t Now you have %d task in the list.", tasks.size()));
        } else {
            System.out.println(String.format("\t Now you have %d tasks in the list.", tasks.size()));
        }
        printLine();
    }

    public static void listTasksOn(LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("E, d MMM yyyy"));
        ArrayList<Task> filteredTasks = tasks.stream().filter(task -> task.isOn(date))
                .collect(Collectors.toCollection(ArrayList::new));
        printLine();
        if (filteredTasks.size() > 0) {
            System.out.println(String.format("\t Here are the tasks you have on %s:", formattedDate));
            for (int i = 0; i < filteredTasks.size(); i++) {
                System.out.println("\t " + (i + 1) + ". " + filteredTasks.get(i));
            }
        } else {
            System.out.println(String.format("\t You have no tasks on %s", formattedDate));
        }
        printLine();
    }

    public static void startDuke() throws DukeException {

        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            try {
                String input = sc.nextLine();
                String[] inputArray = input.split(" ", 2);
                String firstWord = inputArray[0];
                String secondWord = "";
                if (inputArray.length == 2) {
                    secondWord = inputArray[1];
                }
                switch (firstWord) {
                    case "bye":
                        if (inputArray.length == 1) {
                            exit();
                            isRunning = false;
                        } else {
                            throw new DukeException("\t OOPS!!! I'm sorry, but I don't know what that means :-(");
                        }
                        break;
                    case "list":
                        if (inputArray.length == 1) {
                            listTasks();
                        } else {
                            throw new DukeException("\t OOPS!!! I'm sorry, but I don't know what that means :-(");
                        }
                        break;
                    case "mark":
                        if (secondWord.length() == 0) {
                            throw new DukeException("\t OOPS!!! You need to mark a number");
                        }
                        try {
                            markAsDone(Integer.parseInt(inputArray[1]));
                        } catch (NumberFormatException e) {
                            throw new DukeException("\t OOPS!!! You need to mark a number");
                        }
                        break;
                    case "unmark":
                        if (secondWord.length() == 0) {
                            throw new DukeException("\t OOPS!!! You need to unmark a number");
                        }
                        try {
                            markAsNotDone(Integer.parseInt((inputArray[1])));
                        } catch (NumberFormatException e) {
                            throw new DukeException("\t OOPS!!! You need to unmark a number");
                        }
                        break;
                    case "todo":
                        addTask(secondWord, Commands.TODO);
                        break;
                    case "deadline":
                        addTask(secondWord, Commands.DEADLINE);
                        break;
                    case "event":
                        addTask(secondWord, Commands.EVENT);
                        break;
                    case "delete":
                        if (secondWord.length() == 0) {
                            throw new DukeException("\t OOPS!!! You need to delete a number");
                        }
                        try {
                            deleteTask(Integer.parseInt(inputArray[1]));
                        } catch (NumberFormatException e) {
                            throw new DukeException("\t OOPS!!! You need to delete a number");
                        }
                        break;
                    case "on":
                        try {
                            listTasksOn(LocalDate.parse(secondWord));
                        } catch (DateTimeParseException e) {
                            throw new DukeException("\t Please enter a valid date (yyyy-mm-dd).");
                        }
                        break;
                    default:
                        throw new DukeException("\t OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException e) {
                printLine();
                System.out.println(e);
                printLine();
            }
        }
    }

    public static void main(String[] args) {
        try {
            greet();
            startDuke();
        } catch (DukeException e) {
            printLine();
            System.out.println(e);
            printLine();
        }
    }
}
