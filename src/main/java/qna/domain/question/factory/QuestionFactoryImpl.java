package qna.domain.question.factory;

import qna.domain.question.Question;
import qna.domain.question.title.Title;
import qna.domain.question.title.factory.TitleFactory;
import qna.domain.question.title.factory.TitleFactoryImpl;

public class QuestionFactoryImpl implements QuestionFactory {

    private final TitleFactory titleFactory;

    public QuestionFactoryImpl() {
        this.titleFactory = new TitleFactoryImpl();
    }

    @Override
    public Question create(String title, String content) {
        Title createTitle = titleFactory.create(title);
        return new Question(createTitle, content);
    }
}
