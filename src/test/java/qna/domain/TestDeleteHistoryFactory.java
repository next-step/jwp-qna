package qna.domain;

import java.time.LocalDateTime;

public class TestDeleteHistoryFactory {

    private TestDeleteHistoryFactory() {

    }

    public static DeleteHistory create(ContentType contentType, Long contentId, User deletedBy) {
        return new DeleteHistory(contentType, contentId, deletedBy, LocalDateTime.now());
    }
}
