package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.question.exception.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.user.domain.UserTest.JAVAJIGI;
import static qna.user.domain.UserTest.SANJIGI;

class AnswersTest {

    @Test
    void 답변_리스트를_삭제할_때_생성한_사용자와_다른_사람이_존재하면_예외가_발생해야_한다() {
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question();
        Answers answers = new Answers();
        answers.add(new Answer(null, JAVAJIGI, question, "Contents1"));
        answers.add(new Answer(null, SANJIGI, question, "Contents2"));

        assertThatThrownBy(() -> answers.deleteAll(JAVAJIGI, now))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변_리스트를_삭제할_때_생성한_사용자가_삭제할_경우_히스토리가_정상_생성되어야_한다() throws CannotDeleteException {
        LocalDateTime now = LocalDateTime.now();
        Question question = new Question();
        Answers answers = new Answers();
        answers.add(new Answer(null, JAVAJIGI, question, "Contents1"));
        answers.add(new Answer(null, JAVAJIGI, question, "Contents2"));

        List<DeleteHistory> result = answers.deleteAll(JAVAJIGI, now);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(result.get(1).getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
