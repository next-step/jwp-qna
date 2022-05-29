package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class AnswersTest {
    private User writer = new User(1L, "writer1", "password", "name", "email");
    private Question question = new Question(1L, "title", "contents");

    @Test
    void allWrittenBy_호출_시_모든_답변을_같은_사용자가_작성했으면_true가_반환되어야_한다() {
        // given
        final Answer answer1 = new Answer(1L, writer, question, "contents");
        final Answer answer2 = new Answer(2L, writer, question, "contents");
        final Answer answer3 = new Answer(3L, writer, question, "contents");

        final Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);

        // when and then
        assertThat(answers.allWrittenBy(writer)).isTrue();
    }

    @Test
    void allWrittenBy_호출_시_답변_중_하나라도_다른_사용자가_작성했으면_false가_반환되어야_한다() {
        // given
        final User differentWriter = new User(1L, "writer1", "password", "name", "email");

        final Answer answer1 = new Answer(1L, writer, question, "contents");
        final Answer answer2 = new Answer(2L, writer, question, "contents");
        final Answer answer3 = new Answer(3L, differentWriter, question, "contents");

        final Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);

        // when and then
        assertThat(answers.allWrittenBy(writer)).isFalse();
    }

    @Test
    void delete_호출_시_모든_답변들이_삭제되어야_한다() throws Exception {
        // given
        final Answer answer1 = new Answer(1L, writer, question, "contents");
        final Answer answer2 = new Answer(2L, writer, question, "contents");
        final Answer answer3 = new Answer(3L, writer, question, "contents");

        final Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);

        // when
        final List<DeleteHistory> deleteHistories = answers.delete(writer);

        // then
        assertThat(answer1.isDeleted() && answer2.isDeleted() && answer3.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(3);
    }
}
