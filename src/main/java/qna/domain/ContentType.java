package qna.domain;

public enum ContentType {
    QUESTION, ANSWER;

    public boolean isQuestion() {
        return this == QUESTION;
    }

    public boolean isAnswer() {
        return this == ANSWER;
    }
}
