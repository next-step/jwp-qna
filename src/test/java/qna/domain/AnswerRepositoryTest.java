package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Answer answer1;
    private Answer answer2;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;


    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        user2 = userRepository.save(new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net"));

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));

        answer1 = answerRepository.save(new Answer(user1, question1, "Answers Contents1"));
        answer2 = answerRepository.save(new Answer(user2, question2, "Answers Contents2"));
    }

    @Test
    void save() {
        assertThat(answer1.getWriter()).isEqualTo(user1);
        assertThat(answer1.getQuestion()).isEqualTo(question1);
    }

    @Test
    void findById() {
        Optional<Answer> expected = answerRepository.findById(answer1.getId());
        assertThat(expected.isPresent()).isTrue();
    }

    @Test
    void findByIdAndDeletedFalse() {
        answerRepository.delete(answer1);
        Optional<Answer> deletedAnswer = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        Optional<Answer> existAnswer = answerRepository.findByIdAndDeletedFalse(answer2.getId());

        assertThat(deletedAnswer.isPresent()).isFalse();
        assertThat(existAnswer.isPresent()).isTrue();
    }

    @Test
    void findByQuestionId() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
        assertThat(answers.stream().allMatch(answer -> answer.getQuestion().equals(question1))).isTrue();
    }

    @Test
    void update() {
        answer1.setDeleted(true);

        assertThat(answer1.isDeleted()).isTrue();
    }

    @Test
    void delete() {
        answerRepository.delete(answer1);
        Optional<Answer> expected = answerRepository.findById(answer1.getId());
        assertThat(expected.isPresent()).isFalse();
    }
}
