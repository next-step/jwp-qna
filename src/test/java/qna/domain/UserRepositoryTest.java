package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("merge 동일성 테스트")
    public void merge() {

        User javajigi1 = UserTest.JAVAJIGI;
        User javajigi2 = userRepository.save(javajigi1);

        //merge 이므로 not same
        assertThat(javajigi1).isNotSameAs(javajigi2);
    }

    @Test
    @DisplayName("detach 후 find 및 persist 테스트")
    public void detach() {
        User sanjigi = UserTest.SANJIGI;
        User sanjigi2 = userRepository.save(sanjigi);
        //merge 이므로 not same
        assertThat(sanjigi).isNotSameAs(sanjigi2);
        // merge일때 기존 객체가 존재하지않는경우 새로운 id채번을 통해서 리턴
        assertThat(sanjigi2.getId()).isNotNull();

        entityManager.detach(sanjigi2);
        User sanjigi4 = userRepository.findById(sanjigi2.getId()).get();

        // detach한다음에 find해오면 동일성 다름
        assertThat(sanjigi4).isNotSameAs(sanjigi2);

        sanjigi2.setId(null);
        sanjigi2.setUserId("random");
        User sanjigi3 = entityManager.persist(sanjigi2);
        // persist 동일성
        assertThat(sanjigi3).isSameAs(sanjigi2);
        assertThat(sanjigi3.getId()).isNotNull();

    }
}