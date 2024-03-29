package tracker.tasks;

public class Subtask extends AbstractTask {
    private Epic topEpic;

    public Subtask(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public Epic getTopEpic() { return topEpic; }

    public void setTopEpic(Epic epic) {
        topEpic = epic;
        topEpic.addSubtask(this);
    }

    public void removeFromTopEpic() { topEpic.removeSubtask(this); }

    void changeTopEpic(Epic epic) { topEpic = epic; }
}
