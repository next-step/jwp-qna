package qna.domain.timegenerator;

import java.time.LocalDateTime;

public class TimeGeneratorImpl implements TimeGenerator{
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
