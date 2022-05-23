package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

class QuestionTest {
    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "ID", "Password", "이름", "abc@test.com");
        question = new Question(1L, "제목", "내용").writeBy(writer);
    }

    @Nested
    @DisplayName("질문 삭제 성공")
    class 질문_삭제_성공 {
        @Test
        @DisplayName("답변이 없는 질문은 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.")
        void 답변없는_질문() throws CannotDeleteException {
            question.delete(writer);
            assertThat(question.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.")
        void 답변있는_질문() throws CannotDeleteException {
            Answer answer = new Answer(1L, writer, question, "답변내용");
            question.addAnswer(answer);

            question.delete(writer);
            assertThat(question.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("질문을 삭제할 때 답변 또한 삭제된다.")
        void 질문과_답변_모두_삭제() throws CannotDeleteException {
            Answer answer1 = new Answer(1L, writer, question, "답변내용1");
            Answer answer2 = new Answer(2L, writer, question, "답변내용2");
            question.addAnswer(answer1);
            question.addAnswer(answer2);

            question.delete(writer);

            assertAll(
                    () -> assertThat(question.isDeleted()).isTrue(),
                    () -> assertThat(answer1.isDeleted()).isTrue(),
                    () -> assertThat(answer2.isDeleted()).isTrue()
            );
        }

        @Test
        @DisplayName("질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.")
        void 삭제_이력_정보_반환() throws CannotDeleteException {
            Answer answer1 = new Answer(1L, writer, question, "답변내용1");
            Answer answer2 = new Answer(2L, writer, question, "답변내용2");
            Answer answer3 = new Answer(3L, writer, question, "답변내용3");
            question.addAnswer(answer1);
            question.addAnswer(answer2);
            question.addAnswer(answer3);

            DeleteHistories deleteHistories = question.delete(writer);
            assertThat(deleteHistories).isNotNull();
        }
    }

    @Nested
    @DisplayName("질문 삭제 실패")
    class 질문_삭제_실패 {
        @Test
        @DisplayName("로그인 사용자와  질문자가 다른 경우 답변을 삭제할 수 없다.")
        void 질문_작성자가_다르면_삭제_불가() {
            User user = new User(2L, "user", "password1", "유저", "user@b.com");
            assertThatThrownBy(() -> question.delete(user)).isInstanceOf(CannotDeleteException.class);
        }

        @Test
        @DisplayName("질문자와 답변자가 다른 경우 질문을 삭제할 수 없다.")
        void 질문자와_답변자가_다르면_삭제_불가() {
            User user = new User(2L, "user", "password1", "유저", "user@b.com");
            Answer answer1 = new Answer(1L, writer, question, "답변내용1");
            Answer answer2 = new Answer(2L, user, question, "답변내용2");
            question.addAnswer(answer1);
            question.addAnswer(answer2);

            assertThatThrownBy(() -> question.delete(user)).isInstanceOf(CannotDeleteException.class);
        }

    }
}
