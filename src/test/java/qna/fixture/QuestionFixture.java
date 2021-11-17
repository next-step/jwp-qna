package qna.fixture;

import qna.domain.Question;

public class QuestionFixture {
    private QuestionFixture() {
        throw new UnsupportedOperationException();
    }

    public static Question ID가_없는_사용자의_질문ID가_없는_질문() {
        return new Question(null, "title", "contents").writeBy(UserFixture.ID가_없는_사용자());
    }
}
