package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    
    @Autowired
    UserRepository users;

    private static Stream<User> save_user_and_find_test() {
        return Stream.of(JAVAJIGI,SANJIGI);
    }

    @ParameterizedTest
    @DisplayName("유저 엔터티 저장 후 찾기 테스트")
    @MethodSource
    void save_user_and_find_test(User input) {
        User user = users.save(input);
        assertThat(users.findByUserId(user.getUserId()).get()).isEqualTo(user);
    }

    @Test
    @DisplayName("유저 엔터티 저장 후 수정 테스트")
    void save_user_and_update_test() {
        //given
        User user = users.save(JAVAJIGI);
        Long id = user.getId();
        //when
        User searchResult = users.findById(id).get();
        String updateContent = "testEmail";
        users.save(new User(id, searchResult.getUserId(),
                searchResult.getPassword(), searchResult.getName(), updateContent));
        //then
        assertThat(users.findById(id).get().getEmail()).isEqualTo(updateContent);
    }

    static Stream<User> save_user_and_delete_test() {
        return Stream.of(JAVAJIGI, SANJIGI);
    }

    @ParameterizedTest
    @DisplayName("유저 엔터티 저장 후 삭제 테스트")
    @MethodSource
    public void save_user_and_delete_test(User input) {
        //given
        User user = users.save(input);
        Long id = user.getId();
        //when
        users.deleteById(id);
        //then
        assertThat(users.findById(id).orElse(null)).isNull();
    }
}
