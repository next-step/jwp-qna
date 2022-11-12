package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("질문에 답변 추가")
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
                () -> assertThat(question.getAnswers().size()).isEqualTo(1),
                () -> assertThat(question.getAnswers().contains(answer)).isTrue()
        );
    }

    @Test
    @DisplayName("글쓴이와 질문자가 다른 경우 삭제 에러")
    void delete() {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        User other = new User(2L, "coco", "password", "coco", "coco@slipp.net");
        Question question = new Question("title1", "contents1").writeBy(writer);

        //except
        assertThatThrownBy(()-> question.delete(other)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("글쓴이와 질문자가 같은 경우 삭제 가능")
    void delete_success() throws CannotDeleteException {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(1L,"title1", "contents1").writeBy(writer);

        //except
        assertThat(question.delete(writer)).isNotNull();
    }

    @Test
    @DisplayName("질문 삭제 성공 시 답변이 없으면 반환 DeleteHistory 리스트 size는 1개이다")
    void delete_success_답변_없음() throws CannotDeleteException {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(1L,"title1", "contents1").writeBy(writer);

        //except
        assertThat(question.delete(writer)).hasSize(1);
    }

    @Test
    @DisplayName("질문 삭제 성공 시 답변이 2개 있으면 반환 DeleteHistory 리스트 size는 3개이다")
    void delete_success_답변_2개() throws CannotDeleteException {
        //given
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(99L,"title1", "contents1").writeBy(writer);
        question.addAnswer(new Answer(10L,writer, question, "Answers Contents"));
        question.addAnswer(new Answer(11L,writer, question, "Answers Contents"));

        //except
        assertThat(question.delete(writer)).hasSize(3);
    }
}
