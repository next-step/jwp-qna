package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import qna.config.JpaAuditingConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
@DirtiesContext
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("질문 저장 성공")
    void save() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);

        entityManager.persist(writer);
        Question questionEntity = questionRepository.save(question);

        //expect
        assertAll(
                () -> assertThat(questionEntity).isNotNull(),
                () -> assertThat(questionEntity.getId()).isNotNull(),
                () -> assertThat(questionEntity.getCreatedAt()).isNotNull(),
                () -> assertThat(questionEntity.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("question에서 answer 양방향 매핑 확인")
    void answer_양방향_매핑_확인() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        entityManager.persist(writer);
        entityManager.persist(answer);
        questionRepository.save(question);
        entityManager.flush();
        entityManager.clear();

        //when
        Optional<Question> optionalQuestion = questionRepository.findById(question.getId());

        //then
        assertThat(optionalQuestion.get().getAnswers()).hasSize(1);
    }

    @Test
    @DisplayName("findByDeletedFalse 조회")
    void findByDeletedFalse() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question q1 = new Question("title1", "contents1").writeBy(writer);
        Question q2 = new Question("title2", "contents2").writeBy(writer);

        entityManager.persist(writer);
        questionRepository.save(q1);
        questionRepository.save(q2);

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions).hasSize(2);
        assertThat(questions.stream().noneMatch(question -> question.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByDeletedFalse는 Deleted true면 조회되지 않는다.")
    void findByDeletedFalse_delete_검증() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question q1 = new Question("title1", "contents1").writeBy(writer);
        Question q2 = new Question("title2", "contents2").writeBy(writer);

        entityManager.persist(writer);
        questionRepository.save(q1);
        q2 = questionRepository.save(q2);
        q2.setDeleted(true);

        // when
        List<Question> questions = questionRepository.findByDeletedFalse();

        // then
        assertThat(questions).hasSize(1);
        assertThat(questions.stream().noneMatch(question -> question.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 조회")
    void findByIdAndDeletedFalse() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question q1 = new Question("title1", "contents1").writeBy(writer);

        entityManager.persist(writer);
        q1 = questionRepository.save(q1);

        // when
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q1.getId());

        // then
        assertThat(question).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse는 Deleted true면 조회되지 않는다.")
    void findByIdAndDeletedFalse_delete_검증() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question q1 = new Question("title1", "contents1").writeBy(writer);
        Question q2 = new Question("title2", "contents2").writeBy(writer);

        entityManager.persist(writer);
        questionRepository.save(q1);
        q2 = questionRepository.save(q2);
        q2.setDeleted(true);

        // when
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q2.getId());

        // then
        assertThat(question).isNotPresent();
    }

}
