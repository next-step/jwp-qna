package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    private Answer answer;

    @BeforeEach
    void createAnswer() {
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        answer = new Answer(1L, writer, question, "Answers Contents");
    }

    @Test
    @DisplayName("Answers 생성")
    void create() {
        //given
        Answers answers = new Answers();

        //expect
        assertThat(answers).isNotNull();
    }

    @Test
    @DisplayName("Answers에 Answer를 1개를 추가하면 size는 1이다")
    void addAndSize() {
        //given
        Answers answers = new Answers();

        //when
        answers.add(answer);

        //then
        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Answers에 이미 같은 Answer가 들어있으면 contains는 true이다")
    void contains_true() {
        //given
        Answers answers = new Answers();
        answers.add(answer);

        //expect
        assertThat(answers.contains(answer)).isTrue();
    }

    @Test
    @DisplayName("Answers에 같은 Answer가 들어있지 않다면 contains는 false이다")
    void contains_false() {
        //given
        Answers answers = new Answers();
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        Answer diff = new Answer(1L, writer, question, "Answers Contents");
        answers.add(diff);

        //expect
        assertThat(answers.contains(answer)).isFalse();
    }

    @Test
    @DisplayName("Answers에 모두 같은 작성자의 Answer가 들어있다면 삭제할 수 있고 List<DeleteHistory> 반환 한다")
    void delete_성공() throws CannotDeleteException {
        //given
        Answers answers = new Answers();
        User writer = new User(1L, "sangjae", "password", "name", "javajigi@slipp.net");
        Question question = new Question(1L, "title1", "contents1").writeBy(writer);
        for (long i = 1; i <= 5; i++) {
            answers.add(new Answer(i, writer, question, "Answers Contents"));
        }

        //when
        List<DeleteHistory> deleteHistories = answers.delete(writer);

        //then
        assertThat(deleteHistories).hasSize(5);
    }
}
