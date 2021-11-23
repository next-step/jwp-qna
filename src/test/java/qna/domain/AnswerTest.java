package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        questionJavajigi = questionRepository.save(QuestionFixture.질문().writeBy(javajigi));
        userRepository.flush();
        questionRepository.flush();
    }

    @Test
    void save() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        assertThat(answer.isOwner(sanjigi)).isTrue();
    }

    @Test
    public void findById() {
        final Answer expected = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        final Answer answer = answerRepository.findById(expected.getId())
                .orElseThrow(NotFoundException::new);
        assertThat(expected).isEqualTo(answer);
    }

    @Test
    @DisplayName("질문에 대한 답변 조회")
    public void getAnswersWithQuestion() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        assertThat(questionJavajigi.getAnswers().size()).isEqualTo(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        answer.setDeleted(true);
        assertThatThrownBy(() -> answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "answer 111"));
        answerRepository.flush();
        answer.setDeleted(true);
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(questionJavajigi.getId()).size()).isEqualTo(0);
    }

    @Test
    void 질문자와_답변자가_동일인() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "answer 111"));
        answerRepository.flush();
        assertThat(answer.isOwner(questionJavajigi.getWriter())).isTrue();
    }

    @Test
    void 답변_조회() {
        final Answer answerJavajigi = answerRepository.save(new Answer(javajigi, questionJavajigi, "answer 111"));
        final Answer answerSanjigi = answerRepository.save(new Answer(sanjigi, questionJavajigi, "answer 111"));
        answerRepository.flush();
        assertThat(questionJavajigi.getAnswers().size()).isEqualTo(2);
    }

    @AfterEach
    public void tearDown() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
        answerRepository.flush();
        questionRepository.flush();
        userRepository.flush();
    }
}
