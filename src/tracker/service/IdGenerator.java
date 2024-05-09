package tracker.service;

class IdGenerator {
    private int idSequence = 0;

    public void setIdSequence(int id) {
        if (idSequence <= id) {
            idSequence = id + 1;
        }
    }

    public int generateId() {
        return idSequence++;
    }
}
