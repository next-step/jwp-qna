package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.FixtureUser.HEOWC;
import static qna.domain.FixtureUser.JAVAJIGI;

@DisplayName("Answers 클래스 테스트")
class AnswersTest {

    private Answers answers;
    private Answer answer;
    private Question question;

    @BeforeEach
    void setup() {
        question = new Question("question", "question").writeBy(JAVAJIGI);
        answer = new Answer(JAVAJIGI, question, "answer");
        this.answers = new Answers();
    }

    @DisplayName("추가")
    @Test
    void add() {
        answers.add(answer);
        answers.add(answer);
        assertThat(answers.get()).containsExactly(answer);
    }

    @DisplayName("삭제")
    @Test
    void remove() {
        answers.add(answer);
        answers.remove(answer);
        assertThat(answers.get()).isEmpty();
    }

    @DisplayName("모두 제거 실패")
    @Test
    void failureRemoveAll() {
        answers.add(answer);
        answers.add(new Answer(HEOWC, question, "answer 2"));
        assertThatThrownBy(() -> {
            answers.removeAll(JAVAJIGI);
        })
        .isInstanceOf(CannotDeleteException.class)
        .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("모두 제거 성송")
    @Test
    void successfulRemoveAll() {
        answers.add(answer);
        answers.add(new Answer(JAVAJIGI, question, "answer 2"));
        answers.removeAll(JAVAJIGI);
        assertThat(answers.get()).allSatisfy(a -> {
            assertThat(a.isDeleted()).isTrue();
        });
    }
}