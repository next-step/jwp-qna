package qna.assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import qna.domain.Answer;
import qna.domain.Question;
import qna.exception.CannotDeleteException;

public class QnaAssertions {
    public static void 질문삭제여부_검증(Question question){
        assertThat(question.isDeleted()).isTrue();
    }

    public static void 답변삭제여부_검증(Answer answer){
        assertThat(answer.isDeleted()).isTrue();
    }

    public static void 삭제불가_예외발생(ThrowingCallable 삭제시도){
        assertThatThrownBy(삭제시도).isInstanceOf(CannotDeleteException.class);
    }

}
