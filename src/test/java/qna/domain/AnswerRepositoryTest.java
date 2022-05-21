package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        // given
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    @DisplayName("Answer 저장")
    void save() {
        // when
        final Answer answer = answerRepository.save(new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1"));

        // then
        assertThat(answer).isNotNull();
    }

    @Test
    @DisplayName("QuestionId 와 delete = false 인 Answer 들 조회")
    void findByQuestionIdAndDeletedFalse() {
        // when
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(Q1.getId());
        answerRepository.flush();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("id 와 delete = false 인 Answer 들 조회")
    void findByIdAndDeletedFalse() {
        // when
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(A1.getId());

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(A1);
    }

    @Test
    @DisplayName("Answer contents 내용 변경")
    void update() {
        // when
        A1.setContents("contents 변경");
        answerRepository.flush();

        // then
        assertThat(A1.getContents()).isEqualTo("contents 변경");
    }

    @Test
    @DisplayName("Answer 삭제")
    void delete() {
        // when
        answerRepository.delete(A1);
        answerRepository.flush();

        // then
        assertThat(answerRepository.findById(A1.getId())).isEmpty();
    }
}