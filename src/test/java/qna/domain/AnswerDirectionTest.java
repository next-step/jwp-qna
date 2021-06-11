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
        System.out.println("start !!!");
        Question question = questionRepository.findById(5L).get();
        for (Answer answer : question.getAnswers()) {
            System.out.println(answer);
        }

        User writer = userRepository.findById(8L).get();

        question.addAnswer(new Answer(writer, question, "new answer"));
        questionRepository.save(question);
        assertThat(answerRepository.count()).isEqualTo(9);
    }
}
