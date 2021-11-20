package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.common.exception.CannotDeleteException;
import qna.domain.qna.Answer;
import qna.domain.qna.Contents;
import qna.domain.qna.Question;
import qna.domain.qna.AnswerRepository;
import qna.domain.qna.QuestionRepository;
import qna.domain.user.UserRepository;
import qna.domain.user.Email;
import qna.domain.user.User;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    Question QUESTION;
    User USER;
    Contents CONTENTS = Contents.of("Answers Contents1");

    @BeforeEach
    public void setUp() throws Exception {
        USER = users.save(UserTest.createUser("answerJavajigi", "password", "javajigi",
            new Email("javajigi@slipp.net")));
        QUESTION = new Question(QuestionPostTest.QUESTION_POST1).writeBy(USER);
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 ID not null 체크")
    void save() {
        // when
        Question actual = questions.save(QUESTION);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository 저장 후 DB 조회 객체 동일성 체크")
    void identity() {
        // when
        Question actual = questions.save(QUESTION);
        Question expect = questions.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("질문 삭제 후 해당 Question 조회결과없음 검증")
    void findByIdAndDeletedFalse_false() {
        // given
        // when
        Question actual = questions.save(QUESTION);
        actual.delete(actual.getWriter());
        Optional<Question> expect = questions.findByIdAndDeletedFalse(actual.getId());

        // then
        assertThat(expect.isPresent()).isFalse();
    }

    @Test
    @DisplayName("getAnswers 메소드를 통해 답변 목록 확인")
    void getAnswers() {
        // given
        Answer expect = new Answer(QUESTION.getWriter(), QUESTION, CONTENTS);
        Question question = questions.save(QUESTION);

        // when
        Question actual = questions.findById(question.getId()).get();

        // then
        assertThat(actual.getAnswers()).contains(expect);
    }

    @Test
    @DisplayName("delete 메소드 호출시 연관 답변도 delete 되는지 확인")
    void deleted_and_answer_delete() {
        // given
        Question saveQuestion = questions.save(QUESTION);
        new Answer(saveQuestion.getWriter(), saveQuestion, CONTENTS);

        // when
        saveQuestion.delete(USER);
        questions.flush();

        //then
        Question deletedQ1 = questions.findById(saveQuestion.getId()).get();
        assertAll(
            () -> assertThat(deletedQ1.isDeleted()).isTrue(),
            () -> assertThat(deletedQ1.getAnswers().get(0).isDeleted()).isTrue()
        );
    }


    @Test
    @DisplayName("자신의 질문 삭제, - 답변없는 질문")
    void deleted_자신의_질문_삭제_성공() {
        // given
        Question question = new Question(QuestionPostTest.QUESTION_POST1).writeBy(
            UserTest.JAVAJIGI);

        // when
        question.delete(UserTest.JAVAJIGI);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 사람 질문 삭제하기, 실패")
    void deleted_다른_사람_질문_삭제하기_실패() {
        // given
        questions.save(QUESTION);

        // when
        // then
        assertThatThrownBy(() -> {
            // when
            QUESTION.delete(UserTest.SANJIGI);
        })// then
            .isInstanceOf(CannotDeleteException.class);
    }


    @Test
    @DisplayName("자신의 질문 삭제, - 질문자가 답변한 답변만 있는 케이스")
    void deleted_자신의_질문_답변_삭제_성공() {
        // given
        new Answer(QUESTION.getWriter(), QUESTION, CONTENTS);
        new Answer(QUESTION.getWriter(), QUESTION, CONTENTS);
        questions.save(QUESTION);

        // when
        // then
        QUESTION.delete(QUESTION.getWriter());
    }

    @Test
    @DisplayName("자신의 질문 삭제, - 다른 유저 답변이 있는 케이스")
    void deleted_자신의_질문_삭제_실패() {
        // given
        new Answer(UserTest.JAVAJIGI, QUESTION, CONTENTS);
        new Answer(UserTest.SANJIGI, QUESTION, CONTENTS);
        questions.save(QUESTION);

        assertThatThrownBy(() -> {
            // when
            QUESTION.delete(QUESTION.getWriter());
        })// then
            .isInstanceOf(CannotDeleteException.class);
    }
}
