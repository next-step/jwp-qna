package qna.question.domain;

import qna.user.domain.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question(null, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(null, "title2", "contents2").writeBy(UserTest.SANJIGI);
}
