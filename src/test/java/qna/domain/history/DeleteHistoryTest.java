package qna.domain.history;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.common.ContentType;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryTest {

    public static final DeleteHistory questiDelete =
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory answerDelete =
            new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

    public static final DeleteHistory createQuestiDelete(){
        return new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    }
    public static final DeleteHistory createAnswerDelete(){
        return new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    }
}
