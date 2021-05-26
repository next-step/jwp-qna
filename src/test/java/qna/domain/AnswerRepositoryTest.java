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

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        entityManagerHelper = new EntityManagerHelper(entityManager);

        user = userRepository.save(new User("USER", "PASSWORD", "NAME", "EMAIL"));
        question = questionRepository.save(new Question("title", "contents"));
        answer = answerRepository.save(new Answer(user, question, "contents"));
    }

    @Test
    @DisplayName("저장을 하고, 다시 가져왔을 때 원본 객체와 같아야 한다")
    void 저장을_하고_다시_가져왔을_때_원본_객체와_같아야_한다() {
        assertThat(answer)
                .isSameAs(answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new));
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByQuestionIdAndDeletedFalse는 찾지 못한다")
    void 삭제가_되어있으면_findByQuestionIdAndDeletedFalse는_찾지_못한다() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
                .containsExactly(answer);
    }

    @Test
    @DisplayName("삭제가 되어있으면, findByIdAndDeletedFalse는 찾지 못한다")
    void 삭제가_되어있으면_findByIdAndDeletedFalse는_찾지_못한다() {
        Answer deletedAnswer = answerRepository.save(new Answer(user, question, "contents"));
        deletedAnswer.setDeleted(true);

        assertThat(answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId()))
                .isNotPresent();
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()))
                .isPresent();
    }

    @Test
    @DisplayName("User와 연관관계를 맺었을 때, 다시 가져올 때도 맺혀있어야 한다")
    void User와_연관관계를_맺었을_때_다시_가져올_때도_맺혀있어야_한다() {
        entityManagerHelper.flushAndClear();

        Answer foundAnswer = answerRepository.findById(answer.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertThat(foundAnswer.getWriter())
                .isEqualTo(userRepository.findById(user.getId()).orElseThrow(EntityNotFoundException::new));
    }
}
