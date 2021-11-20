package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswersTest {

    @DisplayName("Answers 일급컬렉션 값 확인")
    @Test
    void containsAnswer() {
        Answer answer = AnswerTest.A1;
        List<Answer> answerList = Arrays.asList(answer);
        Answers answers = new Answers(answerList);

        boolean isContains = answers.contains(answer);

        assertThat(isContains).isTrue();
    }

    @DisplayName("Answer 값 추가")
    @Test
    void addAnswers() {
        Answer answer = AnswerTest.A1;
        List<Answer> answerList = Arrays.asList(answer);
        Answers answers = new Answers(answerList);

        answers = answers.add(AnswerTest.A2);
        boolean contains = answers.contains(AnswerTest.A2);

        assertThat(contains).isTrue();
    }
}
