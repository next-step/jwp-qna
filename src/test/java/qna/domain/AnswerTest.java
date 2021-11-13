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
    private AnswerRepository answers;

    @Test
    @DisplayName("저장됐는지 확인")
    void 저장() {
        Answer expected = new Answer(UserTest.JENNIE, QuestionTest.Q1, "New Answer Contents");
        Answer actual = answers.save(expected);
        assertAll(() -> assertThat(actual).isEqualTo(expected), 
                () -> assertThat(expected.getId()).isNotNull(), 
                () -> assertThat(expected.getId()).isEqualTo(actual.getId()));
    }

    @Test
    @DisplayName("아이디로 조회")
    void 아이디로_조회() {
        Answer expected = new Answer(UserTest.JENNIE, QuestionTest.Q1, "New Answer Contents");
        answers.save(expected);
        Optional<Answer> actual = answers.findById(expected.getId());
        assertAll(() -> assertThat(actual.isPresent()).isTrue(), 
                () -> assertThat(actual.orElse(null)).isEqualTo(expected));
    }
    
    @Test
    @DisplayName("답변을 삭제하면 삭제 상태로 변경")
    void 삭제_상태_확인() {
        Answer expected = new Answer(UserTest.JENNIE, QuestionTest.Q1, "이렇게 하시면 됩니다~");
        answers.save(expected);
        expected.delete();
        assertThat(answers.findById(expected.getId()).get().isDeleted()).isTrue();
    }
    
    @Test
    @DisplayName("답변 삭제시 삭제 이력 확인")
    void 삭제_이력_확인() {
        Answer expected = new Answer(UserTest.JENNIE, QuestionTest.Q1, "이렇게 하시면 됩니다~");
        answers.save(expected);
        assertThat(expected.delete()).isNotNull();
    }
}
