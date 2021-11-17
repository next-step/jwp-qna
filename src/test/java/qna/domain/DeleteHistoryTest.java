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
    Question question1;
    User user1;

    @BeforeEach
    void init() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        question1 = new Question("title1", "contents1").writeBy(user1);
        userRepository.save(user1);
        Question savedQuestion = questionRepository.save(question1);
        deleteHistory = new DeleteHistory(ContentType.QUESTION, savedQuestion.getId(), user1, LocalDateTime.now());
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
    void 연관관계_유저_조회() {
        user1.addDeleteHistory(savedDeleteHistory);
        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
        assertThat(foundDeleteHistory.getDeletedByUser().getId()).isEqualTo(user1.getId());
    }

    @Test
    void 삭제() {
        deleteHistoryRepository.delete(savedDeleteHistory);
        assertThat(deleteHistoryRepository.findById(savedDeleteHistory.getId()).isPresent()).isFalse();
    }
}
