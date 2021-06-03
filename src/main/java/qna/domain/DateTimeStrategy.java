package qna.domain;

import java.time.LocalDateTime;

@FunctionalInterface
public interface DateTimeStrategy {

    LocalDateTime now();
}
