package qna.generator;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

/**
 * @author : choi-ys
 * @date : 2022/05/26 1:14 오후
 */
@TestConstructor(autowireMode = AutowireMode.ALL)
public class QuestionGenerator {

    private final QuestionRepository questionRepository;

    public QuestionGenerator(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public static Question generateQuestion(User writer) {
        return new Question("질문 제목", "contents").writeBy(writer);
    }

    public Question savedQuestion(User savedUser) {
        return questionRepository.saveAndFlush(generateQuestion(savedUser));
    }
}
