package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    private User user;
    private Question question;
    private Answer answer;
    private Answer deletedAnswer;

    @BeforeEach
    void setUp() {
        user = new User("id1234", "password", "name", "mail@email.com");
        question = new Question("질문", "질문 내용");
        answer = new Answer(user, question, "답변 내용");
        deletedAnswer = new Answer(user, question, "답변 내용2");
        deletedAnswer.delete();
    }

    @DisplayName("답변을 저장한다.")
    @Test
    void save() {
        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull(),
                () -> assertThat(savedAnswer.getUpdatedAt()).isNotNull(),
                () -> assertThat(savedAnswer).isEqualTo(answer)
        );
    }

    @DisplayName("질문으로 삭제되지 않은 답변 리스트를 조회한다.")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        answerRepository.save(answer);
        answerRepository.save(deletedAnswer);

        // when
        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(answer.getQuestion());

        // then
        assertThat(answers).containsExactly(answer);
    }

    @DisplayName("회원의 삭제되지 않은 답변 리스트를 조회한다.")
    @Test
    void findByWriter() {
        // given
        answerRepository.save(answer);
        answerRepository.save(deletedAnswer);

        // when
        List<Answer> answers = answerRepository.findByWriterAndDeletedFalse(answer.getWriter());

        // then
        assertThat(answers).containsExactly(answer);
    }

    @DisplayName("답변ID로 답변을 조회한다.")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Answer savedAnswer = answerRepository.save(answer);

        // when
        Answer foundAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        // then
        assertThat(foundAnswer).isEqualTo(savedAnswer);
    }

}
