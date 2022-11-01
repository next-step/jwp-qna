package qna.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        Answer expected = answerRepository.save(A1);
        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(expected.getWriterId()).isEqualTo(A1.getWriterId())
        );
    }

    @Test
    @DisplayName("식별자로 답변을 조회한다.")
    void findById() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        Answer answer = answerRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        answerRepository.delete(expected);

        assertThat(answerRepository.findById(expected.getId())).isEmpty();
    }

}
