package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

/**
 * DeleteHistories 객체 생성자 및 기능 테스트
 */
public class DeleteHistoriesTest {

    @Test
    @DisplayName("생성된 삭제이력 목록 꺼내기")
    void getDeleteHistories() throws CannotDeleteException {
        //given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        DeleteHistories deleteHistories = new DeleteHistories(QuestionTest.Q1, answers, UserTest.JAVAJIGI);

        // when
        List<DeleteHistory> histories = deleteHistories.getDeleteHistories();

        // then
        assertThat(histories).extracting("contentType").contains(ContentType.ANSWER, ContentType.QUESTION);
    }
}
