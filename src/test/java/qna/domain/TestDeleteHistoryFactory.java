package qna.domain;

import java.time.LocalDateTime;

public class TestDeleteHistoryFactory {

    public static DeleteHistory createQuestionDeleteHistory(Long contentId, User writer) {
        return DeleteHistory.ofQuestion(contentId, writer);
    }

    public static DeleteHistory createAnswerDeleteHistory(Long contentId, User writer) {
        return DeleteHistory.ofAnswer(contentId, writer);
    }
}
