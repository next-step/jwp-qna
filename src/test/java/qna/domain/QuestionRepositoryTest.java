package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;

    @DisplayName("Question 저장 및 데이터 확인")
    @Test
    void saveQuestion() {
        User user = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(user);
        Question actual = questions.save(question);

        Long actualId = actual.getId();

        assertThat(actualId).isNotNull();
    }

    @DisplayName("writer_id로 Question 정보 찾기")
    @Test
    void findByWriterId() {
        User user = users.save(TestCreateFactory.createUser(1L));
        Question actual = questions.save(TestCreateFactory.createQuestion(user));
        Question searched = questions.findByWriterId(user.getId());

        assertThat(actual).isEqualTo(searched);
    }

    @DisplayName("Question 정보 중 deleted false 인 값 찾기")
    @Test
    void findByDeletedFalse() {
        questions.deleteAll();
        User firstUser = users.save(TestCreateFactory.createUser(1L));
        User secondUser = users.save(TestCreateFactory.createUser(2L));
        Question firstQuestion = questions.save(TestCreateFactory.createQuestion(firstUser));
        Question secondQuestion = questions.save(TestCreateFactory.createQuestion(secondUser));

        List<Question> questionList = questions.findByDeletedFalse();

        assertThat(questionList).contains(firstQuestion, secondQuestion);
    }
}
