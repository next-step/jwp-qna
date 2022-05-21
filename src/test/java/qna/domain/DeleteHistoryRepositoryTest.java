package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    void DeleteHistory_저장() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now()));

        assertThat(questionRepository.findById(deleteHistory.getId())).isNotNull();
    }

    @Test
    void deletedBy_연관관계_맵핑_검증() {
        User writer2 = em.persist(new User(null, "sihyun", "password", "name", "javajigi@slipp.net"));

        Question question = em.persist(new Question("title1", "contents1").writeBy(writer2));
        DeleteHistory deleteHistory = em.persist(new DeleteHistory(ContentType.QUESTION, question.getId(), writer2, LocalDateTime.now()));
        em.flush();
        em.clear();

        DeleteHistory findDeleteHistory = em.find(DeleteHistory.class, deleteHistory.getId());

        assertThat(findDeleteHistory.getDeletedBy()).isEqualTo(writer2);
    }

}
