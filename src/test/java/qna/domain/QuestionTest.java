package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionTest(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @ParameterizedTest
    @MethodSource("testQuestionList")
    void testEquals(Question question) {
        Question savedQuestion = questionRepository.save(question);
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle());
        assertThat(savedQuestion.getContents()).isEqualTo(question.getContents());
        assertThat(savedQuestion.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedQuestion.getUpdatedAt()).isNull();
    }

    private static List<Question> testQuestionList() {
        return Arrays.asList(Q1, Q2);
    }
}
