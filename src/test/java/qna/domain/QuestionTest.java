package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;

@DataJpaTestIncludeAuditing
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void persistRelatedEntities(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(javajigi);
        userRepository.save(sanjigi);
    }

    @Test
    void save_테스트(){
        Question newQuestion = new Question("title1", "contents1").writeBy(javajigi);
        Question managedQuestion = questionRepository.save(newQuestion);
        assertThat(newQuestion).isSameAs(managedQuestion);
        assertThat(managedQuestion.getId()).isNotNull();
    }

    @Test
    void findById_테스트() {
        Question managedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        Optional<Question> foundQuestion = questionRepository.findById(managedQuestion.getId());
        assertThat(foundQuestion.isPresent()).isTrue();
    }

    @Test
    void deleteById_테스트() {
        Question managedQuestion = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        questionRepository.deleteById(managedQuestion.getId());
        Optional<Question> question = questionRepository.findById(managedQuestion.getId());
        assertThat(question.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Question managedQuestion1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        Question managedQuestion2 = questionRepository.save(new Question("title2", "contents2").writeBy(sanjigi));
        managedQuestion1.setDeleted(true);
        Optional<Question> q1 = questionRepository.findByIdAndDeletedFalse(managedQuestion1.getId());
        Optional<Question> q2 = questionRepository.findByIdAndDeletedFalse(managedQuestion2.getId());
        assertThat(q1.isPresent()).isFalse();
        assertThat(q2.isPresent()).isTrue();
    }

    @Test
    void findByDeletedFalse_테스트() {
        Question managedQuestion1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        Question managedQuestion2 = questionRepository.save(new Question("title2", "contents2").writeBy(sanjigi));
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertThat(questionList).containsExactly(managedQuestion1, managedQuestion2);
    }
}
