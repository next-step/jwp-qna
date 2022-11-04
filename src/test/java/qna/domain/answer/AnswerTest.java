package qna.domain.answer;

import qna.domain.question.QuestionTest;
import qna.domain.user.User;

public class AnswerTest {

        public static Answer createAnswer(User writeUser){
            return new Answer(writeUser, QuestionTest.createQuestion(writeUser), "Answers Contents1");
    }

}
