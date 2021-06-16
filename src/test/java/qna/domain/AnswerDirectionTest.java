package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerDirectionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "bi direction select")
    void biDirectionSelect() {
        Question question = questionRepository.findById(5L).get();
        User writer = userRepository.findById(8L).get();

        question.writeAnswer("new answer", writer);
        questionRepository.save(question);
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(5L).size()).isEqualTo(9);
    }

    @Test
    @DisplayName(value = "question 에서 answer 의 value 를 갱신하면 answer 도 변경된다")
    void updateAnswerInQuestion() {
        Question question = questionRepository.findById(8L).get();
        question.deleteRelated();

        questionRepository.save(question);
        question.getAnswers().stream()
            .forEach(answer -> assertThat(answer.isDeleted()).isTrue());
    }
}
