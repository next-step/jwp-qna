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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private EntityManagerHelper entityManagerHelper;

    private User savedUser;

    private Question question;

    @BeforeEach
    public void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        savedUser = userRepository.save(new User("USER_ID", "PASSWORD", "NAME", "EMAIL"));

        question = new Question("Hello", "Hello", savedUser);
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Question savedQuestion = questionRepository.save(question);

        Question foundQuestion = questionRepository.findById(question.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundQuestion)
                .isSameAs(savedQuestion);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    void 삭제를_하지_않으면_findByIdAndDeletedFalse는_찾지_못한다() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question);
        Question deletedQuestion = questionRepository.save(new Question("Bye", "Bye", savedUser));

        deletedQuestion.delete(savedUser);

        assertThat(questionRepository.findByIdAndDeletedFalse(savedQuestion.getId()))
                .isPresent();
        assertThat(questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId()))
                .isNotPresent();
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByDeletedFalse에는 포함이 되면 안된다")
    void 삭제가_되어있으면_findByDeletedFalse에는_포함이_되면_안된다() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question);
        Question deletedQuestion = questionRepository.save(new Question("Bye", "Bye", savedUser));

        deletedQuestion.delete(savedUser);

        assertThat(questionRepository.findByDeletedFalse())
                .containsExactlyInAnyOrder(savedQuestion);
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        Question savedQuestion = questionRepository.save(question);

        entityManagerHelper.flushAndClear();

        Question foundQuestion = questionRepository.findById(savedQuestion.getId())
                .orElseThrow(EntityNotFoundException::new);

        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundQuestion.isOwner(foundUser))
                .isTrue();
    }

    @Test
    @DisplayName("Answer와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void Answer와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        Question savedQuestion = questionRepository.save(question);

        List<Answer> answers = Arrays.asList(
                new Answer(savedUser, question, "CONTENTS1"),
                new Answer(savedUser, question, "CONTENTS2"),
                new Answer(savedUser, question, "CONTENTS3")
        );

        answers.forEach(savedQuestion::addAnswer);

        entityManagerHelper.flushAndClear();

        List<Long> answersId = answers.stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());

        Question foundQuestion = questionRepository.findById(question.getId()).orElseThrow(EntityNotFoundException::new);

        assertThat(foundQuestion.getAnswers().toCollection())
                .map(item -> item.getId())
                .containsExactlyInAnyOrderElementsOf(answersId);
    }
}
