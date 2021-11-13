package qna.fixture;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

public class DeleteHistoryFixture {
    private DeleteHistoryFixture() {
        throw new UnsupportedOperationException();
    }

    public static DeleteHistory create(ContentType contentType, Long id, User deletedBy) {
        return new DeleteHistory(contentType, id, deletedBy);
    }
}
