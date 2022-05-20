package qna.domain;

public enum ContentType {
    QUESTION("QUESTION"),
    ANSWER("ANSWER"),
    ;

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
