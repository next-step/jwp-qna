package qna.domain;

public class TestDeleteHistoryFactory {

    private TestDeleteHistoryFactory() {

    }

    public static DeleteHistory create(ContentType contentType, Long contentId, User deletedBy) {
        return new DeleteHistory(contentType, contentId, deletedBy);
    }
}
