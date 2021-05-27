package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

class UserRepositoryTest extends BaseDataJpaTest {
    @Autowired
    private UserRepository repository;

    User user;
    User savedUser;

    @BeforeEach
    void setUp() {
        user = new User(JAVAJIGI.getUserId(), JAVAJIGI.getPassword(), JAVAJIGI.getName(), JAVAJIGI.getEmail());
        savedUser = repository.save(user);
    }

    @Test
    @DisplayName("서로 같은 데이터를 가진 엔티티는 동일해야 한다")
    void entitySameAsTest() {
        assertThat(savedUser).isSameAs(user);
    }

    @Test
    @DisplayName("조회한 데이터와 같은 id 값을 가진 엔티티는 동일해야 한다")
    void entityRetrieveTest() {
        User findUser1 = repository.findByUserId(user.getUserId()).get();
        User findUser2 = repository.findById(user.getId()).get();

        assertAll(
                () -> assertThat(findUser1).isSameAs(savedUser),
                () -> assertThat(findUser2).isSameAs(savedUser),
                () -> assertThat(findUser1).isSameAs(findUser2)
        );
    }

    @Test
    @DisplayName("저장 전 후의 데이터가 같아야 한다")
    void entitySameValueTest() {
        assertAll(
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword())
        );
    }

    @Test
    @DisplayName("inert 시 createAt 이 자동으로 입력된다.")
    void dateAutoCreateTest() {

        assertAll(
                () -> assertThat(savedUser.getCreateAt()).isNotNull(),
                () -> assertThat(savedUser.getUpdateAt()).isNull()
        );
    }

    @Test
    @DisplayName("update 시 updateAt 이 자동으로 변경된다.")
    void dateAutoModifyTest() {
        savedUser.setUserId("update date?");
        repository.flush();

        assertThat(savedUser.getUpdateAt()).isNotNull();
    }

    @AfterEach
    void endUp() {
        repository.deleteAll();
    }

}