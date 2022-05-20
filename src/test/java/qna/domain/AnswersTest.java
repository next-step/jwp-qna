package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
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
}