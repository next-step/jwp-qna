package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    User u1;
    DeleteHistory h1;
    DeleteHistory h2;


    @BeforeEach
    public void setup() {
        u1 = new User("userid","password","name","email");
        h1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        h2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.SANJIGI, LocalDateTime.now());
        h1.setUser(u1);
        h2.setUser(u1);
        userRepository.save(u1);
    }

    @Test
    public void save() {
        assertThat(h1.getId()).isNotNull();
        assertThat(h1.getId()).isEqualTo(1L);
        assertThat(h2.getId()).isNotNull();
        assertThat(h2.getId()).isEqualTo(2L);


        // db에 반영이 안될때는 이상이 없다.
        h1.setId(null);
        // db에 null setting이 반영되면 JpaSystemException 발생
        // 트랜잭션 내 lazy sql 확인가능
        assertThatThrownBy(()->{
            h2.setId(null);
            deleteHistoryRepository.findAll();
        }).isInstanceOf(JpaSystemException.class);
    }
}