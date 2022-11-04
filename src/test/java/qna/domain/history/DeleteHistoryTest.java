package qna.domain.history;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.common.ContentType;

import java.time.LocalDateTime;

import static qna.domain.user.UserTest.JAVAJIGI;

@DataJpaTest
public class DeleteHistoryTest {

    public static final DeleteHistory questiDelete =
            new DeleteHistory(ContentType.QUESTION, 1L, JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory answerDelete =
            new DeleteHistory(ContentType.ANSWER, 1L, JAVAJIGI, LocalDateTime.now());

}
