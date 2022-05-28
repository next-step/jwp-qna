package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AnswersTest {

    private Answers defaultAnswers;

    @BeforeEach
    void setUp() {
        defaultAnswers = new Answers();
    }

    @DisplayName("답변 객체를 관리 한다.")
    @Test
    void create() {
        assertThat(defaultAnswers).isEqualTo(new Answers());
    }

    @DisplayName("Answer 를 저장한다.")
    @Test
    void add() {
        Answers answers = new Answers();
        answers.addAnswer(AnswerTest.A1);
        assertThat(answers.size()).isEqualTo(1);
    }

    @DisplayName("중복 Answer 시 하나만 저장한다.")
    @Test
    void noDuplicateAnswer() {
        defaultAnswers.addAnswer(AnswerTest.A1);
        defaultAnswers.addAnswer(AnswerTest.A1);
        assertThat(defaultAnswers.size()).isEqualTo(1);
    }

    @DisplayName("DeletedType 에 따른 Answer 를 찾을수 있다.")
    @Test
    void findAnswerByDeleteType() {
        Answer deleteAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test content");
        deleteAnswer.setDeleted(true);
        defaultAnswers.addAnswer(deleteAnswer);

        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "test1 content");
        defaultAnswers.addAnswer(answer);
        Answers noDeletedAnswers = defaultAnswers.findAnswerBy(DeletedType.NO);
        assertThat(noDeletedAnswers.isContains(answer)).isTrue();
    }

    @DisplayName("사용자 정보를 입력하면 해당 사용자의 답변을 찾을수 있다.")
    @Test
    void findAnswerByWriter() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "test");
        defaultAnswers.addAnswer(answer);
        defaultAnswers.addAnswer(AnswerTest.A1);
        defaultAnswers.addAnswer(AnswerTest.A2);
        assertThat(defaultAnswers.findAnswerBy(UserTest.JAVAJIGI)).isEqualTo(new Answers(Arrays.asList(AnswerTest.A1,answer)));
        assertThat(defaultAnswers.findAnswerBy(UserTest.JAVAJIGI)).isEqualTo(new Answers(Arrays.asList(answer,AnswerTest.A1)));
    }

    @DisplayName("삭제 시 질문자와 답변자가 다르면 에러를 발생한다.")
    @Test
    void invalidRemove() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.remove(UserTest.JAVAJIGI))
                .withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("삭제시 질문자와 답변자가 같으면 DeleteHistories 객체를 반환한다.")
    @Test
    void remove() throws CannotDeleteException {
        Answer answer = new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, "test content1");
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, answer));
        DeleteHistories deleteHistories = answers.remove(UserTest.JAVAJIGI);
        DeleteHistory expectedDeleteHistory = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now());
        assertThat(deleteHistories.isContains(expectedDeleteHistory)).isTrue();
    }

    @DisplayName("답장 포함 확인 ")
    @Test
    void isContains() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        assertThat(answers.isContains(AnswerTest.A1)).isTrue();
    }
}
