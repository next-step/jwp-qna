package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.Answer;
import qna.domain.DeleteHistories;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;


    @Autowired
    private TestEntityManager manager;

    @Autowired
    private EntityManagerFactory factory;

    private User u1;
    private User u2;
    private Question q1;
    private Answer a1;
    private DeleteHistory d1;
    private DeleteHistory d2;


    @BeforeEach
    void setup() {
        u1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        u2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        q1 = new Question("title1", "contents1").writeBy(u1);
        a1 = new Answer(u1, q1, "contents");
        d1 = DeleteHistory.ofAnswer(a1);
        d2 = DeleteHistory.ofQuestion(q1);
    }

    @DisplayName("유저를 저장할 수 있다")
    @Test
    void save_test() {
        User saved = userRepository.save(u1);

        assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals(saved.getUserId(), u1.getUserId()),
                () -> assertEquals(saved.getPassword(), u1.getPassword())
        );
    }

    @DisplayName("유저를 저장할 때 user_id가 중복되면 DataIntegrityViolationException 발생")
    @Test
    void save_exception() {
        userRepository.save(u1);

        u2.setUserId(u1.getUserId());

        assertThatThrownBy(() -> userRepository.save(u2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @DisplayName("유저의 유저아이디로 동일한 유저를 조회할 수 있다.")
    @Test
    void findByUserId_test() {
        User saved = userRepository.save(u1);

        Optional<User> user1 = userRepository.findById(saved.getId());
        Optional<User> user2 = userRepository.findByUserId(saved.getUserId());

        assertAll(
                () -> assertTrue(user2.isPresent()),
                () -> assertEquals(user1, user2)
        );
    }

    @DisplayName("유저를 조회하면 연관관계의 엔티티도 지연 조회(default)된다.")
    @Test
    void findByUserId_with_deleteHistory_test() {
        PersistenceUnitUtil persistenceUnitUtil = factory.getPersistenceUnitUtil();
        u1.addDeleteHistory(d1);
        userRepository.save(u1);
        manager.clear();

        User user = userRepository.findByUserId(u1.getUserId()).get();
        DeleteHistories deleteHistories = user.getDeleteHistories();

        assertAll(
                () -> assertThat(persistenceUnitUtil.isLoaded(deleteHistories, "deleteHistories")).isFalse(),
                () -> assertEquals(user, deleteHistories.getList().get(0).getDeletedBy()),
                () -> assertThat(persistenceUnitUtil.isLoaded(deleteHistories, "deleteHistories")).isTrue()
        );

    }

    @DisplayName("유저를 삭제하면 CascadeType.All로 연관관계 설정된 엔티티도 삭제된다.")
    @Test
    void delete_test() {
        u1.addDeleteHistory(d1);
        u1.addDeleteHistory(d2);
        userRepository.save(u1);

        assertEquals(2, deleteHistoryRepository.findAll().size());

        User user = userRepository.findByUserId(u1.getUserId()).get();
   
        userRepository.delete(user);
        manager.flush();

        assertEquals(0, deleteHistoryRepository.findAll().size());

    }

}