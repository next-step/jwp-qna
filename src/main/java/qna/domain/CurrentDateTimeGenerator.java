package qna.domain;

import java.time.LocalDateTime;

public class CurrentDateTimeGenerator implements DateTimeGenerator {
    @Override
    public LocalDateTime generateDateTime() {
        return LocalDateTime.now();
    }
}
