package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.EntityManagerHelper;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private EntityManagerHelper entityManagerHelper;

    private User savedUser;
    private Question savedQuestion;

    private Answer answer;
    @BeforeEach
    void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        savedUser = userRepository.save(new User("USER", "PASSWORD", "NAME", "EMAIL@EMAIL.COM"));
        savedQuestion = questionRepository.save(new Question("title", "contents", savedUser));

        answer = new Answer(savedUser, savedQuestion, "contents");
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Answer savedAnswer = answerRepository.save(answer);
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(savedAnswer)
                .isSameAs(foundAnswer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByQuestionIdAndDeletedFalse는 찾지 못한다")
    void 삭제가_되어있으면_findByQuestionIdAndDeletedFalse는_찾지_못한다() throws CannotDeleteException {
        Answer savedAnswer = answerRepository.save(answer);
        Answer deletedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        deletedAnswer.delete(savedUser);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId()))
                .containsExactly(savedAnswer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    void 삭제가_되어있으면_findByIdAndDeletedFalse는_찾지_못한다() throws CannotDeleteException {
        Answer savedAnswer = answerRepository.save(answer);
        Answer deletedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        deletedAnswer.delete(savedUser);

        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId()))
                .isNotPresent();
        assertThat(answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()))
                .isPresent();
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        Answer savedAnswer = answerRepository.save(answer);

        entityManagerHelper.flushAndClear();

        Answer foundAnswer = answerRepository.findById(savedAnswer.getId())
                .orElseThrow(EntityNotFoundException::new);

        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundAnswer.isOwner(foundUser))
                .isTrue();
    }

    @Test
    @DisplayName("Question와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void Question와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        Answer savedAnswer = answerRepository.save(answer);

        entityManagerHelper.flushAndClear();

        Answer foundAnswer = answerRepository.findById(savedAnswer.getId())
                .orElseThrow(EntityNotFoundException::new);

        Question foundQuestion = questionRepository.findById(savedQuestion.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundAnswer.isAnswerOf(foundQuestion))
                .isTrue();
    }
}
