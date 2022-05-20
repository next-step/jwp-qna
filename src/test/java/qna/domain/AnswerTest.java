package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.UserRepository;
import qna.exception.CannotDeleteException;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("작성자가 본인인 경우 답변을 삭제한다.")
    @Test
    void delete() throws CannotDeleteException {
        long answerId = 1L;
        long writerId = 1L;
        Answer answer = answerRepository.findById(answerId).get();
        User writer = userRepository.findById(writerId).get();

        answer.delete(writer);
        entityManager.flush();
        entityManager.clear();

        Answer actual = answerRepository.findById(answerId).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    @DisplayName("작성자가 본인인 아닌 경우 답변을 삭제 할 수 없다.")
    @Test
    void delete_not_writer(){
        long answerId = 1L;
        long loginUserId = 2L;
        Answer answer = answerRepository.findById(answerId).get();
        User loginUser = userRepository.findById(loginUserId).get();

        assertThatThrownBy(()-> answer.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("[ERROR] 작성자가 아닌 경우 삭제할 수 없습니다.");

    }
}
