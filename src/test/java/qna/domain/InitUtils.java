package qna.domain;

import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;


public class InitUtils {

    public static void init() {
        JAVAJIGI.setId(null);
        SANJIGI.setId(null);

        Q1.setId(null);
        Q2.setId(null);

        A1.setQuestion(null);
        A2.setQuestion(null);

        Q1.getAnswers().clear();
        Q2.getAnswers().clear();

        A1.setId(null);
        A2.setId(null);
        A1.toQuestion(Q1);
        A2.toQuestion(Q1);

    }

}
