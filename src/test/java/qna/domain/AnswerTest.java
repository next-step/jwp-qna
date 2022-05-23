package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswerTest {
    Question question;
    Answer answer;
    User sanjigi;
    User javajigi;
    DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        javajigi = User.builder("javajigi", "password", "name")
                .id(1L)
                .email("javajigi@slipp.net")
                .build();
        sanjigi = User.builder("sanjigi", "password", "name")
                .id(2L)
                .email("sanjigi@slipp.net")
                .build();
        question = Question.builder("title1")
                .id(1L)
                .contents("contents1")
                .build()
                .writeBy(javajigi);
        answer = Answer.builder(sanjigi, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        deleteHistory = DeleteHistory.builder()
                .contentType(ContentType.ANSWER)
                .contentId(answer.getId())
                .deletedBy(answer.getWriter())
                .createDate(LocalDateTime.now())
                .build();
    }

    @DisplayName("로그인 유저로 답변 제거 테스트")
    @Test
    void delete() {
        assertThat(answer.delete(sanjigi, LocalDateTime.now())).isEqualTo(deleteHistory);
        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("다른 사람의 답변 삭제시 예외 테스트")
    @Test
    void deleteAnswerOtherWriter() {
        assertThatThrownBy(() -> answer.delete(javajigi, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
