package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    private Answer savedAnswer;

    @BeforeEach
    void init() {
        savedAnswer = answers.save(AnswerTest.A1);
    }

    @Test
    @DisplayName("저장 테스트")
    void save() {
        // given & when & then
        assertAll(
            () -> assertThat(savedAnswer.getId()).isNotNull(),
            () -> assertThat(savedAnswer.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
            () -> assertThat(savedAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    @DisplayName("영속성 동일성 테스트")
    void findById() {
        // given & when
        Answer actual = answers.findById(savedAnswer.getId()).get();
        // then
        assertThat(actual).isEqualTo(savedAnswer);
    }

    @Test
    @DisplayName("수정 전 테스트")
    void update_before() {
        // given
        String newContent = "new content";
        // when & then
        assertThat(savedAnswer.getContents()).isNotEqualTo(newContent);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        String newContent = "new content";
        // when
        savedAnswer.setContents(newContent);
        answers.flush();
        Answer actual = answers.findById(savedAnswer.getId()).get();
        // then
        assertThat(actual.getContents()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        // given & when
        answers.delete(savedAnswer);
        // then
        assertThat(answers.findById(savedAnswer.getId())).isNotPresent();
    }
}
