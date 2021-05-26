package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

    private User user;
    private Question question;

    @BeforeEach
    public void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        user = userRepository.save(new User("USER_ID", "PASSWORD", "NAME", "EMAIL"));
        question = questionRepository.save(new Question("Hello", "Hello"));

        question.writeBy(user);
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        Question foundQuestion = questionRepository.findById(question.getId()).orElseThrow(EntityNotFoundException::new);

        assertSame(question, foundQuestion);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    void 삭제를_하지_않으면_findByIdAndDeletedFalse는_찾지_못한다() {
        Question deletedQuestion = questionRepository.save(new Question("Bye", "Bye"));

        deletedQuestion.setDeleted(true);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()))
                .isPresent();
        assertThat(questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId()))
                .isNotPresent();
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByDeletedFalse에는 포함이 되면 안된다")
    void 삭제가_되어있으면_findByDeletedFalse에는_포함이_되면_안된다() {
        assertThat(questionRepository.findByDeletedFalse())
                .containsExactlyInAnyOrder(question);
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        entityManagerHelper.flushAndClear();

        Question foundQuestion = questionRepository.findById(question.getId()).orElseThrow(EntityNotFoundException::new);

        assertThat(foundQuestion.getWriter().getId())
                .isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Answer와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void Answer와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        List<Answer> answers = Arrays.asList(
                new Answer(user, question, "CONTENTS1"),
                new Answer(user, question, "CONTENTS2"),
                new Answer(user, question, "CONTENTS3")
        );

        answers.forEach(question::addAnswer);

        entityManagerHelper.flushAndClear();

        List<Long> answersId = answers.stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());

        Question foundQuestion = questionRepository.findById(question.getId()).orElseThrow(EntityNotFoundException::new);

        assertThat(foundQuestion.getAnswers())
                .hasSize(3);
        assertThat(foundQuestion.getAnswers())
                .map(item -> item.getId())
                .containsExactlyInAnyOrderElementsOf(answersId);
    }
}
