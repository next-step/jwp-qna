package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @ParameterizedTest
    @NullAndEmptySource
    void title_null_테스트(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Question(title, "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @Test
    void 질문_저장_후_조회_테스트() {
        Question question = questionRepository.save(Q1);

        Optional<Question> opQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(opQuestion).isNotEmpty();
        assertThat(opQuestion.get()).isEqualTo(question);
    }
}
