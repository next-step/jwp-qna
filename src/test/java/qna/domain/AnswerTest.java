package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "질문1", "질문내용");
        answer = new Answer(UserTest.JAVAJIGI, question, "답변내용");
    }

    @Test
    @DisplayName("answer 정상 생성 테스트")
    void create() {
        assertThat(answer).isNotNull();
        assertThat(answer).isEqualTo(new Answer(UserTest.JAVAJIGI, question, "답변내용"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("answer 의 contents 업데이트 시 빈 값 입력 하면 예외발생")
    void validateUpdateContents(String contents) {
        assertThatThrownBy(() -> answer.updateContents(contents))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("null 또는 빈값으로 contents를 업데이트 할 수 없습니다.");
    }

    @Test
    @DisplayName("answer 의 contents 정상 업데이트 테스트")
    void updateContents() {
        String changedContents = "변경된 내용";

        answer.updateContents(changedContents);

        assertThat(answer.getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("answer 작성자명 null 일때 UnAuthorizedException 예외 발생")
    void validateWriter() {
        assertThatThrownBy(() -> new Answer(1L, null, question, "답변내용"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("answer 질문 null 일때 NotFoundException 예외 발생")
    void validateQuestion() {
        Question question = null;

        assertThatThrownBy(() -> new Answer(1L, UserTest.SANJIGI, question, "답변내용"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("answer 질문 작성자 아닐 경우 예외발생 테스트")
    void checkOwner() throws CannotDeleteException {
        answer.checkAnswerOwner(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> answer.checkAnswerOwner(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
