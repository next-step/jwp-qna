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
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @DisplayName("question 저장 테스트")
    @Test
    void questionSaveTest() {
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question("title", "contents", 1L, false);

        Question savedQuestion = questionRepository.save(question);

        assertAll(() -> {
            assertThat(savedQuestion.getId(), is(notNullValue()));
            assertThat(savedQuestion.getTitle(), is(question.getTitle()));
            assertThat(savedQuestion.getContents(), is(question.getContents()));
            assertThat(savedQuestion.getWriterId(), is(question.getWriterId()));
            assertThat(savedQuestion.isDeleted(), is(question.isDeleted()));
            assertTrue(savedQuestion.getCreatedAt().isEqual(now) || savedQuestion.getCreatedAt().isAfter(now));
            assertTrue(savedQuestion.getUpdatedAt().isEqual(now) || savedQuestion.getUpdatedAt().isAfter(now));
        });
    }

}
