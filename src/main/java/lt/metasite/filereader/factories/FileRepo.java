package lt.metasite.filereader.factories;

public enum FileRepo {
    From_A_to_G('a', 'g'), From_H_to_N('h', 'n'), From_O_to_U('o', 'u'), From_V_to_Z('v', 'z');

    private char from, to;

    FileRepo(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }
}
