package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import qna.CannotDeleteException;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer1;
    private Answer answer2;

    private Question question1;
    private Question question2;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User("id1", "password1", "name1", "email1");
        user2 = new User("id2", "password2", "name2", "email2");
        entityManager.persist(user1);
        entityManager.persist(user2);

        question1 = new Question("title1", "contents1", user1);
        question2 = new Question("title2", "contents2", user2);
        entityManager.persist(question1);
        entityManager.persist(question2);

        answer1 = new Answer(user1, question1, "Answers Contents1");
        answer2 = new Answer(user2, question2, "Answers Contents2");
    }

    @DisplayName("Entity 데이터를 DB에 저장하는 테스트")
    @Test
    void save() {
        // given
        final Answer actual = answerRepository.save(answer1);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull()
        );
    }

    @DisplayName("DB에 저장할 때 반환된 Entity 와 DB 에서 조회한 데이터가 일치하는지 확인하는 테스트")
    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(answer1);

        // when
        final Optional<Answer> optAnswer = answerRepository.findById(expected.getId());
        final Answer actual = optAnswer.orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @DisplayName("DB에 저장된 데이터 수를 확인하는 테스트")
    @Test
    void count() {
        // given
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        // when
        final long actual = answerRepository.count();

        // when
        assertThat(actual).isEqualTo(2);
    }

    @DisplayName("deleted 값을 변경하면 조회 결과가 달라지는지 테스트")
    @Test
    void findByIdAndDeletedFalse() throws CannotDeleteException {
        // given
        final Answer savedAnswer = answerRepository.save(answer1);
        final Answer savedAnswer2 = answerRepository.save(answer2);
        savedAnswer2.delete(user2);

        // when
        final Optional<Answer> optionalAnswer = answerRepository.findByWriterAndDeletedFalse(user1);
        final Optional<Answer> optionalAnswer2 = answerRepository.findByWriterAndDeletedFalse(user2);

        // then
        final Answer actual = optionalAnswer.orElseThrow(IllegalArgumentException::new);
        assertThat(actual).isEqualTo(savedAnswer);
        assertThatThrownBy(() ->
            optionalAnswer2.orElseThrow(IllegalArgumentException::new)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Question ID와 deleted 값을 각각 설정해서 조회하는 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() throws CannotDeleteException {
        // given
        final Answer savedAnswer = answerRepository.save(answer1);
        final Answer savedAnswer2 = answerRepository.save(answer2);
        savedAnswer2.delete(user2);

        // when
        final List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(question1);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0)).isEqualTo(savedAnswer)
        );
    }

    @DisplayName("saveAll 과 findAll 테스트")
    @Test
    void findAll() {
        // given
        final List<Answer> answers = Arrays.asList(answer1, answer2);
        final List<Answer> savedAnswers = answerRepository.saveAll(answers);

        // when
        final List<Answer> findAll = answerRepository.findAll();

        // then
        assertAll(
            () -> assertThat(savedAnswers.size()).isEqualTo(2),
            () -> assertThat(findAll.size()).isEqualTo(2),
            () -> assertThat(savedAnswers).isEqualTo(findAll)
        );
    }

    @Test
    void delete() {
        // given
        final Answer savedAnswer1 = answerRepository.save(answer1);
        final Answer savedAnswer2 = answerRepository.save(answer2);
        answerRepository.delete(savedAnswer1);
        answerRepository.deleteById(savedAnswer2.getId());

        // when
        final long actual = answerRepository.count();

        // then
        assertThat(actual).isEqualTo(0);
    }

    @DisplayName("EntityManager#getReference 로 존재하지 않는 Entity 를 조회해서 결과를 확인하는 테스트")
    @Test
    void getReference() {
        // given
        final EntityManager em = this.entityManager.getEntityManager();
        final long id = 1L;

        // when
        final Answer actual = em.getReference(Answer.class, id);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThatThrownBy(actual::getContents)
        );
    }

    @DisplayName("EntityManager#find 로 Entity 를 조회한 결과가 EntityManager#getReference 결과와 같은지 확인하는 테스트")
    @Test
    void find() {
        // given
        final EntityManager entityManager = this.entityManager.getEntityManager();
        entityManager.persist(answer1);
        final long id = answer1.getId();
        final Answer proxy = entityManager.getReference(Answer.class, id);

        // when
        final Answer actual = entityManager.find(Answer.class, id);

        // then
        assertAll(
            () -> assertThat(proxy.getId()).isEqualTo(id),
            () -> assertThat(proxy.getContents()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(proxy.getContents()),
            () -> assertThat(actual).isEqualTo(proxy),
            () -> assertThat(actual == proxy).isTrue()
        );
    }

    @DisplayName("Question 을 저장하면 연관 관계를 맺은 Answer 도 같이 저장되는지 테스트")
    @Test
    void cascadeSave() {
        // given
        final Question question = new Question("title", "content", user1);
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        // when
        final Question actual = questionRepository.save(question);

        // then
        assertAll(
            () -> assertThat(actual.countOfAnswer()).isEqualTo(2),
            () -> assertThat(answerRepository.findAll().size()).as("DB 에 저장된 Answer 개수 확인").isEqualTo(2)
        );
    }

    @DisplayName("Question 을 연관 관계를 맺은 Answer 도 삭제되는지 테스트")
    @Test
    void cascadeDelete() {
        // given
        final Question question = new Question("title", "content", user1);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        questionRepository.save(question);

        // when
        questionRepository.delete(question);

        // then
        assertAll(
            () -> assertThat(answerRepository.findAll().size()).as("DB 에 저장된 Answer 개수 확인").isEqualTo(0),
            () -> assertThatThrownBy(() -> questionRepository.findById(question.getId())
                .orElseThrow(NullPointerException::new))
                .isInstanceOf(NullPointerException.class)
        );
    }

    @DisplayName("Question 을 연관 관계를 맺은 Answer 도 업데이트되는지 테스트")
    @Test
    void cascadeUpdate() {
        // given
        final Question question = new Question("title", "content", user1);
        question.addAnswer(answer1);
        question.addAnswer(answer2);
        questionRepository.save(question);
        final String newContent = "new content";
        answer1.setContents(newContent);

        // when
        questionRepository.save(question);

        // then
        final Answer actual = answerRepository.findById(answer1.getId()).orElseThrow(NullPointerException::new);
        assertAll(
            () -> assertThat(actual.getContents()).isEqualTo(newContent)
        );
    }
}
