package qna.helper;

import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

public class QuestionHelper {
    private final QuestionRepository questionRepository;

    public QuestionHelper(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question createQuestion(String title, String contents, User writer) {
        final Question question = new Question(title, contents).writeBy(writer);
        Question savedQuestion = questionRepository.save(question);
        writer.addQuestion(savedQuestion);
        return savedQuestion;
    }
}
