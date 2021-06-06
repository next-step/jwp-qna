package qna.domain.wrappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = new User(1L, "mwkown", "password", "권민욱", "mwkwon@github.com");
        question = new Question(1L, "title", "contents");
    }

    @Test
    void 답변_일급_컬렉션_생성() {
        Answer answer1 = new Answer(1L, user, question, "contents");
        Answer answer2 = new Answer(2L, user, question, "contents");
        List<Answer> answers = Arrays.asList(answer1, answer2);
        Answers actual = new Answers(answers);
        assertThat(actual).isEqualTo(new Answers(answers));
    }

    @Test
    void 답변_일급_컬렉션_객체_질문_존재_여부_확인() {
        Answers answers = new Answers();
        assertThat(answers.isEmpty()).isTrue();
    }

    @Test
    void 답변들의_작성자와_로그인_사용자가_전부_같은지_여부_확인() {
        Answer answer1 = new Answer(1L, user, question, "contents");
        Answer answer2 = new Answer(2L, user, question, "contents");
        List<Answer> answers = Arrays.asList(answer1, answer2);
        Answers actual = new Answers(answers);
        assertThat(actual.isAllSameWriter(user)).isTrue();
        User user = new User(2L, "kwon", "password", "name", "email");
        actual = new Answers(answers);
        assertThat(actual.isAllSameWriter(user)).isFalse();
    }

    @Test
    void 답변_삭제_히스토리_생성() {
        List<Answer> answers = Arrays.asList(new Answer(1L, user, question, "contents"));
        Answers actual = new Answers(answers);
        List<DeleteHistory> deleteHistories = actual.createDeleteHistories();
        assertThat(deleteHistories.size()).isEqualTo(1);
    }
}
