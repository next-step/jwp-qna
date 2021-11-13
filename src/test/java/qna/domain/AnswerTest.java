package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void save() {
        User user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer expected = new Answer(user, question, "Answers Contents1");

        Answer actual = answerRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
        assertThat(actual.getWriter()).isSameAs(user);
        assertThat(actual.getQuestion()).isSameAs(question);
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        Answer expected = saveNewDefaultAnswer();

        Answer actual = answerRepository.findById(expected.getId()).get();

        assertThat(actual).isSameAs(expected);
    }

    @Test
    @DisplayName("JPA 변경감지로 인한 업데이트 기능 테스트")
    void update() {
        Answer expected = saveNewDefaultAnswer();
        expected.setContents("하고 싶은대로 하면서 살면 된다.");

        Answer actual = answerRepository.findById(expected.getId()).get();

        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        Answer expected = saveNewDefaultAnswer();
        answerRepository.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> answerRepository.findById(expected.getId()).get()
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer expected = saveNewDefaultAnswer();

        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(expected.getQuestion());

        assertThat(answers).isNotEmpty();
    }

    @Test
    @DisplayName("저장한 객체에 대해 soft delete를 한 후, findByIdAndDeletedFalse함수로 조회하면 나오지 않는지 테스트")
    void findByIdAndDeletedFalse() {
        Answer saved = saveNewDefaultAnswer();
        saved.setDeleted(true);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> answerRepository.findByIdAndDeletedFalse(saved.getId()).get()
        );
    }

    private Answer saveNewDefaultAnswer() {
        User user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer defaultAnswer = new Answer(user, question, "Answers Contents1");
        return answerRepository.save(defaultAnswer);
    }
}
