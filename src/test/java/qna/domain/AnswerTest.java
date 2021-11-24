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
@DisplayName("답변 테스트")
public class AnswerTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private User javajigi;
    private User sanjigi;

    private Question questionJavajigi;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
        questionJavajigi = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
    }

    @Test
    @DisplayName("답변 저장")
    void save() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        assertThat(answer.isOwner(sanjigi)).isTrue();
    }

    @Test
    @DisplayName("답변ID로 답변 조회")
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
        answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        assertThat(questionJavajigi.getAnswers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("답변 ID로 조회시 삭제된 답변은 조회되지 않음")
    void findByIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "Answers Contents1"));
        answerRepository.flush();
        answer.delete(true);
        assertThatThrownBy(() -> answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("질문에 대한 답변 조회시 삭제된 답변은 조회되지 않음")
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(new Answer(sanjigi, questionJavajigi, "answer 111"));
        answerRepository.flush();
        answer.delete(true);
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(questionJavajigi.getId()).size()).isEqualTo(0);
    }

    @Test
    void 질문자와_답변자가_동일인() {
        final Answer answer = answerRepository.save(new Answer(javajigi, questionJavajigi, "answer 111"));
        answerRepository.flush();
        assertThat(answer.isOwner(questionJavajigi.getWriter())).isTrue();
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
