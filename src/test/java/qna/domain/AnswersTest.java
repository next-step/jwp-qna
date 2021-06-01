package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersTest {

    private Question question;
    private Answer answer1;
    private Answer answer2;
    private Answers answers;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "질문1", "질문내용");
        answer1 = new Answer(UserTest.JAVAJIGI, question, "답변내용1");
        answer2 = new Answer(UserTest.JAVAJIGI, question, "답변내용2");
        answers = new Answers();

        answers.add(answer1);
        answers.add(answer2);
    }

    @Test
    @DisplayName("Answers 클래스 내의 컬렉션에 질문 요소를 추가하여 저장 테스트")
    void add() {
        List<Answer> resultAnswers = this.answers.getAnswers();
        assertAll(
                () -> assertThat(resultAnswers).hasSize(2),
                () -> assertThat(resultAnswers.get(0).isDeleted()).isFalse(),
                () -> assertThat(resultAnswers.get(1).isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문들 전부 삭제 테스트")
    void deleteAll() throws CannotDeleteException {
        answers.deleteAll(UserTest.JAVAJIGI);

        List<Answer> resultAnswers = this.answers.getAnswers();
        assertAll(
                () -> assertThat(resultAnswers.get(0).isDeleted()).isTrue(),
                () -> assertThat(resultAnswers.get(1).isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("질문들 전부 삭제시 작성자가 다른 답변이 있을경우 실패한다.(예외발생)")
    void deleteAll_fail() {
        Answer answer3 = new Answer(UserTest.SANJIGI, question, "답변내용3");
        answers.add(answer3);

        assertThatThrownBy(() -> answers.deleteAll(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
