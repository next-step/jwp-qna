package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoryTest {

    private ContentType questionType;
    private long contentId;
    private User deleteUser;
    private LocalDateTime createDate;

    @BeforeEach
    public void setup(){
        questionType = ContentType.QUESTION;
        contentId = 1L;
        deleteUser = UserTest.JAVAJIGI;
        createDate = LocalDateTime.now();
    }

    @Test
    @DisplayName("삭제 이력 생성")
    public void createDeleteHistory(){
        DeleteHistory deleteHistory = new DeleteHistory(questionType, contentId, deleteUser, createDate);
        assertThat(deleteHistory.equals(new DeleteHistory(questionType, contentId, deleteUser, createDate))).isTrue();
    }

    @Test
    @DisplayName("질문 삭제 이력 생성")
    public void questionDeleteHistory(){
        DeleteHistory deleteHistory = DeleteHistory.questionHistory(contentId, deleteUser);
        assertThat(deleteHistory.equals(new DeleteHistory(questionType, contentId, deleteUser, createDate))).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 이력 생성")
    public void answerDeleteHistory(){
        DeleteHistory deleteHistory = DeleteHistory.answerHistory(contentId, deleteUser);
        assertThat(deleteHistory.equals(new DeleteHistory(ContentType.ANSWER, contentId, deleteUser, createDate))).isTrue();
    }

}
