package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.DeleteHistoryTest;
import qna.domain.QuestionRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save() {
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        assertThat(actual).isEqualTo(DeleteHistoryTest.DH1);
    }
}
