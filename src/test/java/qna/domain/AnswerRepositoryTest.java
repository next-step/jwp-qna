package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    private Answer answer;

    private Answer actual;

    @BeforeEach
    void setUp() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        actual = repository.save(answer);
    }

    @Test
    @DisplayName("정상적으로 전 후 데이터가 들어가 있는지 확인한다.")
    void save() {
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNull(),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(actual.isDeleted()).isEqualTo(answer.isDeleted()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(answer.getQuestionId()),
            () -> assertThat(actual.getWriterId()).isEqualTo(answer.getWriterId()));
    }

    @Test
    @DisplayName("update 확인")
    void updata() {
        answer.setContents("change content");
        repository.saveAndFlush(answer);
        Answer finedAnswer = repository.findById(answer.getId()).get();
        assertAll(
            () -> assertThat(finedAnswer.getContents()).isEqualTo("change content"),
            () -> assertThat(finedAnswer.getUpdatedAt()).isNotNull());
    }
}
