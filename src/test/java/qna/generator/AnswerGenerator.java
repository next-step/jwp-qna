package qna.generator;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.User;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class AnswerGenerator {

    public static int COUNTER = 0;
    public static final String CONTENTS = "답변 내용";

    private final AnswerRepository answerRepository;

    public AnswerGenerator(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public static Answer generateAnswer(User writer, Question question) {
        Answer answer = new Answer(writer, question, CONTENTS + COUNTER);
        question.addAnswer(answer);
        return answer;
    }

    public Answer savedAnswer(User writer, Question question) {
        Answer answer = generateAnswer(writer, question);
        answer.toQuestion(question);
        return answerRepository.saveAndFlush(answer);
    }
}
