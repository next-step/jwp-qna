package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("답변 생성")
    void create() {
        //given
        //when
        Answer answer = answerRepository.save(A1);
        //then
        assertThat(answer.getId()).isEqualTo(A1.getId());
    }

    @Test
    @DisplayName("답변 조회 - id")
    void findById() {
        //given
        //when
        Answer save = answerRepository.save(A1);
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(save.getId());
        //then
        assertThat(answer.isPresent()).isTrue();
    }

    @Test
    @DisplayName("답변 조회 - questionId")
    void findByQuestionId() {
        //given
        //when
        Answer save = answerRepository.save(A1);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(save.getQuestionId());
        //then
        assertThat(answers.size() > 0).isTrue();
        assertThat(answers.get(0).getQuestionId()).isEqualTo(save.getQuestionId());
    }
}
