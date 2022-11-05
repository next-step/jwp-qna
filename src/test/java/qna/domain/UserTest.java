package qna.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        List<User> 생성된_유저 = 사용자_생성();

        List<User> 사용자목록 = users.findAll();

        도메인_생성_검증(사용자목록, 생성된_유저);
    }

    @Test
    void 도메인을_수정할_수_있다() {
        List<User> 수정할_도메인 = 사용자_생성();

        List<LocalDateTime> 최종_수정_일자 = 최종_수정_일자(수정할_도메인);

        도메인_수정(수정할_도메인);

        수정된_도메인_검증(수정할_도메인, 최종_수정_일자);
    }

    @Test
    void 생성날짜_수정날짜가_입력되어_있다() {
        List<User> 도메인 = 사용자_생성();
        생성날짜_수정날짜_검증(도메인);
    }

    private List<User> 사용자_생성() {
        return Lists.newArrayList(
            사용자_생성("유저1"),
            사용자_생성("유저2"));
    }

    private User 사용자_생성(String 이름) {
        return users.save(사용자(이름));
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

    public static User 사용자(String 이름) {
        return new User(UUID.randomUUID().toString(), "password", 이름, "email");
    }
}
