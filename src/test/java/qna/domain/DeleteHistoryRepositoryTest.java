package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("DeleteHistoryRepository 클래스")
@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
    }

    @DisplayName("저장")
    @Test
    void save() {
        final DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContentId()).isNotNull(),
                () -> assertThat(saved.getDeletedById()).isNotNull()
        );
    }

    @DisplayName("DeleteHistory Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        final DeleteHistory finded = deleteHistoryRepository.findById(saved.getId()).get();
        assertAll(
                () -> assertThat(finded).isNotNull(),
                () -> assertThat(saved.equals(finded)).isTrue()
        );
    }

    @DisplayName("DeletedUser 조회")
    @Test
    void findByDeletedByAndDeletedFalse() {
        deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        final DeleteHistory finded = deleteHistoryRepository.findByDeletedUser(UserTest.JAVAJIGI).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("DeletedUser 변경")
    @Test
    void updateWriter() {
        final DeleteHistory saved = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        saved.deletedUser(UserTest.SANJIGI);
        final DeleteHistory finded = deleteHistoryRepository.findById(saved.getId()).get();
        assertThat(finded.getDeletedUser()).isEqualTo(UserTest.SANJIGI);
    }
}
