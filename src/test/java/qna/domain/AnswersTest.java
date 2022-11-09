package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {

    private Answers answers;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "contents");
        answers = new Answers();
    }
    
    @DisplayName("답변 목록에 추가가 성공했는지 확인")
    @Test
    void add() {
        answers.add(answer);

        assertThat(answers.getAnswers()).hasSize(1).containsExactly(answer);
    }

    @DisplayName("중복된 답변이 존재하는 경우 에러가 발생되는지 확인")
    @Test
    void validateDuplicate() {
        answers.add(answer);

        assertThatThrownBy(() -> answers.add(answer))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("삭제가 정상적으로 동작했는지 확인")
    void delete() {
        answers.add(answer);
        answers.delete(question.getWriter());

        for (Answer answer : answers.getAnswers()) {
            assertThat(answer.isDeleted()).isTrue();
        }
    }
}