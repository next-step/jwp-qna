package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 사용자_저장() {
        //given
        User actual = userRepository.save(JAVAJIGI);
        Long savedId = actual.getId();
        em.clear();

        //when
        User expected = userRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 사용자_저장_후_사용자_불러오기() {
        //given
        User actual = userRepository.save(JAVAJIGI);

        //when
        List<User> userList = userRepository.findAll();
        User expected = userList.get(0);

        //then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    public void 사용자_이름으로_사용자_목록_조회() {
        //given
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        em.clear();

        String name = "name";

        //when
        List<User> byName = userRepository.findByName(name);

        //then
        assertThat(2).isEqualTo(byName.size());
    }
}
