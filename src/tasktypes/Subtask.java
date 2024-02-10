package tasktypes;

public class Subtask extends Task {
    private Epic topEpic;

    public Subtask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public Epic getTopEpic() { return topEpic; }

    public void setTopEpic(Epic epic) {
        topEpic = epic;
        topEpic.addSubtask(this);
    }

    void changeTopEpic(Epic epic) { topEpic = epic; }

    public void removeFromTopEpic() { topEpic.removeSubtask(this); }
}
