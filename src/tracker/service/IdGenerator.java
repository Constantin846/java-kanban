package tracker.service;

class IdGenerator {
    private static int idSequence = 0;

    public static int generateId() {
        return idSequence++;
    }
}
