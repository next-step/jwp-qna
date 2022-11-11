package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnswerTest {

    private User writer;
    private Question question;
    private Answer answer1;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(writer);
        answer1 = new Answer(writer, question, "Answers Contents1");
    }

    @Test
    @DisplayName("답변의 작성자 일치 여부")
    void answer_작성자_일치_여부() {
        assertTrue(answer1.isOwner(writer));
    }

    @Test
    @DisplayName("답변에 질문을 할당한다.")
    void answer_질문_일치_여부() {
        answer1.toQuestion(question);
        assertThat(answer1.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void answer_삭제() {
        answer1.delete();
        assertTrue(answer1.isDeleted());
    }
}
