package qna.domain;

public enum Status {
    PUBLISHED, DELETED;

    public boolean isDeleted() {
        return this == DELETED;
    }

}
