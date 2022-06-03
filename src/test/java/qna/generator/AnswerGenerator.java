package qna.generator;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.User;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class AnswerGenerator {

    private final AnswerRepository answerRepository;

    public AnswerGenerator(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public static Answer generateAnswer(User writer, Question question, String contents) {
        return new Answer(writer, question, contents);
    }

    public Answer savedAnswer(User writer, Question question, String contents) {
        Answer answer = generateAnswer(writer, question, contents);
        answer.toQuestion(question);
        return answerRepository.saveAndFlush(answer);
    }
}
