package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QnaDataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    private Question question;
    private User user;
    private Answer answer;

    @BeforeEach
    void setUp(@Autowired QuestionRepository questionRepository,
               @Autowired UserRepository userRepository,
               @Autowired AnswerRepository answerRepository) {
        repository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(new User("dh", "1234", "dh", "dh@example.com"));
        questionRepository.deleteAll();
        question = questionRepository.save(new Question("dh", "dh").writeBy(user));
        answerRepository.deleteAll();
        answer = answerRepository.save(new Answer(user, question, "dh"));
    }

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final DeleteHistory deleteHistory = repository.save(DeleteHistory.ofAnswer(answer));
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final DeleteHistory saved = repository.save(DeleteHistory.ofQuestion(question));
        final DeleteHistory deleteHistory = repository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(deleteHistory.getId()).isEqualTo(saved.getId()),
                () -> assertThat(deleteHistory).isEqualTo(saved)
        );
    }
}
