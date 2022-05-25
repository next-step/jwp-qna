package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AnswersTest {
    @Test
    @DisplayName("Answers 삭제: 정상")
    void Answers_삭제() {
        Answer answer1 = AnswerTest.generateAnswer(UserTest.JAVAJIGI, QuestionTest.Q1, false);
        Answers answers = new Answers(Arrays.asList(answer1));
        answers.deleteAnswers(UserTest.JAVAJIGI);
        assertThat(answer1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Answers 삭제: 작성자가 맞지않아 실패")
    void Answers_삭제_실패(){
        Answer answer1 = AnswerTest.generateAnswer(UserTest.JAVAJIGI, QuestionTest.Q1, false);
        Answers answers = new Answers(Arrays.asList(answer1));
        assertThatThrownBy(() -> {
            answers.deleteAnswers(UserTest.SANJIGI);
        });
    }

    @Test
    @DisplayName("Answers 삭제 반환값: 새롭게 삭제한 Answer만 반환")
    void Answers_삭제_반환값(){
        Answer answer1 = AnswerTest.generateAnswer(UserTest.JAVAJIGI, QuestionTest.Q1, false);
        Answer answer2 = AnswerTest.generateAnswer(UserTest.JAVAJIGI, QuestionTest.Q1, false);
        Answer answer3 = AnswerTest.generateAnswer(UserTest.JAVAJIGI, QuestionTest.Q1, true);
        Answers answers = new Answers(Arrays.asList(answer1, answer2, answer3));
        Answers deletedAnswers = answers.deleteAnswers(UserTest.JAVAJIGI);
        assertThat(deletedAnswers.getAnswers()).containsExactly(answer1, answer2);
    }
}
