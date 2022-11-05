package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class UserTest extends BaseDomainTest<User> {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        users.deleteAll();
    }

    @Test
    void 도메인을_생성할_수_있다() {
        List<User> 생성된_유저 = 사용자();

        List<User> 사용자목록 = users.findAll();

        도메인_생성_검증(사용자목록, 생성된_유저);
    }

    @Test
    void 도메인을_수정할_수_있다() {
        List<User> 수정할_도메인 = 사용자();

        List<LocalDateTime> 최종_수정_일자 = 최종_수정_일자(수정할_도메인);

        도메인_수정(수정할_도메인);

        수정된_도메인_검증(수정할_도메인, 최종_수정_일자);
    }

    @Test
    void 생성날짜_수정날짜가_입력되어_있다() {
        List<User> 도메인 = 사용자();
        생성날짜_수정날짜_검증(도메인);
    }

    @Test
    void 사용자의_질문들을_조회할_수_있다() {
        User 사용자 = 사용자().get(0);
        Question 질문1 = QuestionTest.질문("질문1");
        Question 질문2 = QuestionTest.질문("질문2");
        사용자.addQuestion(질문1);
        사용자.addQuestion(질문2);
        flush();

        List<Question> 사용자의_질문 = 사용자.getQuestions();

        assertThat(사용자의_질문).isNotEmpty();
        assertThat(사용자의_질문)
            .flatExtracting(Question::getWriter)
            .hasSize(2)
            .containsExactlyInAnyOrder(사용자, 사용자);

    }

    private List<User> 사용자() {
        return this.users.saveAll(Lists.newArrayList(
            사용자("유저1"),
            사용자("유저2")));
    }

    void 도메인_수정(List<User> 수정할_도메인) {
        수정할_도메인.forEach(e ->
            e.update(e, new User("updated", "password", "updated-name", "updated@java.com")));
        users.saveAll(수정할_도메인);
        users.flush();
    }

    void flush() {
        entityManager.flush();
        entityManager.clear();
    }

    public static User 사용자(String 사용자_아이디) {
        return new User(사용자_아이디, "password", "name", "email");
    }
}
