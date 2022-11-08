package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.question.title.Title;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Autowired
    UserRepository users;

    @Autowired
    QuestionRepository questions;

    @Autowired
    TestEntityManager em;

    @Test
    void 엔티티_저장() {
        User javajigi = users.save(new User(new UserId("javajigi"), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net")));
        Question question = getQuestion().writeBy(javajigi);
        Answer expected = new Answer(javajigi, question, question.getContents());
        Answer actual = answers.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    void Id로_삭제() {
        User javajigi = users.save(new User(new UserId("javajigi"), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net")));
        Question question = questions.save(getQuestion().writeBy(javajigi));
        Answer actual = answers.save(new Answer(javajigi, question, question.getContents()));
        answers.delete(actual);
        answers.flush();
    }

    @Test
    @DisplayName("질문 게시글에 달린 답변 목록 조회")
    void findByQuestionIdAndDeletedFalse() {
        User javajigi = users.save(new User(new UserId("javajigi"), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net")));
        Question question = questions.save(getQuestion().writeBy(javajigi));
        Answer expected = answers.save(new Answer(javajigi, question, question.getContents()));
        List<Answer> findAnswers = answers.findByQuestionIdAndDeletedFalse(expected.getQuestion().getId());
        assertThat(findAnswers).hasSize(1);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 조회")
    void findByIdAndDeletedFalse() {
        User javajigi = users.save(new User(new UserId("javajigi"), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net")));
        Question question = questions.save(getQuestion().writeBy(javajigi));
        Answer expected = answers.save(new Answer(javajigi, question, question.getContents()));
        Answer actual = answers.findByIdAndDeletedFalse(expected.getId()).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("Answer 는 User 와 Question 이 반드시 존재해야한다")
    void 익셉션_테스트() {
        User javajigi = new User(1L, new UserId("javajigi"), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net"));
        Question question = getQuestion().writeBy(javajigi);
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> new Answer(null, question, question.getContents()));
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> new Answer(javajigi, null, question.getContents()));
    }

    @ParameterizedTest
    @DisplayName("(지연로딩)Answer 에서 User, Question 으로 참조할 수 있다")
    @ValueSource(strings = "writer")
    void answer_to_user_question_lazy(String expected) {
        User writer = users.save(new User(new UserId(expected), new Password("password"), new Name("name"),
                new Email("javajigi@slipp.net")));
        Question question = questions.save(getQuestion().writeBy(writer));
        Answer answer = answers.save(new Answer(writer, question, question.getContents()));
        flushAndClear();
        Answer findAnswer = answers.findById(answer.getId()).get();
        assertThat(findAnswer.getWriter().getId()).isEqualTo(writer.getId());
        assertThat(findAnswer.getQuestion().getId()).isEqualTo(question.getId());
    }

    private Question getQuestion() {
        return new Question(new Title("title1"), "contents1");
    }

    void flushAndClear() {
        em.flush();
        em.clear();
    }
}
