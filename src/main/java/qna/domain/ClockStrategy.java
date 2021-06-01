package qna.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class ClockStrategy implements DateTimeStrategy {

    private final Clock clock;

    public ClockStrategy(Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}
