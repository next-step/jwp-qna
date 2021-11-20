package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문_저장_후_조회_테스트() {
        Question question = questionRepository.save(Q1);

        Optional<Question> opQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(opQuestion).isNotEmpty();
        assertThat(opQuestion.get()).isEqualTo(question);
    }
}
