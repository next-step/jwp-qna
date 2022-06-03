package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class AnswersTest {
    Answers answers = new Answers();
    Answer answer;
    Question question;

    @BeforeEach
    void setUp() {
        question = new Question("Question Title", "Question content").writeBy(JAVAJIGI);
        answer = new Answer(JAVAJIGI, question, "Answer content1");
    }

    @Test
    @DisplayName("답변 리스트에 답변을 추가한다")
    void add() {
        answers.add(answer);

        assertThat(answers.size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("주어진 답변이 포함되어 있는지 확인한다.")
    void contains() {
        answers.add(answer);

        assertThat(answers.contains(answer))
                .isTrue();
    }


    @Test
    @DisplayName("답변들을 삭제한다.")
    void delete() throws CannotDeleteException {
        answers.add(new Answer(JAVAJIGI, question, "Answer content1"));
        answers.add(new Answer(JAVAJIGI, question, "Answer content2"));

        List<DeleteHistory> deletedHistories = answers.delete(JAVAJIGI);
        assertThat(deletedHistories)
                .hasSize(2);
    }

    @Test
    @DisplayName("답변들을 삭제 시 다른 사람이 쓴 답변이 있으면 예외를 발생시킨다.")
    void delete_wrong_writer() {
        answers.add(new Answer(JAVAJIGI, question, "Answer content1"));
        answers.add(new Answer(SANJIGI, question, "Answer content2"));

        assertThatThrownBy(() -> answers.delete(JAVAJIGI))
                .isExactlyInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("답변들을 삭제 시 이미 삭제한 답변이 있으면 예외를 발생시킨다.")
    void delete_already_deleted() {
        answer.setDeleted(true);
        answers.add(answer);

        assertThatThrownBy(() -> answers.delete(JAVAJIGI))
                .isExactlyInstanceOf(CannotDeleteException.class)
                .hasMessage("이미 삭제된 답변 입니다.");
    }
}
