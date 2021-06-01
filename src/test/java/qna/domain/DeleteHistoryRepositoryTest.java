package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * DeleteHistoryRepository 인터페이스 선언 메소드 테스트
 */
@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManagerFactory factory;

    private User hagiUser;
    private User javajigiUser;
    private User sanjigiUser;
    private Question firstQuestion;
    private Question secondQuestion;
    private Answer firstAnswer;
    private Answer secondAnswer;
    private DeleteHistory firstDeleteHistory;
    private DeleteHistory secondDeleteHistory;
    private DeleteHistory thirdDeleteHistory;
    private List<DeleteHistory> deleteHistories;
    private PersistenceUnitUtil entityUtil;

    @BeforeEach
    public void beforeEach() {
        this.entityUtil = factory.getPersistenceUnitUtil();
        this.javajigiUser = User.copy(UserTest.JAVAJIGI);
        this.sanjigiUser = User.copy(UserTest.SANJIGI);
        this.hagiUser = User.copy(UserTest.HAGI);
        userRepository.saveAll(Arrays.asList(this.javajigiUser, this.sanjigiUser, this.hagiUser));

        this.firstQuestion = Question.copy(QuestionTest.Q1);
        this.secondQuestion = Question.copy(QuestionTest.Q2);
        questionRepository.saveAll(Arrays.asList(this.firstQuestion, this.secondQuestion));

        this.firstAnswer = new Answer(this.javajigiUser, this.firstQuestion, AnswerTest.A1.getContents());
        this.secondAnswer = new Answer(this.sanjigiUser, this.firstQuestion, AnswerTest.A2.getContents());
        answerRepository.saveAll(Arrays.asList(this.firstAnswer, this.secondAnswer));

        this.firstDeleteHistory = new DeleteHistory(ContentType.ANSWER, this.firstAnswer.getId(),
                this.javajigiUser, LocalDateTime.now());
        this.secondDeleteHistory = new DeleteHistory(ContentType.ANSWER, this.secondAnswer.getId(),
                this.sanjigiUser, LocalDateTime.now());
        this.thirdDeleteHistory = new DeleteHistory(ContentType.QUESTION, this.firstQuestion.getId(),
                this.hagiUser, LocalDateTime.now());
        this.deleteHistories = Arrays.asList(this.firstDeleteHistory, this.secondDeleteHistory,
                this.thirdDeleteHistory);
    }

    @Test
    @DisplayName("지연로딩")
    void lazy_loading() {
        DeleteHistory lazyDeleteHistory = repository.findById(100L).get();
        assertThat(this.entityUtil.isLoaded(lazyDeleteHistory, "deletedByUser")).isFalse();

        User lazyDeletedByUser = lazyDeleteHistory.getDeletedByUser();
        assertThat(this.entityUtil.isLoaded(lazyDeleteHistory, "deletedByUser")).isFalse();

        String name = lazyDeletedByUser.getName();
        assertThat(this.entityUtil.isLoaded(lazyDeleteHistory, "deletedByUser")).isTrue();
    }

    @Test
    @DisplayName("삭제이력 저장")
    void save() {
        // when
        DeleteHistory saveDeleteHistory = repository.save(this.firstDeleteHistory);

        // then
        assertThat(saveDeleteHistory).isSameAs(this.firstDeleteHistory);
    }

    @Test
    @DisplayName("컨텐츠 유형 및 컨텐츠 ID 기준 ")
    void find_by_answer_contentId() {
        // given
        repository.save(this.firstDeleteHistory);

        // when
        Optional<DeleteHistory> deleteHistory = repository.findByContentTypeAndContentId(ContentType.ANSWER,
                this.firstAnswer.getId());

        // then
        assertThat(deleteHistory.get()).isSameAs(this.firstDeleteHistory);
    }

    @Test
    @DisplayName("컨텐츠 유형, 컨텐츠ID, 삭제한 사용자 ID기준 조회")
    void find_by_ContentType_contentId_deletedById() {
        // given
        repository.saveAll(this.deleteHistories);

        // when
        List<DeleteHistory> findHistories = repository.findByCreateDateGreaterThanEqual(LocalDateTime.now().minusDays(1));

        // then
        assertThat(findHistories.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("생성일자 범위 조회 및 정렬")
    void find_by_createDate_between_orderBy_desc() {
        // given
        repository.saveAll(this.deleteHistories);

        // when
        List<DeleteHistory> deleteHistories = repository.findByCreateDateBetweenOrderByCreateDateDesc(
                LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(1));

        // then
        assertThat(deleteHistories.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("삭제한 사용자 ID를 기준으로 목록 조회")
    void find_by_deletedById() {
        // given
        repository.saveAll(this.deleteHistories);

        // when
        List<DeleteHistory> deleteHistories = repository.findByDeletedByUser(this.javajigiUser);

        // then
        assertThat(deleteHistories.size()).isEqualTo(1);
    }
}
