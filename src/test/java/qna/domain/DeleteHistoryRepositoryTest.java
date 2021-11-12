package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    UserRepository users;

    @Autowired
    DeleteHistoryRepository deleteHistories;

    User javajigi;
    DeleteHistory answerHistory;
    DeleteHistory questionHistory;

    @BeforeEach
    public void setUp() throws Exception {
        javajigi = users.save(new User("answerJavajigi", "password", "javajigi", "javajigi@slipp.net"));
        answerHistory = new DeleteHistory(ContentType.ANSWER, 1L, javajigi, LocalDateTime.now());
        questionHistory = new DeleteHistory(ContentType.QUESTION, 1L, javajigi, LocalDateTime.now());
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 ID not null 체크")
    void save() {
        // when
        DeleteHistory expect = deleteHistories.save(answerHistory);

        // then
        assertThat(expect.getId()).isNotNull();
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        DeleteHistory actual = deleteHistories.save(questionHistory);
        DeleteHistory expect = deleteHistories.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @CsvSource(value = {"QUESTION,ANSWER"})
    @DisplayName("nativeQuery 사용하여 ContentType 값 으로 조회")
    void findByContentType(ContentType QUESTION, ContentType ANSWER) {
        // given
        deleteHistories.save(questionHistory);
        deleteHistories.save(answerHistory);

        // when
        // then
        assertAll(
            () -> assertThat(deleteHistories.findByContentType(QUESTION)).isNotNull(),
            () -> assertThat(deleteHistories.findByContentType(ANSWER)).isNotNull()
        );
    }
}
