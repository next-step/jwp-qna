package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.AnswerTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    UserRepository userRepository;
    User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @Test
    @DisplayName("삭제이력을 저장한후에 인스턴스의 id가 반환")
    void test_returns_answers_with_questionId_and_deleted_is_false() {
        DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), savedUser, LocalDateTime.now());

        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);

        assertThat(deleteHistory).isNotNull();
    }
}


