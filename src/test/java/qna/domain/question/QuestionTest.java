package qna.domain.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;
import qna.domain.history.DeleteHistory;
import qna.domain.history.DeleteHistoryRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

@DirtiesContext
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question createQuestion(User writeUser) {
        return new Question("title", "contents").writeBy(writeUser);
    }

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("질문이 삭제되는지 테스트한다")
    void delete_question() throws CannotDeleteException {
        User user = userRepository.save(UserTest.createUser("user"));
        Question question = createQuestion(user);
        question.delete(user);

        Question save = questionRepository.save(question);
        Question byId = questionRepository.findById(save.getId()).get();

        assertThat(byId.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 등록자와 로그인한 사용자가 다른 경우 CannotDeleteException 발생")
    void delete_question_exception_CannotDeleteException() {
        User writerUser = userRepository.save(UserTest.createUser("writerUser"));
        User loginUser = userRepository.save(UserTest.createUser("loginUser"));
        Question question = createQuestion(writerUser);

        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제 후 이력이 등록되는지 테스트한다")
    void save_delete_question_history() throws CannotDeleteException {
        User user = userRepository.save(UserTest.createUser("user"));
        Question question = createQuestion(user);
        DeleteHistory deleteQuestionHistory = question.delete(user);
        deleteHistoryRepository.save(deleteQuestionHistory);

        List<DeleteHistory> byDeletedByUser = deleteHistoryRepository.findByDeletedByUser(user);

        assertThat(byDeletedByUser).contains(deleteQuestionHistory);
    }

}
