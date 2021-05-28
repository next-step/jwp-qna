package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User INSUP = new User(3L, "insup", "password", "name", "insup@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private User javajigi;
    private User insup;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(JAVAJIGI);
        System.out.println("setUp javajigi = " + javajigi);

        insup = userRepository.save(INSUP);
        System.out.println("setUp insup = " + insup);
    }

    @DisplayName("저장")
    @Test
    void save() {
        System.out.println(javajigi); //find 이후에 save 테스트가 호출이 되면 javajigi가 id=3, userId='insup'으로 바뀌어 있음...
        //when, then
        assertThat(javajigi).isNotNull();
        assertThat(javajigi.getUserId()).isEqualTo("javajigi");
    }

    @DisplayName("찾기")
    @Test
    void find() {
        //when
        User findUser = userRepository.findById(javajigi.getId())
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findUser.getId()).isEqualTo(1L);
        assertThat(findUser).isSameAs(javajigi);
    }

    @DisplayName("userId로 찾기")
    @Test
    void findByUserId() {
        //when
        User findUser = userRepository.findByUserId("javajigi")
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findUser).isEqualTo(javajigi);
        assertThat(findUser).isSameAs(javajigi);
    }
}
