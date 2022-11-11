package qna.domain;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class ClockDateTimeStrategy implements DateTimeStrategy {
    @Override
    public LocalDateTime getNowDateTime() {
        return LocalDateTime.now(Clock.systemDefaultZone());
    }
}
