package qna.domain;

import qna.question.Question;

import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", SANJIGI);
}
