package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User("id1", "password1", "name1", "email1");
        user2 = new User("id2", "password2", "name2", "email2");
        entityManager.persist(user1);
        entityManager.persist(user2);
    }

    @DisplayName("Entity 저장 테스트")
    @Test
    void save() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(ANSWER, 1L, user1, LocalDateTime.now());

        // when
        final DeleteHistory saved = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
            () -> assertThat(saved).isEqualTo(deleteHistory)
        );
    }

    @DisplayName("Indexed Query Parameter 테스트")
    @Test
    void findById() {
        // given
        final long contentId = 1L;
        final DeleteHistory deleteHistory = new DeleteHistory(ANSWER, contentId, user1, null);
        final DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);

        // when
        final List<DeleteHistory> actual = deleteHistoryRepository.findByContentId(contentId);

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0)).isEqualTo(expected)
        );
    }

    @DisplayName("createDate 필드에 null 을 전달해도 @CreatedDate 에서 값을 자동으로 생성하는지 테스트")
    @Test
    void findByCreateDateIsNull() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(ANSWER, 1L, user1, null);
        deleteHistoryRepository.save(deleteHistory);

        // when
        final List<DeleteHistory> actual = deleteHistoryRepository.findByCreateDateNull();

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(0)
        );
    }

    @DisplayName("ContentType 필드에 null 을 설정해서 저장하고 검색 조건도 null 로 설정하면 정상적으로 조회되는지 테스트")
    @Test
    void findByContentType() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(null, 1L, user1, null);
        deleteHistoryRepository.save(deleteHistory);

        // when
        final List<DeleteHistory> actual = deleteHistoryRepository.findByContentType(null);

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(1)
        );
    }

    @DisplayName("Example Interface 를 이용해서 find, count 테스트")
    @Test
    void example_interface() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(null, 1L, user1, null);
        final DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        final DeleteHistory probe = new DeleteHistory(null, 1L, user1, null);

        // when
        final Optional<DeleteHistory> optActual = deleteHistoryRepository.findOne(Example.of(probe));
        final DeleteHistory actual = optActual.orElseThrow(IllegalArgumentException::new);
        final long actualCount = deleteHistoryRepository.count(Example.of(probe));

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actualCount).isEqualTo(1)
        );
    }

    @DisplayName("Native Query 작성해서 테스트")
    @Test
    void native_query() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(null, 1L, user1, null);
        final DeleteHistory deleteHistory2 = new DeleteHistory(null, 2L, user2, null);
        deleteHistoryRepository.save(deleteHistory);
        deleteHistoryRepository.save(deleteHistory2);
        final Collection<Long> ids = Arrays.asList(1L, 2L);

        // when
        final List<DeleteHistory> actual = deleteHistoryRepository.findByIdsWithNative(ids);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(2),
            () -> assertThat(actual).contains(deleteHistory, deleteHistory2)
        );
    }

    @DisplayName("@Modifying 을 이용하여 update sql 테스트")
    @Test
    void updateDeleteHistorySetContentTypeById() {
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(ANSWER, 1L, user1, null);
        final Long id = entityManager.persistAndGetId(deleteHistory, Long.class);
        deleteHistoryRepository.updateDeleteHistorySetContentTypeById(QUESTION, id);

        // when
        final DeleteHistory probe = new DeleteHistory(QUESTION, 1L, user1, null);
        final Optional<DeleteHistory> optActual = deleteHistoryRepository.findOne(Example.of(probe));
        final DeleteHistory actual = optActual.orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual).isEqualTo(deleteHistory)
        );
    }
}
