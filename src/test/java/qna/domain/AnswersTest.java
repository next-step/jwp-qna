package qna.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class AnswersTest {

    private Answer answer;

    @BeforeEach
    void setup() {
        answer = new Answer(1L, JAVAJIGI, Q1, "answer_contents");
    }

    @Test
    @DisplayName("답변 생성 기능 테스트")
    void add() {
        Answers answers = new Answers();

        answers.add(answer);

        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    @DisplayName("모든 답변 삭제 기능 테스트")
    void deleteAll() {
        Answers answers = new Answers();
        answers.add(answer);

        DeleteHistories deleteHistories = answers.deleteAll();

        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("답변 작성자 판별 기능 테스트")
    void isIncludedFromOtherThan() {
        Answers answers = new Answers();
        answers.add(answer);

        assertThat(answers.isIncludedFromOtherThan(SANJIGI)).isTrue();
    }
}