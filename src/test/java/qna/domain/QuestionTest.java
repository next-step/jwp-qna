package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("writeBy의 결과 입력한 작성자 정보를 저장한 객체를 반환한다")
    void writeBy() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question(3L, "title3", "contents3");
        User expected = question.getWriter();

        // when
        final Question question2 = question.writeBy(user);
        User actual = question2.getWriter();

        // then
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("isOwner의 결과 입력된 유저 정보와 현재 저장된 유저 정보가 일치하는지 여부를 반환한다")
    void isOwner() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question("title3", "contents3").writeBy(user);

        // when
        final boolean actual = question.isOwner(user);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("addAnswer의 결과 Answer 객체의 Question을 변경한다")
    void addAnswer() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question(3L, "title3", "contents3").writeBy(user);
        final Question question2 = new Question(4L, "title4", "contents4").writeBy(user);
        final Answer answer = new Answer(user, question, "Answers Contents3");
        final Question expected = answer.getQuestion();

        // when
        question2.addAnswer(answer);
        final Question actual = answer.getQuestion();

        // then
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("deleteQuestion의 결과 삭제 히스토리가 반환된다")
    void deleteQuestion() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question(3L, "title3", "contents3").writeBy(user);
        final Answer answer = new Answer(user, question, "Answers Contents3");

        // when
        question.addAnswer(answer);
        List<DeleteHistory> actual = question.deleteQuestion(user);

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("deleteQuestion의 결과 삭제하려는 답변이 이미 삭제된 답변이면 삭제가 되지 않는다")
    void deleteQuestionAlreadyDeleteException() {
        // given
        User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        Question question = new Question(3L, "title3", "contents3").writeBy(user);

        // when & then
        question.deleteQuestion(user);
        assertThatThrownBy(() -> question.deleteQuestion(user))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("deleteQuestion의 결과 삭제하려는 답변이 작성자와 불일치하면 삭제가 되지 않는다")
    void deleteQuestionDifferentWriterException() {
        // given
        User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        Question question = new Question(3L, "title3", "contents3").writeBy(user);
        Answer answer = new Answer(3L, user, question, "Answers Contents3");

        // when & then
        assertThatThrownBy(() -> question.deleteQuestion(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("getAnswers의 결과 삭제가 되지 않은 답변 리스트가 반환된다")
    void getAnswers() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question(3L, "title3", "contents3").writeBy(user);
        final Answer answer = new Answer(user, question, "Answers Contents3");
        final Answer answer2 = new Answer(user, question, "Answers Contents4");

        // when
        question.addAnswer(answer);
        question.addAnswer(answer2);

        answer2.deleteAnswer(user);
        List<Answer> answerList = question.getAnswers();

        // then
        assertThat(answerList).hasSize(1);
    }
}
