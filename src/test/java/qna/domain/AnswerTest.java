package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    public User javajigi;
    public User sanjigi;

    public Question questionJavajigi;
    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
        questionJavajigi = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
    }

    @Test
    void save() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "Answers Contents1"));
        assertThat(answer.getWriter()).isEqualTo(sanjigi);
    }

    @Test
    public void findById() {
        final Answer expected = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        final Answer answer = answerRepository.findById(expected.getId())
                .orElseThrow(NotFoundException::new);
        assertThat(expected).isEqualTo(answer);
    }

    @Test
    @DisplayName("질문에 대한 답변 조회")
    public void getAnswersWithQuestion() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        assertThat(questionJavajigi.getAnswers()).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answer.setDeleted(true);
        assertThatThrownBy(() -> answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "answer 111"));
        answer.setDeleted(true);
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(questionJavajigi.getId())).hasSize(0);
    }
}
