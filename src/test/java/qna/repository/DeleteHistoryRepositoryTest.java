package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    private User writer;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAllInBatch();
        writer = User.create("gerrad", "password", "humba", "gerrad@liverpool.uk");
        question = Question.create("title", "contents", writer);
        answer = Answer.create(writer, question, "contents");
    }

    @Test
    void deleteHistory_save_test() {
        // given
        DeleteHistory deleteHistory = DeleteHistory.create(ContentType.ANSWER, answer.getId(), answer.getWriter());
        // when
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        // then
        assertThat(saveDeleteHistory.getId()).isNotNull();
    }

    @Test
    void retreive_deleteHistory_test() {
        // given
        DeleteHistory deleteHistory1 = DeleteHistory.create(ContentType.ANSWER, answer.getId(), answer.getWriter());
        DeleteHistory deleteHistory2 = DeleteHistory.create(ContentType.ANSWER, answer.getId(), answer.getWriter());
        // when
        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        // then
        assertThat(deleteHistories).hasSize(2);
    }
}
