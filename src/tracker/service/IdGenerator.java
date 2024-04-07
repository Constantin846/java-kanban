package tracker.service;

class IdGenerator {
    private static int idSequence = 0;

    private IdGenerator() {
    }

    public static void setIdSequence(int id) {
        if (idSequence <= id) {
            idSequence = id + 1;
        }
    }

    public static int generateId() {
        return idSequence++;
    }
}
