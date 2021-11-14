package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    Answer savedAnswer;

    @BeforeEach
    void init() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        savedAnswer = answerRepository.save(A1);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents())
        );
    }

    @Test
    void 검색() {
        assertThat(answerRepository.findById(savedAnswer.getId()).get().getId())
                .isEqualTo(savedAnswer.getId());
    }

    @Test
    void 연관관계_유저() {
        UserTest.JAVAJIGI.addAnswer(A1);
        assertAll(
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(UserTest.JAVAJIGI),
                () -> assertThat(UserTest.JAVAJIGI.getAnswers().get(0)).isEqualTo(savedAnswer)
        );
    }

    @Test
    void 연관관계_질문() {
        Question question = questionRepository.save(QuestionTest.Q1);
        question.addAnswer(A1);
        assertAll(
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(question),
                () -> assertThat(question.getAnswers().get(0)).isEqualTo(savedAnswer)
        );
    }

    @Test
    void 검색_없을경우() {
        A1.setDeleted(true);
        questionRepository.save(QuestionTest.Q1);
        assertThat(answerRepository.findByIdAndDeletedFalse(A1.getId()).isPresent()).isFalse();
    }

}
