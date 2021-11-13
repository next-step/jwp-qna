package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @DisplayName("Question 저장 테스트")
    @Test
    void saveQuestionTest() {
        final Question savedQuestion = questions.save(Q1);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(savedQuestion.getWriterId()).isNotNull()
        );
    }

    @DisplayName("Question 조회 테스트")
    @Test
    void findByTitleAndContentTest() {
        final Question savedQuestion = questions.save(Q1);
        Question actualQuestion = questions.findByTitleAndContents(Q1.getTitle(), Q1.getContents())
                .orElseThrow(() -> new IllegalArgumentException("Question이 존재하지 않습니다."));

        assertThat(actualQuestion).isSameAs(savedQuestion);
        assertThat(actualQuestion).isEqualTo(savedQuestion);
        assertThat(actualQuestion.getId()).isEqualTo(savedQuestion.getId());
    }

    @DisplayName("Question 수정 테스트")
    @Test
    void updateQuestionTest() {
        final Question savedQuestion = questions.save(Q1);
        savedQuestion.setTitle("수정된 타이틀");
        Question actualQuestion = questions.findByIdAndDeletedFalse(savedQuestion.getId())
                .orElseThrow(() -> new IllegalArgumentException("Question이 존재하지 않습니다."));

        assertThat(actualQuestion.getId()).isEqualTo(savedQuestion.getId());
        assertThat(actualQuestion.getTitle()).isEqualTo("수정된 타이틀");
    }
}
