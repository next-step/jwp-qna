package qna.domain;

import java.time.LocalDateTime;

public class TestDeleteHistoryFactory {
    public static DeleteHistory create(ContentType contentType, Long contentId, User user) {
        return new DeleteHistory(contentType, contentId, user, LocalDateTime.now());
    }
}
