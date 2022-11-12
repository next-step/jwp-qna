package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoriesTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);

        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @Test
    void 생성_성공() {
        DeleteHistories deleteHistories = new DeleteHistories();

        assertThat(deleteHistories).isInstanceOf(DeleteHistories.class);
    }

    @Test
    void DeleteHistory_일급컬렉션_추가() {
        DeleteHistories deleteHistories = new DeleteHistories();
        DeleteHistory DH1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, now());
        DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.SANJIGI, now());

        deleteHistoryRepository.save(DH1);
        deleteHistoryRepository.save(DH2);

        deleteHistories.add(DH1);
        deleteHistories.add(DH2);

        assertThat(deleteHistories.values()).contains(DH2);
    }
}
