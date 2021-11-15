package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        final Long id = answerRepository.save(A1).getId();
        assertThat(id).isEqualTo(1);
    }

    @Test
    public void findById() {
        final Answer answer = answerRepository.findById(answerRepository.save(A1).getId())
                .orElseThrow(NotFoundException::new);
        assertThat(answer.getWriterId()).isEqualTo(1L);
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Answer answer = answerRepository.save(A1);
        answer.setDeleted(true);
        assertThatThrownBy(() -> answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        final Question question = questionRepository.save(QuestionTest.Q1);
        final Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, question, "answer 111"));
        answer.setDeleted(true);
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId())).hasSize(0);
    }
}
