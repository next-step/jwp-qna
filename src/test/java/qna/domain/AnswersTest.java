package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {

    private Answers answers = new Answers();

    @BeforeEach
    void setUp() {
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        answerList.add(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2"));
        answers.setAnswerList(answerList);
    }

    @Test
    void 삭제_히스토리_리턴() {
        assertThat(answers.createDeleteHistories()).hasSize(2);
    }

}
