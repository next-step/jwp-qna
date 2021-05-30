package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private User questionUser;
    private User firstDeletedUser;
    private User secondDeletedUser;
    private DeleteHistory firstDeleteHistory;
    private DeleteHistory secondDeleteHistory;
    private DeleteHistory thirdDeleteHistory;
    private List<DeleteHistory> deleteHistories;

    @BeforeEach
    public void beforeEach() {
        this.firstDeletedUser = User.copy(UserTest.JAVAJIGI);
        this.firstDeletedUser.setId(1L);
        this.secondDeletedUser = User.copy(UserTest.SANJIGI);
        this.secondDeletedUser.setId(2L);
        this.questionUser = User.copy(UserTest.HAGI);
        this.questionUser.setId(3L);
        this.firstDeleteHistory = new DeleteHistory(ContentType.ANSWER, 1L,
                this.firstDeletedUser.getId(), LocalDateTime.now());
        this.secondDeleteHistory = new DeleteHistory(ContentType.ANSWER, 2L,
                this.secondDeletedUser.getId(), LocalDateTime.now());
        this.thirdDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L,
                this.questionUser.getId(), LocalDateTime.now());
        this.deleteHistories = Arrays.asList(this.firstDeleteHistory, this.secondDeleteHistory,
                this.thirdDeleteHistory);
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
        Optional<DeleteHistory> deleteHistory = repository.findByContentTypeAndContentId(ContentType.ANSWER, 1L);

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
        List<DeleteHistory> deleteHistories = repository.findByDeletedById(this.firstDeletedUser.getId());

        // then
        assertThat(deleteHistories.size()).isEqualTo(1);
    }
}
