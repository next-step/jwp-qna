package qna.domain.history;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.common.ContentType;
import qna.domain.history.DeleteHistory;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryTest {
    
    public static final DeleteHistory questiDelete = 
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory answerDelete = 
            new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    
}
