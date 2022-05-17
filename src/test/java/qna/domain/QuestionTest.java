package qna.domain;

public class QuestionTest {
    public static final Question Q1 = new Question.QuestionBuilder("title1")
            .contents("contents1")
            .build()
            .writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question.QuestionBuilder("title2")
            .contents("contents2")
            .build()
            .writeBy(UserTest.SANJIGI);
}
