package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {

    @Test
    void 질문한_유저와_답변한_유저가_다르면_삭제불가() {
        final User questionWriter = new User("donghee.han", "1234", "donghee", "donghee.han@slipp.net");
        final Question question = new Question("제목", "내용").writeBy(questionWriter);
        final User answerWriter = new User("nextstep", "1234", "nextstep", "nextstep@slipp.net");

        final Answers answers = new Answers(Arrays.asList(new Answer(answerWriter, question, "댓글 내용")));
        assertThat(answers.isQuestionDeletePossible(questionWriter)).isFalse();
    }
}