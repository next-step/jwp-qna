package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answer;

    @Test
    @DisplayName("저장됐는지 확인")
    void 저장() {
        Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "New Answer Contents");
        Answer actual = answer.save(expected);
        assertAll(() -> assertThat(actual).isEqualTo(expected), 
                () -> assertThat(expected.getId()).isNotNull(), 
                () -> assertThat(expected.getId()).isEqualTo(actual.getId()));
    }

    @Test
    @DisplayName("아이디로 조회")
    void 아이디로_조회() {
        Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "New Answer Contents");
        answer.save(expected);
        Optional<Answer> actual = answer.findById(expected.getId());
        assertAll(() -> assertThat(actual.isPresent()).isTrue(), 
                () -> assertThat(actual.orElse(null)).isEqualTo(expected));
    }
    
    @Test
    @DisplayName("삭제됐는지 확인")
    void 삭제() {
        Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "New Answer Contents");
        answer.save(expected);
        answer.delete(expected);
        Optional<Answer> actual = answer.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
