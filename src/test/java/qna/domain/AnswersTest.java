package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Answer_List_일급객체_테스트")
public class AnswersTest {
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void before() {
        answer1 = AnswerTest.A1;
        answer2 = AnswerTest.A2;
    }

    @DisplayName("Answers_add_테스트")
    @Test
    void add() {
        Answers answers = new Answers();
        answers.add(answer1);
        assertThat(answers.getAnswers()).containsExactly(answer1);
    }

    @DisplayName("Answers_중에_User가_쓰지않은_것이_있으면_성공")
    @Test
    void isNotWrittenUserInAnswersPass() {
        Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);

        assertThat(answers.isNotWrittenUserInAnswers(UserTest.SANJIGI)).isTrue();
    }

    @DisplayName("Answers_중에_User가_쓰지않은_것이_있으면_실패")
    @Test
    void isNotWrittenUserInAnswersFail() {
        Answers answers = new Answers();
        answers.add(answer1);

        assertThat(answers.isNotWrittenUserInAnswers(UserTest.JAVAJIGI)).isFalse();
    }

    @DisplayName("전체_삭제_테스트")
    void deleteAll() {
        Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);

        DeleteHistories deleteHistories = answers.deleteAll();

        assertThat(deleteHistories.getDeleteHistories().size()).isEqualTo(2);
    }
}
