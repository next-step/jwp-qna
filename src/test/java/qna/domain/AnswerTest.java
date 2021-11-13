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
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @DisplayName("answer 저장 테스트")
    @Test
    void saveAnswerTest() {
        Answer actualAnswer1 = answers.save(A1);
        Answer actualAnswer2 = answers.save(A2);

        assertAll(
                () -> assertThat(actualAnswer1).isNotNull(),
                () -> assertThat(actualAnswer1.getId()).isNotNull(),
                () -> assertThat(actualAnswer1.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(actualAnswer1.getQuestionId()).isNull(),
                () -> assertThat(actualAnswer1.getWriterId()).isNotNull()
        );

        assertAll(
                () -> assertThat(actualAnswer2).isNotNull(),
                () -> assertThat(actualAnswer2.getId()).isNotNull(),
                () -> assertThat(actualAnswer2.getContents()).isEqualTo(A2.getContents()),
                () -> assertThat(actualAnswer2.getQuestionId()).isNull(),
                () -> assertThat(actualAnswer2.getWriterId()).isNotNull()
        );
    }

    @DisplayName("answer 조회 테스트")
    @Test
    void findByContentsTest() {
        final Answer savedAnswer = answers.save(A1);
        Answer actualAnswer = answers.findByContents(A1.getContents())
                .orElseThrow(() -> new IllegalArgumentException("answer가 존재하지 않습니다."));

        assertThat(actualAnswer).isSameAs(savedAnswer);
        assertThat(actualAnswer.getId()).isEqualTo(savedAnswer.getId());
        assertThat(actualAnswer.getContents()).isEqualTo(savedAnswer.getContents());
    }


    @DisplayName("answer 수정 테스트")
    @Test
    void updateAnswerTest() {
        final Answer savedAnswer = answers.save(A1);
        savedAnswer.setDeleted(true);
        Answer actualAnswer = answers.findByContents(A1.getContents())
                .orElseThrow(() -> new IllegalArgumentException("answer가 존재하지 않습니다."));

        assertThat(actualAnswer.isDeleted()).isTrue();
        answers.flush();
    }
}
