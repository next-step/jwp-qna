package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //given
        repository.save(JAVAJIGI);
        repository.save(SANJIGI);

        //when
        List<User> users = repository.findAll();

        //then
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("특정 UserId를 조회 한다.")
    void read() {
        //given
        repository.save(JAVAJIGI);

        //when
        User findUser1 = repository.findByUserId(JAVAJIGI.getUserId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findUser1.getName()).isEqualTo(JAVAJIGI.getName());
    }

    @Test
    @DisplayName("특정 Id를 조회 한다.")
    void read_id() {
        //given
        User save = repository.save(JAVAJIGI);

        //when
        User findUser = repository.findById(save.getId()).get();
        em.flush();
        em.clear();

        //then
        assertThat(findUser.getName()).isEqualTo(save.getName());
    }

    @Test
    @DisplayName("특정 UserIdId의 레코드를 업데이트 한다.")
    void update() {
        //given
        User save = repository.save(JAVAJIGI);

        //when
        User findUser = repository.findByUserId(save.getUserId()).get();

        User target = new User(1L, "javajigi",
                "password", "updated_name",
                "updated_javajigi@slipp.net");

        findUser.update(findUser, target);

        em.flush();
        em.clear();

        User updatedFindUser = repository.findByUserId(save.getUserId()).get();

        //then
        assertAll(
                () -> assertThat(updatedFindUser.getName()).isEqualTo("updated_name"),
                () -> assertThat(updatedFindUser.getEmail()).isEqualTo("updated_javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("특정 Id의 레코드를 업데이트 한다.")
    void update_id() {
        //given
        User save = repository.save(JAVAJIGI);

        //when
        User findUser = repository.findById(save.getId()).get();

        User target = new User(1L, "javajigi",
                "password", "updated_name",
                "updated_javajigi@slipp.net");

        findUser.update(findUser, target);

        em.flush();
        em.clear();

        User updatedFindUser = repository.findById(save.getId()).get();

        //then
        assertAll(
                () -> assertThat(updatedFindUser.getName()).isEqualTo("updated_name"),
                () -> assertThat(updatedFindUser.getEmail()).isEqualTo("updated_javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        //given
        repository.save(JAVAJIGI);
        repository.save(SANJIGI);

        //when
        assertThat(repository.count()).isEqualTo(2);
        repository.deleteAll();

        //then
        assertThat(repository.count()).isZero();
    }
}
