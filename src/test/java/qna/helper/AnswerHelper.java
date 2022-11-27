package qna.helper;

import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.User;

public class AnswerHelper {
    private final AnswerRepository answerRepository;

    public AnswerHelper(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public Answer createAnswer(User writer, Question question, String contents) {
        final Answer answer = new Answer(writer, question, contents);
        Answer savedAnswer = answerRepository.save(answer);
        writer.addAnswer(savedAnswer);
        question.addAnswer(savedAnswer);
        return savedAnswer;
    }
}
