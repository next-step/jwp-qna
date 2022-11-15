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

        final User user1 = users.save(new User(1L, "user1", "qwerty", "P", "P@test.com"));
        final Question question1 = questions.save(new Question(1L, "title1", "contents1").writeBy(user1));

        final DeleteHistory deleted = new DeleteHistory(ContentType.QUESTION, question1.getId(), user1, LocalDateTime.now());
        final DeleteHistory result = deleteHistories.save(deleted);

        assertThat(result).isNotNull();
    }

}
