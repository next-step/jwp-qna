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
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(5L).size()).isEqualTo(9);
    }

    @Test
    @DisplayName(value = "question 에서 answer 의 value 를 갱신하면 answer 도 변경된다")
    void updateAnswerInQuestion() {
        User loginUser = userRepository.findById(5L).get();
        Question question = questionRepository.findById(8L).get();
        question.deleteAnswers(loginUser);

        questionRepository.save(question);

        question.getAnswers().stream()
            .forEach(answer -> assertThat(answer.isDeleted()).isTrue());
    }
}
