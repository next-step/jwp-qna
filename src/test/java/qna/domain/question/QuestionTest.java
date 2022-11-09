package qna.domain.question;

import qna.domain.content.Content;
import qna.domain.content.Title;
import qna.domain.user.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question(Title.of("title1"), Content.of("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(Title.of("title2"), Content.of("contents2")).writeBy(UserTest.SANJIGI);
}
