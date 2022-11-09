package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.answer.Answer;
import qna.domain.question.Question;
import qna.domain.question.title.Title;
import qna.domain.user.User;
import qna.domain.user.email.Email;
import qna.domain.user.name.Name;
import qna.domain.user.password.Password;
import qna.domain.user.userid.UserId;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questions;

    @Autowired
    UserRepository users;

    @Autowired
    AnswerRepository answers;

    @Autowired
    TestEntityManager em;

    @Test
    void 엔티티_저장() {
        Question expected = getQuestion("title", "content1");
        Question actual = questions.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    void Id로_삭제() {
        Question question = questions.save(getQuestion("title", "content1"));
        questions.delete(question);
        questions.flush();
    }

    @Test
    @DisplayName("delete 컬럼이 false 인 질문목록 조회")
    void findByDeletedFalse() {
        questions.save(getQuestion("코드 리뷰 요청드립니다.", "리뷰 잘 부탁드립니다."));
        questions.save(getQuestion("(수정)코드 리뷰 요청드립니다.", "리뷰 잘 부탁드립니다."));
        List<Question> questionList = questions.findByDeletedFalse();
        assertThat(questionList).hasSize(2);
    }

    @Test
    @DisplayName("메소드명에 And 를 포함하여 And 조건문을 수행할 수 있다")
    void findByIdAndDeletedFalse() {
        Question expected = questions.save(getQuestion("title", "content1"));
        Question actual = questions.findByIdAndDeletedFalse(expected.getId()).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("(지연로딩)Question 에서 User 로 참조할 수 있다")
    void question_to_user_lazy() {
        User loginUser = users.save(getUser(null, "writer", "1111", "작성자", "writer@naver.com"));
        Question question = questions.save(getQuestion("title", "content1").writeBy(loginUser));
        flushAndClear();
        Question findQuestion = questions.findById(question.getId()).get();
        assertThat(findQuestion.getWriter().getId()).isEqualTo(loginUser.getId());
    }

    @Test
    @DisplayName("(지연로딩)Question 에서 Answer 로 참조할 수 있다")
    void question_to_answers() {
        User writer = users.save(getUser(null, "writer", "1111", "작성자", "writer@naver.com"));
        Question question = questions.save(getQuestion("title", "content1").writeBy(writer));
        Answer expected = answers.save(new Answer(writer, question, question.getContents()));
        flushAndClear();
        List<Answer> actual = questions.findById(question.getId()).get().getAnswers();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
    }

    @Test
    @DisplayName("하나의 트랜잭션 안에서 Question과 Answer는 서로 참조가 가능해야 한다")
    void question_to_answers_no_flush_clear() {
        User writer = users.save(getUser(null, "writer", "1111", "작성자", "writer@naver.com"));
        Question question = questions.save(getQuestion("title", "content1").writeBy(writer));
        Answer expected = answers.save(new Answer(writer, question, question.getContents()));
        question.addAnswer(expected);
        List<Answer> actual = question.getAnswers();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
    }

    private User getUser(Long id, String userId, String password, String name, String email) {
        return new User(id, new UserId(userId, users), new Password(password), new Name(name), new Email(email));
    }

    private Question getQuestion(String title, String content) {
        return new Question(new Title(title), content);
    }

    void flushAndClear() {
        em.flush();
        em.clear();
    }
}
