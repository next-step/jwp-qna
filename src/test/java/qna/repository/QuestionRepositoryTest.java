package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;
    User savedUser1;
    User savedUser2;

    @BeforeEach
    void setUp() {
        savedUser1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        savedUser2 = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
    }

    @Test
    @DisplayName("삭제상태가 false인 Question목록을 반환")
    void test_returns_questions_with_deleted_is_false() {
        Question savedQuestion1 = questionRepository.save(new Question("title1", "contents1")
                .writeBy(savedUser1));
        Question savedQuestion2 = questionRepository.save(new Question("title1", "contents1")
                .writeBy(savedUser2));

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestions.size()).isEqualTo(2),
                () -> assertThat(findQuestions).containsExactly(savedQuestion1, savedQuestion2)
        );
    }

    @Test
    @DisplayName("questionId에 해당하고 삭제상태가 false인 Question을 반환")
    void test_returns_question_deleted_is_false() {
        Question savedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(savedUser1));

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(findQuestion).contains(savedQuestion);
    }

    @Test
    @DisplayName("questionId로 조회하면 question과 연관관계에있는 answer목록개수를 반환")
    void test_returns_answers_with_questionid() {
        Question savedQuestion = questionRepository.save(new Question("title1", "contents1")
                .writeBy(savedUser1));
        User user1 = userRepository.save(new User("jongmin", "password", "name", "javajigi@slipp.net"));
        answerRepository.save(new Answer(user1, savedQuestion, "contents"));
        User user2 = userRepository.save(new User("minu", "password", "name", "minu@slipp.net"));
        answerRepository.save(new Answer(user2, savedQuestion, "contents"));
        entityManager.flush();
        entityManager.clear();

        Question findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()).get();

        assertThat(findQuestion.getAnswers()).hasSize(2);

    }
}

