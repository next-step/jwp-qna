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
            () -> assertThat(savedAnswer.getWriter()).isEqualTo(AnswerTest.A1.getWriter()),
            () -> assertThat(savedAnswer.getQuestion()).isEqualTo(QuestionTest.Q1),
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

    @Test
    @DisplayName("실제 삭제가 아닌 경우, 답변 ID로 조회 테스트")
    void findByIdAndDeletedFalse() {
        // given & when
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(savedAnswer.getId());
        // then
        assertThat(actual).isPresent();
        assertThat(actual).contains(savedAnswer);
    }

    @Test
    @DisplayName("실제 삭제인 경우, 답변 ID로 조회 테스트")
    void findByIdAndDeletedFalse_not_found() {
        // given & when
        savedAnswer.setDeleted(true);
        answers.flush();
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(savedAnswer.getId());
        // then
        assertThat(actual).isNotPresent();
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("실제 삭제가 아닌 경우, 질문 ID로 조회 테스트")
    void findByQuestionIdAndDeletedFalse() {
        // given & when
        List<Answer> actualList = answers.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestion().getId());
        // then
        assertThat(actualList).containsExactly(savedAnswer);
    }

    @Test
    @DisplayName("실제 삭제인 경우, 질문 ID로 조회 테스트")
    void findByQuestionIdAndDeletedFalse_not_found() {
        // given & when
        savedAnswer.setDeleted(true);
        answers.flush();
        List<Answer> actualList = answers.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestion().getId());
        // then
        assertThat(actualList).isEmpty();
    }
}
