package tracker.service.history;

import tracker.tasks.AbstractTask;

class Node {
    private Node previousNode;
    private final AbstractTask task;
    private Node nextNode;

    public Node(Node previousNode, AbstractTask task, Node nextNode) {
        this.previousNode = previousNode;
        this.task = task;
        this.nextNode = nextNode;
    }

    public AbstractTask getTask() {
        return task;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
