package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
        savedQ1.addAnswer(new Answer(savedJavajigi, "Answers Contents1").writeBy(savedJavajigi));
        savedQ2.addAnswer(new Answer(savedSanjigi, "Answers Contents2").writeBy(savedSanjigi));
        answerRepository.flush();
    }

    @Test
    @DisplayName("Answer 저장")
    void save() {
        // when
        final Answer answer = answerRepository.save(new Answer(savedSanjigi, "Answers Contents1"));

        // then
        assertThat(answer).isNotNull();
    }

    @Test
    @DisplayName("id 와 delete = false 인 Answer 들 조회")
    void findByIdAndDeletedFalse() {
        // when
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(savedQ1.getAnswers().get(0).getId());

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("Question 에서 answer 정보 조회")
    void getAnswer() {
        // given
        final Answer answer = new Answer(savedJavajigi, "Answers Contents1").writeBy(savedJavajigi);
        savedQ1.addAnswer(answer);
        answerRepository.flush();

        // when & then
        assertThat(savedQ1.getAnswers()).contains(answerRepository.findById(answer.getId()).get());
    }

    @Test
    @DisplayName("Answer contents 내용 변경")
    void update() {
        // given
        final Answer answer = savedQ1.getAnswers().get(0);

        // when
        answer.setContents("contents 변경");

        // then
        assertThat(answer.getContents()).isEqualTo("contents 변경");
    }

    @Test
    @DisplayName("Answer 삭제")
    void delete() {
        // bdd
        final Answer answer = savedQ1.getAnswers().get(0);

        // when
        answerRepository.delete(answer);

        // then
        assertThat(answerRepository.findById(answer.getId())).isEmpty();
    }
}
