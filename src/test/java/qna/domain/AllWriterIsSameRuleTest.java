package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.JAVAJIGI;

class AllWriterIsSameRuleTest {
    @Test
    @DisplayName("질문 작성자와 답변 작성자가 모두 같으면 삭제가능하다고 판단함")
    void test1() throws CannotDeleteException {
        Question question = new Question("title", "contents", JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(JAVAJIGI, question, ""),
                new Answer(JAVAJIGI, question, "")
                );
        AllWriterIsSameRule allWriterIsSameRule = new AllWriterIsSameRule();
        boolean deletable = allWriterIsSameRule.deletable(JAVAJIGI, answers);

        assertThat(deletable).isTrue();
    }
}