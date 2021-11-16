package qna.fixture;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;

public class DeleteHistoryFixture {
    private DeleteHistoryFixture() {
        throw new UnsupportedOperationException();
    }

    public static DeleteHistory ID가_없는_사용자의_질문_삭제_히스토리() {
        return new DeleteHistory(ContentType.QUESTION, 1L, UserFixture.ID가_없는_사용자());
    }

    public static DeleteHistory ID가_없는_사용자의_답변_삭제_히스토리() {
        return new DeleteHistory(ContentType.ANSWER, 2L, UserFixture.ID가_없는_사용자());
    }
}
