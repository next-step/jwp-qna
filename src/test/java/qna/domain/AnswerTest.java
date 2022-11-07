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
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("답변 내역 저장 성공")
    void save() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        entityManager.persist(writer);
        entityManager.persist(question);
        Answer answerEntity = answerRepository.save(answer);

        //expect
        assertAll(
                () -> assertThat(answerEntity).isNotNull(),
                () -> assertThat(answerEntity.getId()).isNotNull(),
                () -> assertThat(answerEntity.getCreatedAt()).isNotNull(),
                () -> assertThat(answerEntity.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse 조회")
    void findByQuestionIdAndDeletedFalse() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(writer, question, "Answers Contents2");

        entityManager.persist(writer);
        entityManager.persist(question);
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        // then
        assertThat(answers).hasSize(2);
        assertThat(answers.stream().noneMatch(answer -> answer.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse은 delete False만 조회한다.")
    void findByQuestionIdAndDeletedFalse_delete_검증() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer1 = new Answer(writer, question, "Answers Contents1");
        Answer answer2 = new Answer(writer, question, "Answers Contents2");

        entityManager.persist(writer);
        entityManager.persist(question);
        answerRepository.save(answer1);
        answer2 = answerRepository.save(answer2);
        answer2.setDeleted(true);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        // then
        assertThat(answers).hasSize(1);
        assertThat(answers.stream().noneMatch(answer -> answer.isDeleted())).isTrue();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 조회")
    void findByIdAndDeletedFalse() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents1");

        entityManager.persist(writer);
        entityManager.persist(question);
        answerRepository.save(answer);

        // when
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        // then
        assertThat(optionalAnswer).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse는 Id가 일치하더라도 Delete Flase면 조회되지 않는다.")
    void findByIdAndDeletedFalse_delete_검증() {
        // given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents1");

        entityManager.persist(writer);
        entityManager.persist(question);
        answerRepository.save(answer);
        answer.setDeleted(true);

        // when
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        // then
        assertThat(optionalAnswer).isNotPresent();
    }


}
