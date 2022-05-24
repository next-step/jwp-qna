package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);
    public static final Question Q4 = new Question("title4", "contents4").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void findByDeletedFalse() {
        questionRepository.save(Q3);
        questionRepository.save(Q4);
        List<Question> actual = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).contains(Q3, Q4)
        );
    }

    @Test
    @DisplayName("질문을 하나 가져올 때, 이 질문에 대한 답변을 같이 가져올 수 있는지 확인한다")
    void findByIdDeletedFalse() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        answerRepository.save(A1);
        answerRepository.save(A2);
        Q1.addAnswer(A1);
        Q1.addAnswer(A2);

        Question actual = questionRepository.findByIdAndDeletedFalse(Q1.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getAnswers()).contains(A1, A2)
        );
    }
}
