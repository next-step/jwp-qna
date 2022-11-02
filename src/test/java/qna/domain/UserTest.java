package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


    @Autowired
    private UserRepository userRepository;
    private List<User> expectList;

    @BeforeEach
    void 저장() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @Test
    void 조회() {
        expectList = userRepository.findAll();
        assertAll(
            () -> assertThat(expectList).isNotNull(),
            () -> assertThat(expectList.size()).isEqualTo(2),
            () -> assertThat(userRepository.findByUserId("javajigi").get().getEmail()).isEqualTo(JAVAJIGI.getEmail()),
            () -> assertThat(userRepository.findByUserId("sanjigi").get().getEmail()).isEqualTo(UserTest.SANJIGI.getEmail())
        );
    }

    @Test
    void 수정() {
        User expect = userRepository.findById(1L).get();
        String previousEmail = JAVAJIGI.getEmail();
        String targetEmail = "wootechcamp@hotmail.com";
        assertThat(expect.getEmail()).isEqualTo(previousEmail);

        expect.setEmail(targetEmail);
        assertThat(userRepository.save(expect).getEmail()).isEqualTo(targetEmail);
    }

    @Test
    void 삭제() {
        userRepository.deleteAll();

        assertThat(userRepository.findAll().size()).isEqualTo(0);
    }
}
