package qna.domain;

public enum ContentType {
    QUESTION(Values.QUESTION),
    ANSWER(Values.ANSWER);

    private final String value;

    ContentType(String value) {
        if (!this.name().equals(value)) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    /**
     * ContentType을 어노테이션에서 String 값으로 불러오기 위한 조치
     */
    public static class Values {
        public static final String QUESTION = "QUESTION";
        public static final String ANSWER = "ANSWER";
    }
}
