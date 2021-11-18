package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");

    @DisplayName("Answer 값 확인")
    @Test
    void init() {
        assertAll(
            () -> assertThat(A1.getWriter()).isEqualTo(UserTest.JAVAJIGI),
            () -> assertThat(A1.getQuestion()).isEqualTo(QuestionTest.Q1),
            () -> assertThat(A1.getContents()).isEqualTo("Answers Contents1")
        );
    }

}
