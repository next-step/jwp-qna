package qna.domain.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.QuestionTest;
import qna.domain.user.UserTest;
import qna.domain.content.Contents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnswersTest {

    @Test
    @DisplayName("Answers 동일한 답변 객체 담을 수 없음")
    void isDuplicatedAnswer() {
        Answers answers = new Answers();
        Answer target = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));

        assertThat(answers.isDuplicatedAnswer(target)).isFalse();
        answers.add(target);
        assertThat(answers.isDuplicatedAnswer(target)).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 동일할 경우 삭제 가능")
    void deleteAll() {
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
        Answer answer2 = new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents2"));
        Answers answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);
        answers.deleteAll(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(answer1.isDeleted()).isTrue(),
                () -> assertThat(answer2.isDeleted()).isTrue()
        );
    }

    @Test
    void add() {
        Answers answers = new Answers();

        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
        assertThat(answers.getAnswers()).hasSize(0);
        answers.add(answer);
        assertThat(answers.getAnswers()).hasSize(1);
    }

}
