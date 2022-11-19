package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.fixtures.UserTestFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        User writer = userRepository.save(UserTestFixture.손상훈);
        question = questionRepository.save(new Question(writer, "title1", "contents1"));
        answer = answerRepository.save(new Answer(writer, question, "content1"));
    }

    @Test
    @DisplayName("주어진 질문으로 삭제 이력을 생성한다")
    void save_delete_history_with_answer_test() {
        // given
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);
        // when
        deleteHistoryRepository.save(deleteHistory);
        // then
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변으로 삭제 이력을 생성한다")
    void save_delete_history_with_question_test() {
        // given
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
        // when
        deleteHistoryRepository.save(deleteHistory);
        // then
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 질문 ID 값으로 삭제 이력을 조회한다")
    void find_by_question_id_test() {
        // given
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofQuestion(question);
        deleteHistoryRepository.save(expectedDeleteHistory);
        // when
        DeleteHistory deleteHistory = deleteHistoryRepository.findByContentId(question.getId());
        // then
        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }

    @Test
    @DisplayName("주어진 답변 ID 값으로 삭제 이력을 조회한다")
    void find_by_answer_id_test() {
        // given
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofAnswer(answer);
        deleteHistoryRepository.save(expectedDeleteHistory);
        // when
        DeleteHistory deleteHistory = deleteHistoryRepository.findByContentId(answer.getId());
        // then
        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }

    @Test
    @DisplayName("주어진 삭제자로 삭제 이력을 조회한다")
    void find_by_deleter_test() {
        // given
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofAnswer(answer);
        deleteHistoryRepository.save(expectedDeleteHistory);
        // when
        DeleteHistory deleteHistory = deleteHistoryRepository.findByDeleter(answer.writer());
        // then
        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }
}
