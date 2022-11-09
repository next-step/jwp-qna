package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    @Test
    @DisplayName("삭제 내역 생성")
    void create() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, writer.getId(), writer, LocalDateTime.now());

        //expect
        assertThat(deleteHistory).isNotNull();
    }

    @Test
    @DisplayName("Question을 받아 DeleteHistory를 생성")
    void 정적_팩토리_메소드_Question() {
        //given
        Question question = new Question("title1", "contents1");

        //expect
        assertThat(DeleteHistory.from(question)).isNotNull();
    }

    @Test
    @DisplayName("Answer를 받아 DeleteHistory를 생성")
    void 정적_팩토리_메소드_Answer() {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(1L, writer, question, "Answers Contents");

        //expect
        assertThat(DeleteHistory.from(answer)).isNotNull();
    }
}