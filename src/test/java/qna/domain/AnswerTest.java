package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static Answer A1;
    public static Answer A2;
    private Question savedQ1;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer savedAnswer1;
    private Answer savedAnswer2;

    @BeforeEach
    void setUp() {
        savedQ1 = questionRepository.save(QuestionTest.Q1);
        A1 = new Answer(UserTest.JAVAJIGI, savedQ1, "Answers Contents1");
        A2 = new Answer(UserTest.JAVAJIGI, savedQ1, "Answers Contents1");
        savedAnswer1 = answerRepository.save(A1);
        savedAnswer2 = answerRepository.save(A2);
    }

    @Test
    @DisplayName("Answer 저장하는 테스트")
    void saveAnswerTest() {
        assertAll(
                () -> assertThat(savedAnswer1.getId()).isEqualTo(A1.getId()),
                () -> assertThat(savedAnswer2.getId()).isEqualTo(A2.getId())
        );
    }

    @Test
    @DisplayName("Question의 Answer을 찾는 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(savedQ1.getId());
        assertThat(answers).contains(A1, A2);
    }
}
