package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnswersTest {
    
    private Question question;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        User writer = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(writer);
        answer1 = new Answer(writer, question, "Answers Contents1");
        answer2 = new Answer(writer, question, "Answers Contents2");
    }

    @Test
    @DisplayName("getter를 통해 조회한 Answers은 추가, 삭제가 불가능하다.")
    void answers_불변_객체() {
        List<Answer> answers = question.getAnswers();
        assertAll(() -> {
            assertThrows(UnsupportedOperationException.class, () -> answers.add(answer1));
            assertThrows(UnsupportedOperationException.class, () -> answers.remove(answer1));
        });
    }

    @Test
    @DisplayName("생성자를 통해 Answers를 생성한다.")
    void answers_생성() {
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        assertThat(answers.values()).containsExactly(answer1, answer2);
    }

    @Test
    @DisplayName("add 메소드를 이용하여 Answer를 추가한다.")
    void answers_추가() {
        Answers answers = new Answers();
        answers.add(answer1);
        assertThat(answers.values()).containsExactly(answer1);
    }

    @Test
    @DisplayName("Answers는 빈 컬렉션이 아니다.")
    void answers_isNotEmpty() {
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        assertTrue(answers.isNotEmpty());
    }
}
