package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
