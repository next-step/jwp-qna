package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    final User aUser = new User("donghee.han", "1234", "donghee", "donghee.han@slipp.net");
    final User bUser = new User("nextstep", "1234", "nextstep", "nextstep@slipp.net");
    final Question aUserQuestion = new Question("제목", "내용").writeBy(aUser);

    @Test
    void 질문한_유저와_답변한_유저가_같으면_삭제가능() {
        final Answers answers = new Answers(Arrays.asList(new Answer(aUser, aUserQuestion, "댓글 내용")));
        assertThat(answers.isQuestionDeletePossible(aUser)).isTrue();
    }

    @Test
    void 질문한_유저와_답변한_유저가_다르면_삭제불가() {
        final Answers answers = new Answers(Arrays.asList(new Answer(bUser, aUserQuestion, "댓글 내용")));
        assertThat(answers.isQuestionDeletePossible(aUser)).isFalse();
    }

    @Test
    void 댓글_질문_정보를_업데이트() {
        final Answers answers = new Answers(Arrays.asList(new Answer(aUser, aUserQuestion, "댓글 내용")));
        aUserQuestion.setDeleted(true);
        answers.updateQuestion(aUserQuestion);
        for (Answer answer : answers.getList()) {
            assertThat(answer.getQuestion().isDeleted()).isTrue();
        }
    }
}