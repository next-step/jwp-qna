package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.DataJpaTestIncludeAuditing;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTestIncludeAuditing
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private Question question;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void persistRelatedEntities(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(javajigi);
        userRepository.save(sanjigi);

        question =  new Question("title1", "contents1").writeBy(javajigi);
        questionRepository.save(question);
    }

    @Test
    void save_테스트(){
        Answer newAnswer = new Answer(javajigi, question, "Answers Contents1");
        Answer managedAnswer = answerRepository.save(newAnswer);
        assertThat(newAnswer).isSameAs(managedAnswer);
        assertThat(managedAnswer.getId()).isNotNull();
    }

    @Test
    void findById_테스트() {
        Answer managedAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Optional<Answer> foundAnswer = answerRepository.findById(managedAnswer.getId());
        assertThat(foundAnswer.isPresent()).isTrue();
        assertThat(foundAnswer.get() == managedAnswer).isTrue();
    }

    @Test
    void deleteById_테스트() {
        Answer managedAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        answerRepository.deleteById(managedAnswer.getId());
        Optional<Answer> answer = answerRepository.findById(managedAnswer.getId());
        assertThat(answer.isPresent()).isFalse();
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Answer managedAnswer1 = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Answer managedAnswer2 = answerRepository.save(new Answer(sanjigi, question, "Answers Contents2"));
        managedAnswer1.setDeleted(true);
        Optional<Answer> a1 = answerRepository.findByIdAndDeletedFalse(managedAnswer1.getId());
        Optional<Answer> a2 = answerRepository.findByIdAndDeletedFalse(managedAnswer2.getId());
        assertThat(a1.isPresent()).isFalse();
        assertThat(a2.isPresent()).isTrue();
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        Answer managedAnswer1 = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Answer managedAnswer2 = answerRepository.save(new Answer(sanjigi, question, "Answers Contents2"));
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(answerList).containsExactly(managedAnswer1, managedAnswer2);
    }
}
