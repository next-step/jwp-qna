package qna.domain.question.factory;

import qna.domain.question.Question;

public interface QuestionFactory {

    Question create(String title, String content);
}
