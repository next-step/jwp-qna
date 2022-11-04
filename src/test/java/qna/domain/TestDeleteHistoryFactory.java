package qna.domain;

import java.time.LocalDateTime;

public class TestDeleteHistoryFactory {

    public static DeleteHistory create(ContentType contentType, Long contentId, User writer) {
        return new DeleteHistory(contentType, contentId, writer, LocalDateTime.now());
    }
}
