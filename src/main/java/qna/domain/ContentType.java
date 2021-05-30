package qna.domain;

public enum ContentType {
    QUESTION("question"), ANSWER("answer");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
