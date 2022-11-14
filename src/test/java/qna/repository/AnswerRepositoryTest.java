package qna.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.TestFixture;
import qna.domain.User;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    EntityManager entityManager;

    public Answer answer;
    public Question question;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title", "contents").writeBy(user));
        answer = new Answer(user, question, "Answers Contents1");
    }

    @Test
    @DisplayName("Answer 저장한 및 조회 테스트")
    void find() {
        answerRepository.save(answer);
        flush();
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 검증")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        Answer saveAnswer = answerRepository.save(answer);
        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(question);
        saveAnswer.setDeleted(true);
        List<Answer> deletedAnswers = answerRepository.findByQuestionAndDeletedFalse(question);
        assertAll(
                () -> assertThat(answers).hasSize(1),
                () -> assertThat(deletedAnswers).isEmpty()
        );
    }

    @Test
    @DisplayName("question lazy 로딩 검증")
    void answer_to_question_lazy() {
        answerRepository.save(answer);
        flush();

        Answer dbAnswer = answerRepository.findById(answer.getId()).orElse(null);
        Question dbQuestion = dbAnswer.getQuestion();
        assertThat(Hibernate.isInitialized(dbQuestion)).isFalse();
    }

    @Test
    @DisplayName("answer 삭제 상태변경 검증")
    void answer_set_delete() throws Exception {
        Answer answerTest = answerRepository.save(answer);
        DeleteHistory deleteHistory = answerTest.delete(answer.getWriter());
        deleteHistoryRepository.save(deleteHistory);

        assertThat(answerTest.isDeleted()).isTrue();
        assertThat(deleteHistoryRepository.findByDeletedBy(answer.getWriter())).isNotEmpty();
    }

    @Test
    @DisplayName("다른사람이 작성한 경우 예외발생")
    void answer_delete_user_valid() {
        Answer answerTest = TestFixture.createAnswer(UserTest.JAVAJIGI, question);
        assertThatThrownBy(() -> answerTest.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람");
    }

    void flush() {
        entityManager.flush();
        entityManager.clear();
    }

}
