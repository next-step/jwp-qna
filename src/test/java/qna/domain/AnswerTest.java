package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void beforeEach() {
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    void findById() {
        Answer answer1 = answerRepository.findById(1L).get();
        Answer answer2 = answerRepository.findById(2L).get();
        assertThat(answer1.getWriterId()).isEqualTo(A1.getId());
        assertThat(answer2.getWriterId()).isEqualTo(A2.getId());
        assertThat(answer1.getQuestionId()).isEqualTo(A1.getQuestionId());
        assertThat(answer2.getQuestionId()).isEqualTo(A2.getQuestionId());
        assertThat(answer1.getContents()).isEqualTo(A1.getContents());
        assertThat(answer2.getContents()).isEqualTo(A2.getContents());
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(answers).isNotEmpty();
        assertThat(answers.get(0).getQuestionId()).isEqualTo(A1.getQuestionId());
        assertThat(answers.get(1).getQuestionId()).isEqualTo(A2.getQuestionId());
        assertThat(answers.get(0).getWriterId()).isEqualTo(A1.getWriterId());
        assertThat(answers.get(1).getWriterId()).isEqualTo(A2.getWriterId());
        assertThat(answers.get(0).getContents()).isEqualTo(A1.getContents());
        assertThat(answers.get(1).getContents()).isEqualTo(A2.getContents());
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer answer1 = answerRepository.findByIdAndDeletedFalse(1L).get();
        Answer answer2 = answerRepository.findByIdAndDeletedFalse(2L).get();
        assertThat(answer1).isNotNull();
        assertThat(answer2).isNotNull();
        assertThat(answer1.getQuestionId()).isEqualTo(A1.getQuestionId());
        assertThat(answer2.getQuestionId()).isEqualTo(A2.getQuestionId());
        assertThat(answer1.getWriterId()).isEqualTo(A1.getWriterId());
        assertThat(answer2.getWriterId()).isEqualTo(A2.getWriterId());
        assertThat(answer1.getContents()).isEqualTo(A1.getContents());
        assertThat(answer2.getContents()).isEqualTo(A2.getContents());
    }
}
