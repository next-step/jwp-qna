package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import qna.domain.*;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void save() {
        Question expected = new Question("제목", "내용");
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(expected.getContents()).isEqualTo(actual.getContents()),
                () -> assertThat(expected.getTitle()).isEqualTo(actual.getTitle())
        );
    }

    @Test
    void findAll() {
        Question expected = new Question("제목", "내용");
        questionRepository.save(expected);
        List<Question> actual = questionRepository.findAll();
        assertThat(actual.get(0).getContents()).isNotNull();
        assertThat(actual.get(0).getCreatedAt()).isNotNull();
        System.out.println(actual.get(0).toString());
    }

    @Test
    void findByDeletedFalse() {
        Question expected = new Question("제목", "내용");
        questionRepository.save(expected);
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual.get(0).getContents()).isNotNull();
    }

    @Test
    void findByWriterId() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question expected = new Question("제목", "내용").writeBy(user);
        questionRepository.save(expected);
        List<Question> actual = questionRepository.findByWriterId(user);
        assertThat(actual.get(0)).isEqualTo(expected);
    }

    @Test
    @Description(value = "answers 조회 테스트")
    void getAnswers() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question expected = new Question("제목", "내용").writeBy(user);
        questionRepository.save(expected);
        answerRepository.save(new Answer(1L, user, expected, "답변1"));
        answerRepository.save(new Answer(2L, user, expected, "답변2"));
        entityManager.flush(); // (1)
        entityManager.clear();
        Optional<Question> actual2 = questionRepository.findByIdAndDeletedFalse(1L);
        assertThat(actual2.get().getAnswers().size()).isEqualTo(2);
    }
}
