package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class QuestionTest {
    Question question;
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
        deleteHistory = DeleteHistory.builder()
                .contentType(ContentType.QUESTION)
                .contentId(question.getId())
                .deletedBy(question.getWriter())
                .createDate(LocalDateTime.now())
                .build();
    }

    @DisplayName("로그인 유저로 질문 제거 테스트")
    @Test
    void delete() {
        assertThat(question.delete(javajigi, LocalDateTime.now())).isEqualTo(deleteHistory);
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("다른 작성자의 질문 삭제시 예외 테스트")
    @Test
    void deleteQuestionOtherWriter() {
        assertThatThrownBy(() -> question.delete(sanjigi, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }
}
