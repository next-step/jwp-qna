package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    DeleteHistory deleteHistory;

    @BeforeEach
    void init() {
        User user = TestUserFactory.create();
        Question question = TestQuestionFactory.create();
        questionRepository.save(question);
        deleteHistory = TestDeleteHistoryFactory.create(ContentType.QUESTION, question.getId(), user);
    }

    @Test
    void 저장() {
        // when
        save(deleteHistory);

        // then
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    void 검색() {
        // given
        save(deleteHistory);

        // when
        DeleteHistory foundDeleteHistory = deleteHistoryRepository.findById(deleteHistory.getId()).get();

        // then
        assertThat(foundDeleteHistory).isEqualTo(deleteHistory);
    }

    @Test
    void 연관관계_유저_조회() {
        // given
        User user = deleteHistory.getDeletedByUser();
        userRepository.save(user);

        // when
        DeleteHistory savedDeleteHistory = save(deleteHistory);

        // then
        assertThat(savedDeleteHistory.getDeletedByUser()).isEqualTo(user);
    }

    @Test
    void 삭제() {
        // given
        save(deleteHistory);

        // when
        deleteHistoryRepository.delete(deleteHistory);

        // then
        assertThat(deleteHistoryRepository.findById(deleteHistory.getId()).isPresent()).isFalse();
    }

    private DeleteHistory save(DeleteHistory deleteHistory) {
        return deleteHistoryRepository.save(deleteHistory);
    }
}
