package qna.domain;

public class DeleteHistoryTestFactory {
    public static DeleteHistory create(ContentType contentType, Long id, Long deletedById) {
        return new DeleteHistory(contentType, id, deletedById);
    }
}
