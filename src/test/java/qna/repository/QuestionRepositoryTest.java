package qna.repository;

import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    EntityManager entityManager;
    public User writer;
    public Question saveQuestion;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.TESTER);
        Question question = new Question("title", "contents").writeBy(writer);
        saveQuestion = questionRepository.save(question);
    }

    @Test
    @DisplayName("Question 저장한 엔티티의 id로 조회한 경우 동일성 테스트")
    void find() {
        Question findQuestion = questionRepository.findById(saveQuestion.getId()).orElse(null);
        assertThat(saveQuestion).isEqualTo(findQuestion);
    }

    @DisplayName("findByDeletedFalse 검증")
    @Test
    void findByDeletedFalse() {
        List<Question> question = questionRepository.findByDeletedFalse();
        saveQuestion.setDeleted(true);
        List<Question> deletedQuestion = questionRepository.findByDeletedFalse();
        assertAll(
                () -> Assertions.assertThat(question).hasSize(1),
                () -> Assertions.assertThat(deletedQuestion).isEmpty()
        );
    }

    @DisplayName("작성자 수정 검증")
    @Test
    void setWriter() {
        User otherWriter = userRepository.save(UserTest.SANJIGI);
        Question updateQuestion = saveQuestion.writeBy(otherWriter);
        assertAll(
                () -> assertEquals(updateQuestion.getWriter(), otherWriter),
                () -> assertEquals(questionRepository.findById(updateQuestion.getId()).orElse(null).getWriter(),
                        otherWriter)
        );
    }

    @DisplayName("질문삭제시 답변도 삭제 검증")
    @Test
    void deleted_test() {
        new Answer(writer, saveQuestion, "Answers test1");
        new Answer(writer, saveQuestion, "Answers test2");
        questionRepository.save(saveQuestion);
        flush();

        saveQuestion.delete(writer);
        assertThat(answerRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("question 삭제 상태변경 검증")
    void question_set_delete() {
        saveQuestion.delete(writer);
        Assertions.assertThat(saveQuestion.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("동일한 유저 아닌경우 예외발생")
    void question_delete_user_valid() {
        saveQuestion.delete(writer);
        Assertions.assertThat(saveQuestion.isDeleted()).isTrue();
        assertThatThrownBy(() -> saveQuestion.delete(User.GUEST_USER))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("삭제할 권한");
    }

    void flush() {
        entityManager.flush();
        entityManager.clear();
    }

}
