package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.user.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.user.domain.UserTest.JAVAJIGI;
import static qna.user.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question(null, "title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question(null, "title2", "contents2").writeBy(SANJIGI);
    public static final Question savedQuestion1 = new Question(1L, "title1", "content1");
    public static final Question savedQuestion2 = new Question(2L, "title2", "content2");

    @Test
    void 질문을_작성하면_작성한_사용자에게_할당되어야_한다() {
        Question question = Question.builder()
                .build();

        question.writeBy(UserTest.JAVAJIGI);

        assertThat(question.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @Test
    void 질문_작성자_여부를_확인하는_기능은_정상_동작해야_한다() {
        Question question = Question.builder()
                .build();

        question.writeBy(UserTest.JAVAJIGI);

        assertThat(question.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(question.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @Test
    void 질문을_삭제하면_삭제_여부가_true_이어야_한다() {
        Question question = Question.builder()
                .build();

        assertThat(question.isDeleted()).isFalse();

        question.questionDelete();
        assertThat(question.isDeleted()).isTrue();
    }
}
