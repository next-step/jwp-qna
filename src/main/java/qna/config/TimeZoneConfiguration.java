package qna.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfiguration {

    @Bean
    public Clock systemDefaultClock() {
        return Clock.systemDefaultZone();
    }
}
