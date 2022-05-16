package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/truncate.sql")
public class AnswerTest {
    private Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    private Answer a1 = new Answer(UserTest.JAVAJIGI,  q1, "Answers Contents1");
    private Answer a2 = new Answer(UserTest.SANJIGI, q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp(){
        q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        a1 = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");
        a2 = new Answer(UserTest.SANJIGI, q1 , "Answers Contents2");
    }

    @Test
    void save_테스트() {
        Answer managed = answerRepository.save(a1);
        Assertions.assertAll(
                () -> assertThat(managed.getContents().equals(a1.getContents())).isTrue()
        );
    }

    @Test
    void findById_테스트() {
        Answer savedA1 = answerRepository.save(a1);
        Optional<Answer> a1 = answerRepository.findById(savedA1.getId());
        assertThat(a1.isPresent()).isTrue();
        assertThat(a1.get() == savedA1).isTrue();
    }

    @Test
    void deleteById_테스트() {
        Answer savedA1 = answerRepository.save(a1);
        answerRepository.deleteById(savedA1.getId());
        Optional<Answer> a1 = answerRepository.findById(savedA1.getId());
        assertThat(a1.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Answer savedA1 = answerRepository.save(a1);
        Answer savedA2 = answerRepository.save(a2);
        savedA1.setDeleted(true);
        Optional<Answer> a1 = answerRepository.findByIdAndDeletedFalse(savedA1.getId());
        Optional<Answer> a2 = answerRepository.findByIdAndDeletedFalse(savedA2.getId());
        assertThat(a1.isPresent()).isFalse();
        assertThat(a2.isPresent()).isTrue();
        assertThat(a2.get() == savedA2).isTrue();
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        Answer savedA1 = answerRepository.save(a1);
        Answer savedA2 = answerRepository.save(a2);
        questionRepository.save(q1);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(savedA1.getQuestionId());
        assertThat(answerList).containsExactly(savedA1, savedA2);
    }
}
