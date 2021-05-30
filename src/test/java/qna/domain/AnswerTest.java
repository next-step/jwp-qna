package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answers;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("Answer 데이터를 저장후 인서트가 이루어 졌는지 데이터 확인")
    @Test
    void save() {
        Answer actual = answers.save(A1);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(actual.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @DisplayName("Answer에 A1,A2 를 저장 후 QuestionId 와 Deleted가 False 인 데이터 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(A1);
        answers.save(A2);
        List<Answer> actuals = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(actuals.size()).isEqualTo(2);
        actuals.stream()
                .forEach(
                        actual -> assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q1.getId())
                );
    }

    @DisplayName("Answer 에 A1을 저장 후 UserTest.SANJIGI 로 업데이트 테스트")
    @Test
    void update() {
        Answer actual = answers.save(A1);
        assertThat(actual.getWriterId()).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();

        actual.setWriterId(UserTest.SANJIGI.getId());
        Answer expected = answers.findByWriterIdAndDeletedFalse(UserTest.SANJIGI.getId());
        assertThat(expected.getWriterId()).isNotNull();
        assertThat(expected.isOwner(UserTest.SANJIGI)).isTrue();
    }

    @DisplayName("Answer 에 A1을 저장 후 다시 delete")
    @Test
    void delete() {
        Answer actual = answers.save(A1);
        assertThat(actual.getWriterId()).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();
        Answer expectedTrue = answers.findByWriterIdAndDeletedFalse(UserTest.JAVAJIGI.getId());
        assertThat(expectedTrue).isNotNull();

        answers.delete(actual);
        Answer expectedFalse = answers.findByWriterIdAndDeletedFalse(UserTest.JAVAJIGI.getId());
        assertThat(expectedFalse).isNull();
    }
}
