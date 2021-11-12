package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    Question question;
    Answer answer;
    User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = users.save(new User("answerJavajigi", "password", "javajigi", "javajigi@slipp.net"));
        question = new Question("title1", "contents1").writeBy(user);
        answer = new Answer(question.getWriter(), question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @Test
    @DisplayName("Answer 저장 후 ID not null 체크")
    void save() {
        // given
        // when
        Answer expect = answers.save(answer);

        // then
        assertThat(expect.getId()).isNotNull();
    }

    @Test
    @DisplayName("Answer 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // given
        // when
        Answer actual = answers.save(answer);
        Answer expect = answers.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("toQuestion 맵핑 후 findByQuestionIdAndDeletedFalse 조회 포함 체크 ")
    void findByQuestionIdAndDeletedFalse() {
        // given
        // when
        Answer expect = answers.save(answer);
        List<Answer> answerList = answers.findByQuestionAndDeletedFalse(expect.getQuestion());

        // then
        assertAll(
            () -> assertThat(answerList).contains(expect),
            () -> assertThat(expect.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("delete 처리 후 findByQuestionIdAndDeletedFalse 메소드 조회 미포함 체크 ")
    void findByQuestionIdAndDeletedFalse_deleted() {
        // given
        Answer expect = answers.save(answer);
        expect.remove();

        // when
        List<Answer> answerList = answers.findByQuestionAndDeletedFalse(expect.getQuestion());

        // then
        assertAll(
            () -> assertThat(answerList.contains(expect)).isFalse(),
            () -> assertThat(expect.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("삭제 안된 Answer 조회")
    void findByIdAndDeletedFalse() {
        // given
        Answer expect = answers.save(answer);

        System.out.println(expect);
        // when
        Answer actual = answers.findByIdAndDeletedFalse(expect.getId()).get();

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expect),
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

}
