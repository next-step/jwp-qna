package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    @DisplayName("Question을 생성한다")
    @Test
    void testCreate() {
        String title = "title1";
        String contents = "contents1";
        Question question = new Question(title, contents);
        assertAll(
            () -> assertThat(question.getTitle()).isEqualTo(title),
            () -> assertThat(question.getContents()).isEqualTo(contents),
            () -> assertThat(question.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
            () -> assertThat(question.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("Question에 작성자를 입력한다")
    @Test
    void testWriteBy() {
        Question question = new Question("title1", "contents1");
        User writer = new User("user1", "1234", "userName", "email");
        question.writeBy(writer);
        assertThat(question.getWriterId()).isEqualTo(writer.getId());
    }
}
