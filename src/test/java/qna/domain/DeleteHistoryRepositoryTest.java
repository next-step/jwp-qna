package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private DeleteHistoryRepository deleteHistories;
    
    @Test
    @DisplayName("삭제이력 저장")
    void save() {
        // given
        users.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.JAVAJIGI, LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        // then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @Test
    @DisplayName("연관관계 매핑 테스트 - 삭제한 유저")
    void deleteUser() {
        // given
        User deleteUser = users.save(UserTest.JAVAJIGI);

        // when
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), deleteUser, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        // then
        assertThat(actual.deleteUser()).isSameAs(deleteUser);
    }
}