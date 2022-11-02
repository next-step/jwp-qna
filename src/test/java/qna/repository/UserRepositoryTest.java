package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void 유저를_저장하면_저장한_유저를_반환한다() {
        //when
        User saveUser = userRepository.save(JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(saveUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(saveUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void 유저id로_조회한다() {
        //given
        User saveUser = userRepository.save(JAVAJIGI);

        //when
        Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

        //then
        assertThat(findUser).isPresent();
        findUser.ifPresent(user -> assertAll(
                () -> assertThat(user).isEqualTo(saveUser),
                () -> assertThat(user.getUserId()).isEqualTo(saveUser.getUserId())
        ));
    }

    @Test
    void 유저Id가_중복되면_예외를_발생시킨다() {
        //given
        User saveUser = userRepository.save(SANJIGI);
        User duplicateUser = new User(null, saveUser.getUserId(), "password", "name", "email@gmail.com");

        //when
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(duplicateUser)).withMessageContaining("constraint");
    }

    @Test
    void 유저를_저장하면_반환된_유저의_id는_비어있지_않다() {
        //given
        User user = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");

        //when
        User saveUser = userRepository.save(user);

        //then
        assertThat(saveUser.getId()).isNotNull();
    }

    @TestFactory
    Collection<DynamicTest> 유저_조회_시나리오() {
        //given
        User saveUser = userRepository.save(SANJIGI);
        Long saveUserId = saveUser.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("id로 유저를 조회한다.", () -> {
                    //when
                    Optional<User> findUser = userRepository.findById(saveUserId);

                    //then
                    assertThat(findUser).isPresent();
                }),
                DynamicTest.dynamicTest("유저를 삭제하면 조회할 수 없다.", () -> {
                    //when
                    userRepository.delete(saveUser);
                    Optional<User> deleteUser = userRepository.findById(saveUserId);

                    //then
                    assertThat(deleteUser).isNotPresent();
                }));
    }
}
