package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.constant.ErrorCode;

public class AnswersTest {

    @Test
    void 답변들_삭제여부_참_변경() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = new Answer(writer, question, "답변 삭제 여부 true 변경");
        Answer answer2 = new Answer(writer, question, "답변 삭제 여부 true 변경");
        Answers answer = new Answers(Arrays.asList(answer1, answer2));

        //when
        answer.delete(writer);

        //then
        assertAll(
                () -> assertThat(answer.getAnswers()).hasSize(2),
                () -> assertThat(answer.getAnswers()).allMatch(Answer::isDeleted)
        );
    }

    @Test
    void 답변들_중_답변한_작성자가_아니면_예외를_발생시킨다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        User fakeWriter = TestUserFactory.create("sanjigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = new Answer(writer, question, "답변한 작성자가 아니면 예외 발생");
        Answer answer2 = new Answer(fakeWriter, question, "답변한 작성자가 아니면 예외 발생");
        Answers answer = new Answers(Arrays.asList(answer1, answer2));

        //when
        assertThatThrownBy(() -> answer.delete(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorCode.답변_중_다른_사람이_쓴_답변_있어_삭제_못함.getErrorMessage());
    }

    @Test
    void 답변을_추가한다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "답변 추가");
        Answers answers = new Answers(new ArrayList<>());

        //when
        answers.addAnswer(answer);

        //then
        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    void 답변을_제거한다() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "답변 추가");
        Answers answers = new Answers(new ArrayList<>(Arrays.asList(answer)));

        //when
        answers.removeAnswer(answer);

        //then
        assertThat(answers.getAnswers().contains(answer)).isFalse();
    }
}