package qna.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User u1;
    private User u2;
    private Question q1;
    private Question q2;


    @BeforeEach
    void setup() {
        u1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        q1 = questionRepository.save(new Question("title1", "contents1").writeBy(u1));
    }


    @DisplayName("답변 삭제이력을 생성할 수 있다")
    @Test
    void save_test() {
        DeleteHistory d1 = new DeleteHistory(ContentType.QUESTION, q1.getId(), u1, LocalDateTime.now());
        DeleteHistory actual = deleteHistoryRepository.save(d1);
        assertAll(
                () -> assertNotNull(actual.getId()),
                () -> assertTrue(userRepository.findByUserId(u1.getUserId()).isPresent())
        );
    }


}