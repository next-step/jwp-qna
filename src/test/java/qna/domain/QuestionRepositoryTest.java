package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
    }

    @AfterEach
    public void tearDown() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("질문 1 등록 성공")
    public void saveQuestionSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();

        Question save = questionRepository.save(
            new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents())
                .writeBy(javajigi));

        assertAll(() -> {
            assertThat(save.getWriter().equalsNameAndEmail(javajigi)).isTrue();
            assertThat(save.getTitle()).isEqualTo(QuestionTest.Q1.getTitle());
        });
    }

    @Test
    @DisplayName("삭제되지 않은 질문 목록 조회 성공")
    public void findQuestionByDeletedFalseSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        questionRepository.save(
            new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents())
                .writeBy(javajigi));

        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertAll(() -> {
            assertThat(questionList.size()).isEqualTo(1);
            questionList.stream().forEach(question -> {
                assertThat(question.getWriter().equalsNameAndEmail(javajigi)).isTrue();
                assertThat(question.getTitle()).isEqualTo(QuestionTest.Q1.getTitle());
            });
        });

    }

    @Test
    @DisplayName("질문 삭제 플래그 true 변경 성공")
    public void updateDeletedSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question save = questionRepository.save(
            new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents())
                .writeBy(javajigi));

        save.setDeleted(true);
        Question deleted = questionRepository.save(save);

        assertAll(() -> {
            assertThat(deleted.getId()).isEqualTo(save.getId());
            assertThat(deleted.isDeleted()).isTrue();
        });
    }

    @Test
    @DisplayName("질문 id 로 한건 찾기 성공")
    public void findQuestionByIdSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question save = questionRepository.save(
            new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents())
                .writeBy(javajigi));

        Optional<Question> optionalQuestion = questionRepository
            .findByIdAndDeletedFalse(save.getId());

        assertAll(() -> {
            assertThat(optionalQuestion.isPresent()).isTrue();
            Question question = optionalQuestion.get();
            assertThat(question.getTitle()).isEqualTo(QuestionTest.Q1.getTitle());
            assertThat(question.getWriter().equalsNameAndEmail(javajigi)).isTrue();
        });

    }

}