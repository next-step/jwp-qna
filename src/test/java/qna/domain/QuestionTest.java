package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("저장")
    void save() {
        //given
        Question question = questionRepository.save(Q1);

        //expect
        assertThat(question).isNotNull();
    }

    @Test
    @DisplayName("findByDeletedFalse 조회")
    void findByDeletedFalse() {
        // given
        questionRepository.save(Q1);
        questionRepository.save(Q2);

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
        questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
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
        questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        // when
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q2.getId());

        // then
        assertThat(question).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse는 Deleted true면 조회되지 않는다.")
    void findByIdAndDeletedFalse_delete_검증() {
        // given
        questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
        q2.setDeleted(true);

        // when
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(q2.getId());

        // then
        assertThat(question).isNotPresent();
    }

}
