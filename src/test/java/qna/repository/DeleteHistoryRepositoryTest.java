package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("삭제 이력을 저장 후 확인")
    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        DeleteHistory deleteHistory = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));

        Optional<DeleteHistory> result = deleteHistoryRepository.findById(deleteHistory.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result).get().isEqualTo(deleteHistory)
        );
    }

    @DisplayName("삭제 이력을 저장 후 조회 확인")
    @Test
    void findAll() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        DeleteHistory deleteHistory1 = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
        DeleteHistory deleteHistory2 = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.ANSWER, answer.getId(), user, LocalDateTime.now()));

        List<DeleteHistory> result = deleteHistoryRepository.findAll();

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(deleteHistory1, deleteHistory2)
        );
    }

    @DisplayName("삭제 이력을 저장 후 삭제 확인")
    @Test
    void remove() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        DeleteHistory deleteHistory = deleteHistoryRepository.save(
            new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
        deleteHistoryRepository.delete(deleteHistory);

        Optional<DeleteHistory> result = deleteHistoryRepository.findById(deleteHistory.getId());

        assertThat(result).isNotPresent();
    }
}