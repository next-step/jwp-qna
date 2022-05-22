package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.question.exception.CannotDeleteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.user.domain.UserTest.JAVAJIGI;
import static qna.user.domain.UserTest.SANJIGI;

class RelatedAnswersByQuestionTest {

    @Test
    void 질문의_관련된_답변을_삭제할_때_생성한_사용자와_다른_사람이_존재하면_예외가_발생해야_한다() {
        Question question = new Question();
        RelatedAnswersByQuestion answers = new RelatedAnswersByQuestion(Arrays.asList(
                new Answer(null, JAVAJIGI, question, "Contents1"),
                new Answer(null, SANJIGI, question, "Contents2")
        ));

        assertThatThrownBy(() -> answers.deleteAndCreateHistory(new ArrayList<>(), JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 관련된_질문을_생성한_사용자가_삭제할_경우_히스토리가_정상_생성되어야_한다() throws CannotDeleteException {
        Question question = new Question();
        RelatedAnswersByQuestion answers = new RelatedAnswersByQuestion(Arrays.asList(
                new Answer(null, JAVAJIGI, question, "Contents1"),
                new Answer(null, JAVAJIGI, question, "Contents2")
        ));
        List<DeleteHistory> result = new ArrayList<>();

        answers.deleteAndCreateHistory(result, JAVAJIGI);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(result.get(1).getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
