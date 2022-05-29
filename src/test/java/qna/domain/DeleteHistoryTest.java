package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User JAVAJIGI;
    private Question Q1;
    private Answer A1;

    @BeforeEach
    void setUp() {
        JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");

        testEntityManager.persistAndFlush(JAVAJIGI);
        testEntityManager.persistAndFlush(Q1);
        testEntityManager.persistAndFlush(A1);
        testEntityManager.clear();
    }

    @Test
    void saveAll() {
        //given
        DeleteHistory D1 = DeleteHistory.question(Q1);
        DeleteHistory D2 = DeleteHistory.answer(A1);
        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(D1, D2));

        //when
        List<DeleteHistory> savedDeleteHistories = deleteHistoryRepository.saveAll(deleteHistories);

        //then
        assertThat(savedDeleteHistories).containsExactlyInAnyOrder(D1, D2);
    }
}
