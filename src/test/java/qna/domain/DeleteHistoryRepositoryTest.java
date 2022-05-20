package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

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

    void deletedBy_연관관계_맵핑_검증() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now()));
        DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();

        assertThat(findDeleteHistory.getDeletedBy()).isEqualTo(writer);
    }

}
