package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.TestAnswerFactory;
import qna.domain.TestDeleteHistoryFactory;
import qna.domain.TestQuestionFactory;
import qna.domain.TestUserFactory;
import qna.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAllInBatch();
    }

    @Test
    void deleteHistory_save_test() {
        // given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);
        DeleteHistory deleteHistory = TestDeleteHistoryFactory.create(ContentType.ANSWER, answer.getId(), answer.getUser());
        // when
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        // then
        assertThat(saveDeleteHistory.getId()).isNotNull();
    }

    @Test
    void retreive_deleteHistory_test() {
        // given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);
        DeleteHistory deleteHistory1 = TestDeleteHistoryFactory.create(ContentType.ANSWER, answer.getId(), answer.getUser());
        DeleteHistory deleteHistory2 = TestDeleteHistoryFactory.create(ContentType.ANSWER, answer.getId(), answer.getUser());
        // when
        deleteHistoryRepository.save(deleteHistory1);
        deleteHistoryRepository.save(deleteHistory2);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        // then
        assertThat(deleteHistories).hasSize(2);
    }
}
