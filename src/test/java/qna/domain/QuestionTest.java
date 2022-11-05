package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    void 질문자가_아니면_삭제_불가능() {
        assertThatThrownBy(() -> Q1.delete(SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 삭제후_삭제_내역_반환() {
        List<DeleteHistory> deleteHistories = Q1.delete(JAVAJIGI);
        assertThat(deleteHistories).isEqualTo(Collections.singletonList(
                new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriterId(), null)
        ));
    }

    @Test
    void 삭제(){
        Q1.delete(JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
    }
}
