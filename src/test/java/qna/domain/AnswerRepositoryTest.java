package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer a1;
    private Answer a2;
    private Answer a3;

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {

        User javajigi = userRepository.save(new User("javajigi", "1234", "javajigi", "a@email.com"));
        User sanjigi = userRepository.save(new User("sanjigi", "1234", "sanjigi", "b@email.com"));

        q1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        q2 = questionRepository.save(new Question("title2", "contents2").writeBy(sanjigi));

        a1 = answerRepository.save(new Answer(javajigi, q1, "Answers Contents1"));
        a2 = answerRepository.save(new Answer(javajigi, q2, "Answers Contents2"));
        a3 = answerRepository.save(new Answer(sanjigi, q2, "Answers Contents3"));
        a3.delete();
    }

    @DisplayName("Answer 저장")
    @Test
    void save() {
        assertAll(
                () -> assertThat(a1.getId()).isNotNull(),
                () -> assertThat(a1.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @DisplayName("업데이트 확인")
    @Test
    void update() {
        a2.setContents("Answers updatedContents2");

        assertAll(
                () -> assertThat(a2.getContents()).isEqualTo("Answers updatedContents2")
        );
    }

    @DisplayName("NonNull인 항목 Not Null인지 확인")
    @Test
    void checkNonNull() {
        assertThat(a1.isDeleted()).isNotNull();
    }

    @DisplayName("Answer의 deleted가 true인 경우 find 안되도록")
    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> optAnswer = answerRepository.findByIdAndDeletedFalse(a3.getId());
        assertThatThrownBy(() -> optAnswer.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("동일한 QuestionId를 가지고 있는 항목 확인")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answerList = answerRepository.findByQuestion(q2);
        assertThat(answerList).hasSize(1);
    }
}
