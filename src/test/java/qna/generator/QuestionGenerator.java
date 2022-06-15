package qna.generator;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class QuestionGenerator {

    public static int COUNTER = 0;
    public static final String TITLE = "질문 제목";
    public static final String CONTENTS = "질문 내용";

    private final QuestionRepository questionRepository;

    public QuestionGenerator(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public static Question generateQuestion(User writer) {
        return new Question(TITLE + COUNTER, CONTENTS + COUNTER).writeBy(writer);
    }

    public Question savedQuestion(User savedUser) {
        return questionRepository.saveAndFlush(generateQuestion(savedUser));
    }
}
