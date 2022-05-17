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
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void before() {
        repository.save(JAVAJIGI);
        repository.save(SANJIGI);
    }

    @Test
    @DisplayName("저장한 테스트 데이터를 모두 조회 한다.")
    void findAll() {
        //when
        List<User> users = repository.findAll();

        //then
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("특정 UserId를 조회 한다.")
    void read() {
        //when
        User findUser1 = repository.findByUserId(JAVAJIGI.getUserId()).get();

        //then
        assertThat(findUser1.getName()).isEqualTo(JAVAJIGI.getName());

    }

    @Test
    @DisplayName("특정 UserIdId의 레코드를 업데이트 한다.")
    void update() {
        User findUser = repository.findByUserId(JAVAJIGI.getUserId()).get();

        User target = new User(1L, "javajigi",
                "password", "updated_name",
                "updated_javajigi@slipp.net");

        findUser.update(findUser, target);

        em.flush();
        em.clear();

        User updatedFindUser = repository.findByUserId(JAVAJIGI.getUserId()).get();

        assertAll(
                () -> assertThat(updatedFindUser.getName()).isEqualTo("updated_name"),
                () -> assertThat(updatedFindUser.getEmail()).isEqualTo("updated_javajigi@slipp.net")
        );
    }

    @Test
    @DisplayName("모든 테스트 데이터를 삭제 한다.")
    void delete() {
        assertThat(repository.count()).isEqualTo(2);

        repository.deleteAll();

        assertThat(repository.count()).isZero();
    }
}
