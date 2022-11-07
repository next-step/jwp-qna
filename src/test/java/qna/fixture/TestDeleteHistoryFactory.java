package qna.fixture;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import java.time.LocalDateTime;

public class TestDeleteHistoryFactory {
    public static DeleteHistory create(User deleteBy) {
        return DeleteHistory.createQuestion(1L, deleteBy, LocalDateTime.now());
    }
}
