package qna.domain.question;

import qna.domain.question.title.Title;
import qna.domain.user.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question(new Title("title1"), "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(new Title("title2"), "contents2").writeBy(UserTest.SANJIGI);
}
