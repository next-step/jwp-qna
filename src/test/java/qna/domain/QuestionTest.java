package qna.domain;

import qna.domain.content.Contents;
import qna.domain.content.Title;
import qna.domain.question.Question;
import qna.domain.user.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question(Title.of("title1"), Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(Title.of("title2"), Contents.of("contents2")).writeBy(UserTest.SANJIGI);
}
