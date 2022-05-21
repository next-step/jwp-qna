package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {
    Question question;
    Answer answer1;
    Answer answer2;
    Answer answer3;
    User sanjigi;
    User javajigi;
    DeleteHistory deleteHistory1;
    DeleteHistory deleteHistory2;
    List<Answer> answers = new ArrayList<>();
    List<DeleteHistory> deleteHistories = new ArrayList<>();

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
        answer1 = Answer.builder(sanjigi, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        answer2 = Answer.builder(sanjigi, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        answer3 = Answer.builder(sanjigi, question)
                .id(1L)
                .contents("Answers Contents1")
                .build();
        deleteHistory1 = DeleteHistory.builder()
                .contentType(ContentType.ANSWER)
                .contentId(answer1.getId())
                .deletedBy(answer1.getWriter())
                .createDate(LocalDateTime.now())
                .build();
        deleteHistory2 = DeleteHistory.builder()
                .contentType(ContentType.ANSWER)
                .contentId(answer2.getId())
                .deletedBy(answer2.getWriter())
                .createDate(LocalDateTime.now())
                .build();
        deleteHistories.add(deleteHistory1);
        deleteHistories.add(deleteHistory2);
    }

    @DisplayName("로그인 유저로 답변들 제거 테스트")
    @Test
    void delete() throws CannotDeleteException {
        this.answers.add(answer1);
        this.answers.add(answer2);
        Answers answers = Answers.valueOf(this.answers);
        assertThat(answers.delete(sanjigi)).isEqualTo(deleteHistories);
    }

    @DisplayName("다른 사람의 답변 삭제시 예외 테스트")
    @Test
    void deleteAnswerOtherWriter() {
        assertThatThrownBy(() -> question.delete(sanjigi))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }
}
