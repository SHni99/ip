package tasks;

// To-do class returns result that is type [T]
public class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}