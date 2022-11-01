package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 삭제되지_않은_질문_조회() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        List<Question> retrievedQuestions = questionRepository.findByDeletedFalse();

        assertThat(retrievedQuestions).hasSize(2);
    }

    @Test
    void 삭제되지_않은_질문_아이디로_조회() {
        Question question = questionRepository.save(Q1);

        Question retrievedQuestion = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        assertThat(retrievedQuestion).isEqualTo(question);
    }
}
