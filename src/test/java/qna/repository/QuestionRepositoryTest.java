package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("Question 저장 테스트")
    void saveQuestion() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = QuestionTest.Q1.writeBy(javajigi);
        Question saveQuestion = questionRepository.save(question);

        assertAll(
                () -> assertThat(saveQuestion.getId()).isNotNull(),
                () -> assertThat(saveQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(saveQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(saveQuestion.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @Test
    @DisplayName("Question 2건 저장 후 전체 조회 테스트")
    void saveAllQuestion() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);
        Question question1 = QuestionTest.Q1.writeBy(javajigi);
        Question question2 = QuestionTest.Q2.writeBy(sanjigi);
        questionRepository.saveAll(Arrays.asList(question1, question2));

        List<Question> Questions = questionRepository.findAll();

        assertThat(Questions).hasSize(2);
    }

    @Test
    @DisplayName("Question 저장 후 Question 조회 테스트")
    void readQuestion() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion).isPresent();
        assertThat(findQuestion.get()).isSameAs(saveQuestion);
    }

    @Test
    @DisplayName("Question 저장 후 Question 수정 테스트")
    void updateQuestion() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        saveQuestion.changeTitle(QuestionTest.Q2.getTitle());

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion).isPresent();
        assertThat(saveQuestion.getTitle()).isEqualTo(findQuestion.get().getTitle());
    }

    @Test
    @DisplayName("Question 저장 후 Question 삭제 테스트")
    void deleteQuestion() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        questionRepository.delete(saveQuestion);

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion).isNotPresent();
    }

    @Test
    @DisplayName("Question 2건 저장 후 deleted가 false인 Question 조회 테스트")
    void findByDeletedFalse() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion1 = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        Question saveQuestion2 = questionRepository.save(QuestionTest.Q2.writeBy(javajigi));
        saveQuestion2.delete();

        List<Question> findQuestion = questionRepository.findByDeletedFalse();

        assertThat(findQuestion).hasSize(1);
        assertThat(findQuestion).containsExactly(saveQuestion1);
    }

    @Test
    @DisplayName("Question 저장 후 deleted가 false인 Question 조회 테스트")
    void findByIdAndDeletedFalse() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion1 = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion1.getId());

        assertThat(findQuestion).isPresent();
        assertThat(findQuestion.get()).isEqualTo(saveQuestion1);
    }

    @Test
    @DisplayName("Question 저장 후 deleted가 true인 Question 조회 테스트")
    void findByIdAndDeletedTrue() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion1 = questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        saveQuestion1.delete();

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion1.getId());

        assertThat(findQuestion).isNotPresent();
    }
}
