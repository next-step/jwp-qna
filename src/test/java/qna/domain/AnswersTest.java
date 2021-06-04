package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    @Test
    void 질문_일급_컬렉션_생성() {
        List<Answer> answers = Arrays.asList(AnswerTest.A1, AnswerTest.A2);
        Answers actual = new Answers(answers);
        assertThat(actual).isEqualTo(new Answers(answers));
    }

    @Test
    void 질문_일급_컬렉션_객체_질문_존재_여부_확인() {
        Answers answers = new Answers();
        assertThat(answers.isEmpty()).isTrue();
    }

    @Test
    void 질문들의_작성자와_로그인_사용자가_전부_같은지_여부_확인() {
        List<Answer> answers = Arrays.asList(AnswerTest.A1, AnswerTest.A2);
        Answers actual = new Answers(answers);
        assertThat(actual.isAllSameWriter(UserTest.JAVAJIGI)).isFalse();

        actual = new Answers(Arrays.asList(AnswerTest.A1));
        assertThat(actual.isAllSameWriter(UserTest.JAVAJIGI)).isTrue();
    }
}
