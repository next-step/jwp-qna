package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("저장한_객체와_저장된_객체_비교")
    @Test
    void 저장한_객체와_저장된_객체_비교() {
        Answer answer = new Answer("zz", LocalDateTime.now(), false, 1L, LocalDateTime.now(), 1L);
        Answer actual = answerRepository.save(answer);
        assertThat(actual).isEqualTo(answer);
    }

    @DisplayName("not null 컬럼에 null을 저장")
    @Test
    void notNull_컬럼에_null을_저장() {
        Answer answer = new Answer("zz", null, false, 1L, LocalDateTime.now(), 1L);

        assertThatThrownBy(()-> answerRepository.save(answer)).isInstanceOf(Exception.class);
    }

    @DisplayName("update 테스트(변경감지)")
    @Test
    void update() {
        Answer answer = new Answer("zz", LocalDateTime.now(), false, 1L, LocalDateTime.now(), 1L);
        Answer actual = answerRepository.save(answer);

        answer.setContents("gtgt");
        assertThat(actual.getContents()).isEqualTo("gtgt");
    }
}
