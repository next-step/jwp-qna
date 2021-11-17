package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AnswersTest extends QnATest {

    @Test
    void 단건_저장_확인() {
        // given
        User user = new User(USER_A, PASSWORD, USER_A, USER_A_EMAIL);
        Question question = new Question(TITLE_1, CONTENTS_1).writeBy(user);
        Answer firstAnswer = new Answer(user, question, CONTENTS_1);

        // when
        Answers expectedAnswers = new Answers();
        expectedAnswers.addAnswer(firstAnswer);

        // then
        assertThat(expectedAnswers.equals(new Answers(firstAnswer))).isTrue();
    }

    @Test
    void 다건_저장_확인() {
        // given
        User user = new User(USER_A, PASSWORD, USER_A, USER_A_EMAIL);
        Question question = new Question(TITLE_1, CONTENTS_1).writeBy(user);
        Answer firstAnswer = new Answer(user, question, CONTENTS_1);
        Answer secondAnswer = new Answer(user, question, CONTENTS_2);
        Answer thirdAnswer = new Answer(user, question, CONTENTS_3);
        List<Answer> answers = asList(firstAnswer, secondAnswer, thirdAnswer);

        // when
        Answers expectedAnswers = new Answers();
        expectedAnswers.addAnswers(answers, question);

        // then
        assertThat(expectedAnswers.equals(new Answers(answers))).isTrue();
    }
}
