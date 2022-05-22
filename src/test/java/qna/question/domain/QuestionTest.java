package qna.question.domain;

import org.junit.jupiter.api.Test;
import qna.question.exception.CannotDeleteException;
import qna.user.domain.UserTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.user.domain.UserTest.JAVAJIGI;
import static qna.user.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question(null, "title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question(null, "title2", "contents2").writeBy(SANJIGI);
    public static final Question savedQuestion1 = new Question(1L, "title1", "content1");
    public static final Question savedQuestion2 = new Question(2L, "title2", "content2");

    @Test
    void 질문을_작성하면_작성한_사용자에게_할당되어야_한다() {
        Question question = new Question();

        question.writeBy(UserTest.JAVAJIGI);

        assertThat(question.getWriter()).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void 질문을_삭제할_때_작성자가_아니면_예외가_발생해야_한다() {
        Question question = new Question().writeBy(JAVAJIGI);

        assertThatThrownBy(() -> question.deleteQuestionWithRelatedAnswer(SANJIGI, Collections.emptyList()))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문을_삭제할_때_해당_질문의_답변중_자신의_답변이_아닌_답변이_있으면_예외가_발생해야_한다() {
        Question question = new Question().writeBy(JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(null, JAVAJIGI, question, "Contents1"),
                new Answer(null, SANJIGI, question, "Contents2")
        );

        assertThatThrownBy(() -> question.deleteQuestionWithRelatedAnswer(JAVAJIGI, answers))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변이_없는_질문을_질문한_사람이_삭제하면_삭제_상태로_변해야_한다() throws CannotDeleteException {
        Question question = new Question().writeBy(JAVAJIGI);
        boolean questionDeletedStateBeforeDelete = question.isDeleted();

        List<DeleteHistory> result = question.deleteQuestionWithRelatedAnswer(JAVAJIGI, Collections.emptyList());

        assertThat(questionDeletedStateBeforeDelete).isFalse();
        assertThat(question.isDeleted()).isTrue();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getContentType()).isEqualTo(ContentType.QUESTION);
    }

    @Test
    void 답변이_질문한_사람만_존재하는_질문을_질문한_사람이_삭제하면_질문과_모든_답변이_삭제_상태로_변해야_한다() throws CannotDeleteException {
        AtomicInteger questionHistoryCount = new AtomicInteger();
        AtomicInteger answerHistoryCount = new AtomicInteger();
        Question question = new Question().writeBy(JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(null, JAVAJIGI, question, "Contents1"),
                new Answer(null, JAVAJIGI, question, "Contents2")
        );
        boolean questionDeletedStateBeforeDelete = question.isDeleted();

        List<DeleteHistory> result = question.deleteQuestionWithRelatedAnswer(JAVAJIGI, answers);
        result.forEach((history) -> {
            if (history.getContentType() == ContentType.QUESTION) {
                questionHistoryCount.getAndIncrement();
            }
            if (history.getContentType() == ContentType.ANSWER) {
                answerHistoryCount.getAndIncrement();
            }
        });

        assertThat(questionDeletedStateBeforeDelete).isFalse();
        assertThat(question.isDeleted()).isTrue();
        assertThat(result.size()).isEqualTo(3);
        assertThat(questionHistoryCount.get()).isEqualTo(1);
        assertThat(answerHistoryCount.get()).isEqualTo(2);
    }
}
