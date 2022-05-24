package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        assertThat(actual).isEqualTo(DeleteHistoryTest.DH1);
    }

    @Test
    void findDeleteHistoryByDeletedById() {
        save();
        DeleteHistory expected = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        List<DeleteHistory> actual = deleteHistoryRepository.findByDeletedById(UserTest.JAVAJIGI);
        assertThat(actual.get(0)).isEqualTo(expected);
    }
}
