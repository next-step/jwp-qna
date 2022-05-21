package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    public void setup() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
    }

    @Test
    void 답변_등록_테스트() {
        Answer answer3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3");
        answerRepository.save(answer3);

        assertThat(answer3.getContents()).isEqualTo("Answers Contents3");
    }

    @Test
    void findByQuestionIdAndDeletedFalse_테스트() {
        List<Answer> byQuestionIdAndDeletedFalse = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(byQuestionIdAndDeletedFalse.size()).isEqualTo(2);
    }

    @Test
    void findByIdAndDeletedFalse_테스트() {
        Answer answer3 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents3");
        answerRepository.save(answer3);
        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(answer3.getId());

        assertThat(byIdAndDeletedFalse.isPresent()).isTrue();
        assertThat(byIdAndDeletedFalse.get()).isEqualTo(answer3);
    }
}
