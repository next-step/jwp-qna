package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

import java.util.Arrays;
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
class AnswerRepositoryTest extends BaseRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    Question savedQ1;

    Question savedQ2;

    @BeforeEach
    void setUp() {
        saveUsers();
        savedQ1 = questionRepository.save(new Question("title1", "contents1").writeBy(savedJavajigi));
        savedQ2 = questionRepository.save(new Question("title2", "contents2").writeBy(savedSanjigi));
        savedQ1.addAnswer(A1);
        savedQ2.addAnswer(A2);
        answerRepository.saveAll(Arrays.asList(A1.writeBy(savedJavajigi), A2.writeBy(savedSanjigi)));
    }

    @Test
    @DisplayName("Answer 저장")
    void save() {
        // when
        final Answer answer = answerRepository.save(new Answer(savedSanjigi, Q1, "Answers Contents1"));

        // then
        assertThat(answer).isNotNull();
    }

    @Test
    @DisplayName("QuestionId 와 delete = false 인 Answer 들 조회")
    void findByQuestionIdAndDeletedFalse() {
        // when
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(savedQ1.getId());
        answerRepository.flush();

        // then
        assertThat(actual).hasSize(1);
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
    @DisplayName("Question 에서 answer 정보 조회")
    void getAnswer() {
        // given
        final Question question = questionRepository.findById(savedQ1.getId()).get();

        // when & then
        assertThat(question.getAnswers()).hasSize(1);
        assertThat(question.getAnswers()).contains(A1);
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
