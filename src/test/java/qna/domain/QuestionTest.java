package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 생성")
    void create() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);

        //expect
        assertThat(question).isNotNull();
    }

    @Test
    @DisplayName("addAnswer은 Answer의 question과 연관 관계 편의 메소드이다.")
    void addAnswer() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);
        Answer answer = new Answer(writer, question, "Answers Contents");

        //when
        question.addAnswer(answer);

        //then
        assertAll(
                () -> assertThat(answer.getQuestion()).isSameAs(question),
                () -> assertThat(question.getAnswers()).hasSize(1),
                () -> assertThat(question.getAnswers().get(0)).isSameAs(answer)
        );
    }

}
