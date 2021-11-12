package qna.domain;

public class DeleteHistoryTestFactory {
    public static DeleteHistory create(ContentType contentType, Long id, User deletedBy) {
        return new DeleteHistory(contentType, id, deletedBy);
    }
}
