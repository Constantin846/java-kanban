package tracker.service;

import tracker.tasks.AbstractTask;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    @Override
    public void add(AbstractTask task) {
        TaskHistoryList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        TaskHistoryList.removeById(id);
    }

    @Override
    public ArrayList<AbstractTask> getHistory() {
        return TaskHistoryList.getTasks();
    }

    static class TaskHistoryList {
        private static final HashMap<Integer, Node> taskLinkedMap = new HashMap<>();
        private static Node firstNode;
        private static Node lastNode;

        public static ArrayList<AbstractTask> getTasks() {
            ArrayList<AbstractTask> tasks = new ArrayList<>();
            Node node = lastNode;

            while (node != null) {
                tasks.add(node.getTask());
                node = node.getPreviousNode();
            }
            return tasks;
        }

        public static void linkLast(AbstractTask task) {
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

        public static void removeById(int id) {
            if (taskLinkedMap.containsKey(id)) {
                removeNode(taskLinkedMap.get(id));
            }
        }

        private static void removeNode(Node node) {
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
}
