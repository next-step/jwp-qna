package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

    @Test
    void 질문_삭제이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        LocalDateTime now = LocalDateTime.now();
        DeleteHistory actualDeleteHistory = DeleteHistory.createDeleteHistory(ContentType.QUESTION, question.getId(), writer, now);

        //when
        DeleteHistory expectDeleteHistory = DeleteHistory.createDeleteHistory(ContentType.QUESTION, question.getId(), writer, now);

        //then
        assertThat(actualDeleteHistory).isEqualTo(expectDeleteHistory);
    }

    @Test
    void 답변_삭제이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "답변 삭제이력 추가 테스트");
        LocalDateTime now = LocalDateTime.now();
        DeleteHistory actualDeleteHistory = DeleteHistory.createDeleteHistory(ContentType.ANSWER, answer.getId(), writer, now);

        //when
        DeleteHistory expectDeleteHistory = DeleteHistory.createDeleteHistory(ContentType.ANSWER, answer.getId(), writer, now);

        //then
        assertThat(actualDeleteHistory).isEqualTo(expectDeleteHistory);
    }
}
