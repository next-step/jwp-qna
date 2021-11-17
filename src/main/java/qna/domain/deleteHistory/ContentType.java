package qna.domain.deleteHistory;

public enum ContentType {
    QUESTION("질문"), ANSWER("답변");

    public final String contentTypeName;

    ContentType(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getContentTypeName() {
        return contentTypeName;
    }
}
