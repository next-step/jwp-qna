package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    DeleteHistory deleteHistory;
    DeleteHistory savedDeleteHistory;

    @BeforeEach
    void init() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        deleteHistory = new DeleteHistory(ContentType.QUESTION, savedQuestion.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
        savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getId()).isEqualTo(deleteHistory.getId())
        );
    }

    @Test
    void 검색() {
        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
        assertThat(foundDeleteHistory).isEqualTo(deleteHistory);
    }

    @Test
    void 연관관계_유저() {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        savedUser.addDeleteHistory(savedDeleteHistory);
        em.flush();
        em.clear();
        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
        assertThat(foundDeleteHistory.getDeletedByUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void 삭제() {
        deleteHistoryRepository.delete(savedDeleteHistory);
        assertThat(deleteHistoryRepository.findById(savedDeleteHistory.getId()).isPresent()).isFalse();
    }

}
