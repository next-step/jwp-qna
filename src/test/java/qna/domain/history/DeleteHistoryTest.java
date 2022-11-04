package qna.domain.history;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.common.ContentType;
import qna.domain.user.UserTest;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryTest {

    public static final DeleteHistory createQuestiDelete() {
        return new DeleteHistory(ContentType.QUESTION, 1L, UserTest.createUser("user1"), LocalDateTime.now());
    }

    public static final DeleteHistory createAnswerDelete() {
        return new DeleteHistory(ContentType.ANSWER, 1L, UserTest.createUser("user1"), LocalDateTime.now());
    }

}
