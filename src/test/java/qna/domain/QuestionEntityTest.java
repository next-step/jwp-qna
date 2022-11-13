package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerEntityTest.provideAnswer;
import static qna.domain.UserEntityTest.provideUser;

@DisplayName("질문 테스트")
class QuestionEntityTest {

    @DisplayName("질문 생성 성공 - 제목, 내용")
    @Test
    void createWithTitleContents_question_success() {
        assertThatNoException().isThrownBy(QuestionEntityTest::provideQuestion);
    }

    @DisplayName("질문 생성 성공 - id, 제목, 내용")
    @Test
    void createWithIdTitleContents_question_success() {
        //given:
        long id = 1;
        //when:
        Question question = provideQuestion();
        //then:
        assertThat(question.getId()).isEqualTo(id);
    }

    @DisplayName("질문자 설정 성공")
    @Test
    void writeBy_question_success() {
        //given:
        User user = new User();
        //when:
        Question question = provideQuestion().writeBy(user);
        //then:
        assertThat(question.getWriter()).isEqualTo(user);
    }

    @DisplayName("질문 작성자 식별 테스트")
    @Test
    void isOwner_question_success() {
        //givne:
        User user = new User();
        //when:
        Question question = provideQuestion().writeBy(user);
        //then:
        assertThat(question.isOwner(user)).isTrue();
    }

    @DisplayName("질문 삭제 성공 - 답변이 없는 경우")
    @Test
    void softDelete_question_success() throws CannotDeleteException {
        User user = provideUser();
        Question question = provideQuestion().writeBy(user);
        question.softDeleteBy(user);
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제 성공 - 질문 작성자와 답변자가 모두 일치하는 경우")
    @Test
    void softDeleteWithSelf_question_success() throws CannotDeleteException {
        //given:
        User owner = provideUser();
        Question question = provideQuestion().writeBy(owner);
        question.updateAnswers(new HashSet<>(Arrays.asList(
                provideAnswer(1L, owner, question),
                provideAnswer(2L, owner, question),
                provideAnswer(3L, owner, question))));
        //when:
        question.softDeleteBy(owner);
        //then:
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("질문 삭제 실패 - 로그인 사용자와 질문자가 다른 경우")
    @Test
    void softDeleteContainNotOwnerAnswer_question_CannotDeleteException() {
        //given:
        User owner = provideUser(1L);
        User anotherUser = provideUser(2L);
        Question question = provideQuestion().writeBy(owner);
        question.updateAnswers(new HashSet<>(Arrays.asList(
                provideAnswer(1L, anotherUser, question),
                provideAnswer(2L, owner, question),
                provideAnswer(3L, owner, question))));
        //when, then:
        assertThatThrownBy(() -> question.softDeleteBy(owner)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제 실패 - 질문의 답변 목록 중 작성자가 다른 답변이 포함 되어있는 경우")
    @Test
    void softDeleteNotMatchOwner_question_CannotDeleteException() {
        User owner = provideUser(1L);
        User anotherUser = provideUser(2L);
        Question question = provideQuestion().writeBy(owner);
        //when, then:
        assertThatThrownBy(() -> question.softDeleteBy(anotherUser)).isInstanceOf(CannotDeleteException.class);

    }

    public static Question provideQuestion() {
        return proviedQuestionWithId(1L);
    }

    public static Question proviedQuestionWithId(long id) {
        return new Question(id, new Title("title"), new Contents("contests"));
    }
}
