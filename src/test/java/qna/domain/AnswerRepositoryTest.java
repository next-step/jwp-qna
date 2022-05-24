package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.MOND;
import static qna.domain.UserTest.SRUGI;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {
    private Answer answer;
    private User mond, srugi;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("영속 상태의 동일성 보장 검증")
    void verifyEntityPrimaryCacheSave() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(answer);
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualAnswerFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("준영속 상태의 동일성 보장 검증")
    void verifyEntityDatabaseSave() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(answer);
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualAnswerFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("엔티티 컨텐츠가 반영되는지 검증")
    void verifyUpdateEntity() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(answer);
        expected.delete(srugi);
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> verifyEqualAnswerFields(actual.get(), expected),
                () -> assertThat(actual.get().isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("저장 및 물리 삭제 후 해당 id로 검색")
    void saveAndPhysicalDeleteThenFindById() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(answer);
        answerRepository.delete(expected);
        entityFlushAndClear();
        Optional<Answer> actual = answerRepository.findById(expected.getId());

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("저장 및 논리 삭제 후 해당 id로 검색")
    void saveAndLogicalDeleteThenFindById() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(answer);
        expected.delete(srugi);
        entityFlushAndClear();
        Optional<Answer> actualOfFindById = answerRepository.findById(expected.getId());
        Optional<Answer> actualOfFindByIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(expected.getId());

        assertAll(
                () -> assertThat(actualOfFindById).isPresent(),
                () -> assertThat(actualOfFindByIdAndDeletedFalse).isNotPresent()
        );
    }

    @Test
    @DisplayName("저장 및 논리 삭제 후 Question id로 검색시 결과값이 존재하지 않는지 검증")
    void verifyDeleteAnswerThenNotResult() {
        initUserAndQuestionSetting();
        Answer expected = answerRepository.save(this.answer);
        expected.delete(srugi);
        entityFlushAndClear();

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestion().getId());
        assertThat(answerList).isEmpty();
    }

    private void initUserAndQuestionSetting() {
        mond = userRepository.findByUserId(MOND.getUserId())
                .orElseGet(() -> userRepository.save(MOND));
        srugi = userRepository.findByUserId(SRUGI.getUserId())
                .orElseGet(() -> userRepository.save(SRUGI));

        Question question = new Question("question title", "question contents").writeBy(mond);
        Question actualQuestion = questionRepository.save(question);
        answer = new Answer(srugi, actualQuestion, "answer contents");
    }

    private void entityFlushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void verifyEqualAnswerFields(Answer a1, Answer a2) {
        assertAll(
                () -> assertThat(a1.getId()).isEqualTo(a2.getId()),
                () -> assertThat(a1.getWriter()).isEqualTo(a2.getWriter()),
                () -> assertThat(a1.getContents()).isEqualTo(a2.getContents()),
                () -> assertThat(a1.getCreatedAt()).isEqualTo(a2.getCreatedAt()),
                () -> assertThat(a1.getUpdatedAt()).isEqualTo(a2.getUpdatedAt())
        );
    }
}
