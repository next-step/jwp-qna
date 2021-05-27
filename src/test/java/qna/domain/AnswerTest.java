package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    void setUpInsert() {
        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @Test
    @DisplayName("Answer id로 Answer 조회 테스트")
    void findById() {
        Answer answer = answerRepository.findById(1L).get();
        assertAll(
            () -> assertThat(answer).isNotNull(),
            () -> assertThat(answer.getWriterId()).isEqualTo(A1.getId()),
            () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
            () -> assertThat(answer.isDeleted()).isEqualTo(false),
            () -> assertThat(answer.getCreateAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("Answer 수정 테스트")
    void update() {
        Answer answer = answerRepository.findById(1L).get();
        Date updateAt = new Date();
        answer.setContents("수정했어요");
        answer.setUpdateAt(updateAt);
        answerRepository.flush();
        Answer updatedAnswer = answerRepository.findById(1L).get();
        assertThat(updatedAnswer.getContents()).isEqualTo("수정했어요");
        assertThat(updatedAnswer.getUpdateAt()).isEqualTo(updateAt);
    }

    @Test
    @DisplayName("Answer 삭제 테스트")
    void delete() {
        Answer deleteAnswer = answerRepository.findById(2L).get();
        answerRepository.delete(deleteAnswer);
    }

}
