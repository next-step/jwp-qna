package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @DisplayName("answer 저장 테스트")
    @Test
    void answerSaveTest() {
        LocalDateTime now = LocalDateTime.now();
        Answer answer = new Answer(1L, 1L, "content");

        Answer savedAnswer = answerRepository.save(answer);

        assertAll(() -> {
            assertThat(savedAnswer.getId(), is(notNullValue()));
            assertThat(savedAnswer.getWriterId(), is(answer.getWriterId()));
            assertThat(savedAnswer.getQuestionId(), is(answer.getQuestionId()));
            assertThat(savedAnswer.getContents(), is(answer.getContents()));
            assertThat(savedAnswer.isDeleted(), is(answer.isDeleted()));
            assertTrue(savedAnswer.getCreatedDate().isAfter(now));
            assertTrue(savedAnswer.getModifiedDate().isAfter(now));
        });
    }
}
