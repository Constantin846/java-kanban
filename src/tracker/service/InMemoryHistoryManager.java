package tracker.service;

import tracker.tasks.AbstractTask;
import java.util.ArrayList;
import java.util.HashMap;

class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> taskLinkedMap;
    private Node firstNode;
    private Node lastNode;

    public InMemoryHistoryManager() {
        taskLinkedMap = new HashMap<>();
        firstNode = null;
        lastNode = null;
    }

    @Override
    public void add(AbstractTask task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeById(id);
    }

    @Override
    public ArrayList<AbstractTask> getHistory() {
        return getTasks();
    }

    private ArrayList<AbstractTask> getTasks() {
        ArrayList<AbstractTask> tasks = new ArrayList<>();
        Node node = lastNode;

        while (node != null) {
            tasks.add(node.getTask());
            node = node.getPreviousNode();
        }
        return tasks;
    }

    private void linkLast(AbstractTask task) {
        int id = task.getId();
        Node node;

        if (taskLinkedMap.containsKey(id)) {
            node = taskLinkedMap.get(id);
            removeNode(node);
        }

        Node last = lastNode;
        node = new Node(lastNode, task, null);
        lastNode = node;

        if (last == null) {
            firstNode = node;
        } else {
            last.setNextNode(node);
        }

        taskLinkedMap.put(id, node);
    }

    private void removeById(int id) {
        if (taskLinkedMap.containsKey(id)) {
            removeNode(taskLinkedMap.get(id));
        }
    }

    private void removeNode(Node node) {
        Node previousNode = node.getPreviousNode();
        Node nextNode = node.getNextNode();

        if (previousNode != null) {
            previousNode.setNextNode(nextNode);
        } else {
            firstNode = nextNode;
        }

        if (nextNode != null) {
            nextNode.setPreviousNode(previousNode);
        } else {
            lastNode = previousNode;
        }
    }
}
