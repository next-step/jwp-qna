package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private TestEntityManager em;

    private User writer;

    private Question question;

    private Answer answer;

    @BeforeEach
    void setUp() {
        writer = UserTest.userSample(null);
        em.persist(writer);

        question = QuestionTest.questionSample(null, writer);
        em.persist(question);

        answer = AnswerTest.answerSample(null, writer, question);
        em.persist(answer);
    }

    @Test
    @DisplayName("주어진 질문으로 삭제 이력을 생성한다")
    void save_delete_history_with_answer_test() {
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);
        deleteHistoryRepository.save(deleteHistory);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 답변으로 삭제 이력을 생성한다")
    void save_delete_history_with_question_test() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
        deleteHistoryRepository.save(deleteHistory);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("주어진 질문 ID 값으로 삭제 이력을 조회한다")
    void find_by_question_id_test() {
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofQuestion(question);
        deleteHistoryRepository.save(expectedDeleteHistory);

        DeleteHistory deleteHistory = deleteHistoryRepository.findByContentId(question.getId());

        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }

    @Test
    @DisplayName("주어진 답변 ID 값으로 삭제 이력을 조회한다")
    void find_by_answer_id_test() {
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofAnswer(answer);
        deleteHistoryRepository.save(expectedDeleteHistory);

        DeleteHistory deleteHistory = deleteHistoryRepository.findByContentId(answer.getId());

        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }

    @Test
    @DisplayName("주어진 삭제자로 삭제 이력을 조회한다")
    void find_by_deleter_test() {
        DeleteHistory expectedDeleteHistory = DeleteHistory.ofAnswer(answer);
        deleteHistoryRepository.save(expectedDeleteHistory);

        DeleteHistory deleteHistory = deleteHistoryRepository.findByDeleter(answer.writer());

        assertThat(deleteHistory).isEqualTo(expectedDeleteHistory);
    }
}
