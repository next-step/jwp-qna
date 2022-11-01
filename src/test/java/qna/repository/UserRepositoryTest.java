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
    void 유저를_저장하면_저장한_유저_객체를_반환한다() {
        //when
        User saveUser = userRepository.save(JAVAJIGI);

        //then
        assertAll(
                () -> assertThat(saveUser.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(saveUser.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void 유저를_저장한_후_해당_userId로_조회하면_저장한_유저가_조회된다() {
        //given
        User saveUser = userRepository.save(JAVAJIGI);

        //when
        Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

        //then
        assertThat(findUser.isPresent()).isTrue();
        findUser.ifPresent(user -> assertAll(
                () -> assertThat(user).isEqualTo(saveUser),
                () -> assertThat(user.getUserId()).isEqualTo(saveUser.getUserId())
        ));
    }

    @Test
    void 동일한_userId를_가진_유저가_레포지토리에_존재하면_예외를_발생시킨다() {
        //given
        User saveUser = userRepository.save(SANJIGI);
        User duplicateUser = new User(null, saveUser.getUserId(), "password", "name", "email@gmail.com");

        //when
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(duplicateUser)).withMessageContaining("constraint");
    }

    @Test
    void id가_null인_유저를_저장하면_null이_아닌_id를_가진_유저를_반환한다() {
        //given
        User user = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");

        //when
        User saveUser = userRepository.save(user);

        //then
        assertThat(saveUser.getId()).isNotNull();
    }

    @TestFactory
    Collection<DynamicTest> 유저를_저장하면_조회가_되지만_해당_유저를_삭제하고_조회하면_해당_유저가_조회되지_않는다() {
        //given
        User saveUser = userRepository.save(SANJIGI);
        Long saveUserId = saveUser.getId();
        return Arrays.asList(
                DynamicTest.dynamicTest("저장한 유저의 id로 유저를 조회하면 정상적으로 조회가 된다.", () -> {
                    //when
                    Optional<User> findUser = userRepository.findById(saveUserId);

                    //then
                    assertThat(findUser.isPresent()).isTrue();
                }),
                DynamicTest.dynamicTest("저장한 유저를 삭제하고, 다시 조회하면 해당 유저가 조회되지 않는다.", () -> {
                    //when
                    userRepository.delete(saveUser);
                    Optional<User> deleteUser = userRepository.findById(saveUserId);

                    //then
                    assertThat(deleteUser.isPresent()).isFalse();
                }));
    }
}
