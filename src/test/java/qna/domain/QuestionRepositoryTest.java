package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    private Question savedQuestion;

    @BeforeEach
    void init() {
        savedQuestion = questions.save(QuestionTest.Q1);
    }

    @Test
    @DisplayName("저장 테스트")
    void save() {
        // given & when & then
        assertAll(
            () -> assertThat(savedQuestion.getId()).isNotNull(),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(QuestionTest.Q1.getWriter())
        );
    }

    @Test
    @DisplayName("영속성 동일성 테스트")
    void findById() {
        // given & when
        Question actual = questions.findById(savedQuestion.getId()).get();
        // then
        assertThat(actual).isEqualTo(savedQuestion);
    }

    @Test
    @DisplayName("수정 전 테스트")
    void update_before() {
        // given
        String newContent = "new content";
        // when & then
        assertThat(savedQuestion.getContents()).isNotEqualTo(newContent);
    }

    @Test
    @DisplayName("수정 테스트")
    void update() {
        // given
        String newContent = "new content";
        // when
        savedQuestion.setContents(newContent);
        questions.flush();
        Question actual = questions.findById(savedQuestion.getId()).get();
        // then
        assertThat(actual.getContents()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        // given & when
        questions.delete(savedQuestion);
        // then
        assertThat(questions.findById(savedQuestion.getId())).isNotPresent();
    }

    @Test
    @DisplayName("실제 삭제가 아닌 경우, 질문 ID로 조회 가능 테스트")
    void findByIdAndDeletedFalse() {
        // given & when
        Optional<Question> actual = questions.findByIdAndDeletedFalse(savedQuestion.getId());
        // then
        assertThat(actual).isPresent();
        assertThat(actual).contains(savedQuestion);
    }

    @Test
    @DisplayName("실제 삭제인 경우, 질문 ID로 조회 테스트")
    void findByIdAndDeletedFalse_not_found() {
        // given & when
        savedQuestion.setDeleted(true);
        questions.flush();
        Optional<Question> actual = questions.findByIdAndDeletedFalse(savedQuestion.getId());
        // then
        assertThat(actual).isNotPresent();
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("실제 삭제가 아닌 질문 리스트 조회 테스트")
    void findByDeletedFalse() {
        // given & when
        List<Question> actualList = questions.findByDeletedFalse();
        // then
        assertThat(actualList).containsExactly(savedQuestion);
    }
}
