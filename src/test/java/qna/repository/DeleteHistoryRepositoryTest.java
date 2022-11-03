package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void setUp() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("삭제이력을 저장한후에 인스턴스의 id가 반환")
    void test_returns_answers_with_questionId_and_deleted_is_false() {
        DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(),
                UserTest.JAVAJIGI.getId(),
                LocalDateTime.now());
        DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);

        assertThat(deleteHistory).isNotNull();
    }
}


