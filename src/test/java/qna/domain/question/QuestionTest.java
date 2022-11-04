package qna.domain.question;

import qna.domain.user.User;
import qna.domain.user.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question createQuestion(User writeUser) {
        return new Question("title", "contents").writeBy(writeUser);
    }

}
