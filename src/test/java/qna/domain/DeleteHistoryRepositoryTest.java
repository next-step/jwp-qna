package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.FixtureAnswer.A1;
import static qna.domain.FixtureDeleteHistory.DH_A1;
import static qna.domain.FixtureDeleteHistory.DH_Q1;
import static qna.domain.FixtureQuestion.Q1;
import static qna.domain.FixtureUser.*;

@QnaDataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository,
                      @Autowired QuestionRepository questionRepository,
                      @Autowired AnswerRepository answerRepository) {
        userRepository.deleteAll();
        userRepository.save(JAVAJIGI);
        questionRepository.deleteAll();
        questionRepository.save(Q1);
        answerRepository.deleteAll();
        answerRepository.save(A1);
    }

    @AfterAll
    static void destroy(@Autowired UserRepository userRepository,
                        @Autowired QuestionRepository questionRepository,
                        @Autowired AnswerRepository answerRepository) {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final DeleteHistory deleteHistory = repository.save(DH_A1);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final DeleteHistory saved = repository.save(DH_Q1);
        final DeleteHistory deleteHistory = repository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(deleteHistory.getId()).isEqualTo(saved.getId()),
                () -> assertThat(deleteHistory).isEqualTo(saved)
        );
    }
}
