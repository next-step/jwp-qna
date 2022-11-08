package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Test
    @DisplayName("DeleteHistory 생성 및 저장 테스트")
    void DeleteHistory_save_test() {
        final Question q1 = questions.save(QuestionTest.Q1);
        final User user = users.save(UserTest.SANJIGI);

        final DeleteHistory deleted = new DeleteHistory(ContentType.QUESTION, q1.getId(), user.getId(), LocalDateTime.now());
        final DeleteHistory result = deleteHistories.save(deleted);

        assertThat(result).isNotNull();
    }

}
