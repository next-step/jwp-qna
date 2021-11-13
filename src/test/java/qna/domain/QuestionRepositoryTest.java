package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    private Question question;

    @BeforeEach
    public void setUp() throws Exception {
        User user = users.save(new User("answerJavajigi", "password", "javajigi", "javajigi@slipp.net"));
        question = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 ID not null 체크")
    void save() {
        // when
        Question actual = questions.save(question);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        Question actual = questions.save(question);
        Question expect = questions.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("삭제안된 Question 조회 검증, 조회결과없음")
    void findByIdAndDeletedFalse_deleted_true() {
        // given
        // when
        Question actual = questions.save(question);
        actual.remove();
        Optional<Question> expect = questions.findByIdAndDeletedFalse(question.getId());

        // then
        assertThat(expect.isPresent()).isFalse();
    }

    @Test
    @DisplayName("getAnswers 메소드를 통해 답변 목록 확인")
    void getAnswers() {
        // given
        Answer answer = new Answer(question.getWriter(), question, "Answers Contents1");

        // when
        question.addAnswer(answer);
        List<Answer> answers = question.getAnswers();

        // then
        assertThat(answers).contains(answer);
    }

    @Test
    @DisplayName("delete 메소드 호출시 연관 답변도 delete 되는지 확인")
    void deleted_and_answer_delete() {
        // given
        Question savedQ1 = questions.save(question);
        Answer answer1 = new Answer(savedQ1.getWriter(), savedQ1, "Answers Contents2");
        savedQ1.addAnswer(answer1);

        // when
        savedQ1.delete();
        questions.flush();

        //then
        Question deletedQ1 = questions.findById(savedQ1.getId()).get();
        assertAll(
            () -> assertThat(deletedQ1.isDeleted()).isTrue(),
            () -> assertThat(deletedQ1.getAnswers().get(0).isDeleted()).isTrue()
        );
    }
}
