package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.domain.User;

@DataJpaTest
@DirtiesContext
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void before() {
        //given
        javajigi = userRepository.save(JAVAJIGI);
        sanjigi = userRepository.save(SANJIGI);
    }

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("특정 UserId를 조회 한다.")
    void read() {
        //when
        User findUser1 = userRepository.findByUserId(javajigi.getUserId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findUser1.getName()).isEqualTo(javajigi.getName());
    }

    @Test
    @DisplayName("특정 Id를 조회 한다.")
    void read_id() {
        //when
        User findUser = userRepository.findById(javajigi.getId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findUser.getName()).isEqualTo(javajigi.getName());
    }

    @Test
    @DisplayName("특정 UserIdId의 레코드를 업데이트 한다.")
    void update() {
        //when
        User findUser = userRepository.findByUserId(javajigi.getUserId()).get();

        User target = new User(1L, "javajigi",
                "password", "updated_name",
                "updated_javajigi@slipp.net");

        findUser.update(findUser, target);

        em.flush();
        em.clear();

        User updatedFindUser = userRepository.findByUserId(javajigi.getUserId()).get();

        //then
        assertAll(
                () -> assertThat(updatedFindUser.getName()).isEqualTo("updated_name"),
                () -> assertThat(updatedFindUser.getEmail()).isEqualTo("updated_javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        //when
        User findUser = userRepository.findById(javajigi.getId()).get();

        User target = new User(1L, "javajigi",
                "password", "updated_name",
                "updated_javajigi@slipp.net");

        findUser.update(findUser, target);

        em.flush();
        em.clear();

        User updatedFindUser = userRepository.findById(javajigi.getId()).get();

        //then
        assertAll(
                () -> assertThat(updatedFindUser.getName()).isEqualTo("updated_name"),
                () -> assertThat(updatedFindUser.getEmail()).isEqualTo("updated_javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //when
        assertThat(userRepository.count()).isEqualTo(2);

        userRepository.deleteAll();

        //then
        assertThat(userRepository.count()).isZero();
    }
}
