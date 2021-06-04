package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.UnAuthorizedException;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(JAVAJIGI);
    }

    @Test
    @DisplayName(value = "저장된 유저를 select 하여 원래 유저와 맞는기 검증한다")
    void select() {
        User javajigi = userRepository.findByUserId("javajigi").get();

        assertThat(javajigi.getUserId()).isEqualTo("javajigi");
        assertThat(javajigi.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName(value = "생성일, 수정일이 자동 생성 되었는지 검증한다")
    void datesAreGenerated() {
        User user = userRepository.findByUserId("javajigi").get();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName(value = "유저의 정보를 갱신: user id와 password 가 일치되면 데이터는 갱신된다")
    void updateWithoutException() {
        User target = new User(
            1L, "javajigi", "password", "pobi", "javajigi@gmail.com"
        );

        User user = userRepository.findByUserId("javajigi").get();
        user.update(JAVAJIGI, target);
        User updated = assertDoesNotThrow(() ->
            userRepository.saveAndFlush(user)
        );
        assertThat(updated.getEmail()).isEqualTo("javajigi@gmail.com");
        assertThat(updated.getName()).isEqualTo("pobi");
    }

    @Test
    @DisplayName(value = "유저 정보 갱신: user id 와 password 가 일치하지 않으면 UnAuthorizedException 을 일으킨다")
    void unAuthorizedUpdate() {
        User target = new User(
            1L, "javajigi", "not-matched-password", "pobi", "javajigi@gmail.com"
        );
        User user = userRepository.findByUserId("javajigi").get();
        assertThrows(UnAuthorizedException.class, ()->
            user.update(JAVAJIGI, target)
        );
    }

    @Test
    @DisplayName(value = "유저의 이름과 메일이 동일하다면 true 를 반환한다")
    void trueWhenMailAndNameAreMatched() {
        User foo = new User("user-id-1", "password1", "foo", "foo@mail.com");
        User bar = new User("user-id-2", "password2", "foo", "foo@mail.com");
        assertThat(foo.equalsNameAndEmail(bar)).isTrue();
    }

    @Test
    @DisplayName(value = "유저의 메일은 일치하지만, 이름이 일치하지 않으면 false를 반환한다")
    void falseWhenNameIsNotMatched() {
        User foo = new User("user-id-1", "password1", "foo", "foo@mail.com");
        User bar = new User("user-id-2", "password2", "bar", "foo@mail.com");
        assertThat(foo.equalsNameAndEmail(bar)).isFalse();
    }

    @Test
    @DisplayName(value = "유저의 이름은 일치하지만, 메일이 일치하지 않으면 false를 반환한다")
    void falseWhenMailIsNotMatched() {
        User foo = new User("user-id-1", "password1", "foo", "foo@mail.com");
        User bar = new User("user-id-2", "password2", "foo", "bar@mail.com");
        assertThat(foo.equalsNameAndEmail(bar)).isFalse();
    }

}
