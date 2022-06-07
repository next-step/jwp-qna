package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;


    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1).writeBy(user);

        assertThat(question.getWriter()).isEqualTo(user);
    }

    @Test
    void getQuestionByUser() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1).writeBy(user);

        Question getQuestion = questionRepository.findByIdAndDeletedFalse(user.getId()).get();

        assertThat(getQuestion.getWriter()).isEqualTo(user);
    }


}