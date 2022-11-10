package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestEntityManager manager;

    @BeforeEach
    void setUp() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("Answer 저장 테스트")
    void saveAnswer() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer answer = new Answer(javajigi, question, "Answers Contents1");
        Answer saveAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(saveAnswer.getId()).isNotNull(),
                () -> assertThat(saveAnswer.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(saveAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(saveAnswer.getQuestion()).isEqualTo(answer.getQuestion())
        );
    }

    @Test
    @DisplayName("Answer 2건 저장 후 전체 조회 테스트")
    void saveAllAnswer() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer1 = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Answer saveAnswer2 = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        answerRepository.saveAll(Arrays.asList(saveAnswer1, saveAnswer2));

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers).hasSize(2);
    }

    @Test
    @DisplayName("Answer 저장 후 Answer 조회 테스트")
    void readAnswer() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(findAnswer.get()).isSameAs(saveAnswer);
    }

    @Test
    @DisplayName("Answer 저장 후 Question 수정 테스트")
    void updateAnswer() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        saveAnswer.setContents("수정 테스트");
        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(findAnswer.get().getContents()).isEqualTo("수정 테스트");
    }

    @Test
    @DisplayName("Answer 저장 후 Answer 삭제 테스트")
    void deleteAnswer() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        answerRepository.delete(saveAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("Answer 저장 후 deleted가 false면 조회되는지 테스트")
    void findByIdAndDeletedFalse() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(saveAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("Answer 저장 후 deleted가 true면 조회되는지 테스트")
    void findByIdAndDeletedTrue() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        saveAnswer.delete();

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("Answer 저장 후 Question id로 조회되는지 테스트")
    void findByQuestionIdAndDeletedFalse() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        List<Answer> findAnswer = answerRepository.findByQuestionIdAndDeletedFalse(saveAnswer.getQuestion().getId());

        assertThat(findAnswer).containsExactly(saveAnswer);
    }

    @Test
    @DisplayName("Answer 저장 후 delete가 true인 경우 Question id로 조회되는지 테스트")
    void findByQuestionIdAndDeletedTrue() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Answer saveAnswer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));
        saveAnswer.delete();

        List<Answer> findAnswer = answerRepository.findByQuestionIdAndDeletedFalse(saveAnswer.getQuestion().getId());

        assertThat(findAnswer).isEmpty();
    }
}
