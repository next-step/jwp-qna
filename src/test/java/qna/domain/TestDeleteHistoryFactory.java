package qna.domain;

public class TestDeleteHistoryFactory {

    public static DeleteHistory create(ContentType contentType, Long contentId, User writer) {
        return DeleteHistory.createDeleteHistory(contentType, contentId, writer);
    }
}
