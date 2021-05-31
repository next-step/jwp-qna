package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("저장 후 반환되는 객체 확인 테스트")
    @Test
    void save() {
        // when
        final Question actual = questionRepository.save(Q1);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull(),
            () -> assertThat(actual.isDeleted()).isEqualTo(false),
            () -> assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId())
        );
    }

    @DisplayName("저장 후 반환되는 객체와 DB 를 조회해서 나오는 객체가 동일한지 테스트")
    @Test
    void findById() {
        // given
        final Question expected = questionRepository.save(Q1);

        // when
        final Optional<Question> optActual = questionRepository.findById(expected.getId());
        final Question actual = optActual.orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull(),
            () -> assertThat(actual.isDeleted()).isEqualTo(false),
            () -> assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @DisplayName("메소드명에 조회 조건을 부여한 새로운 메소드(findByIdAndDeletedTrue)를 만들었을 때 정상적으로 조회되는지 확인하는 테스트")
    @Test
    void findByIdAndDeletedTrue() {
        // given
        final Question questionMarkedDeleted = questionRepository.save(Q1);
        questionMarkedDeleted.setDeleted(true);
        final Question questionMarkUndeleted = questionRepository.save(Q2);
        questionMarkUndeleted.setDeleted(false);

        // when
        final Optional<Question> optActualMarkDeleted = questionRepository.findByIdAndDeletedTrue(
            questionMarkedDeleted.getId());
        final Question actualMarkDeleted = optActualMarkDeleted.orElseThrow(IllegalArgumentException::new);
        final Optional<Question> optNotExistActualMarkDeleted = questionRepository.findByIdAndDeletedFalse(
            questionMarkedDeleted.getId());

        final Optional<Question> optActualMarkUndeleted = questionRepository.findByIdAndDeletedFalse(
            questionMarkUndeleted.getId());
        final Question actualMarkUndeleted = optActualMarkUndeleted.orElseThrow(IllegalArgumentException::new);
        final Optional<Question> optNotExistActualMarkUndeletedNotExist = questionRepository.findByIdAndDeletedTrue(
            questionMarkUndeleted.getId());

        // then
        assertAll(
            () -> assertThat(actualMarkDeleted).isNotNull(),
            () -> assertThat(actualMarkDeleted.getId()).isNotNull(),
            () -> assertThat(actualMarkDeleted).isEqualTo(questionMarkedDeleted),
            () -> assertThat(actualMarkDeleted).isEqualTo(questionMarkedDeleted),
            () -> assertThat(actualMarkUndeleted).isNotNull(),
            () -> assertThat(actualMarkUndeleted.getId()).isNotNull(),
            () -> assertThat(actualMarkUndeleted).isEqualTo(questionMarkUndeleted),
            () -> assertThat(actualMarkUndeleted).isEqualTo(questionMarkUndeleted),
            () -> assertThatThrownBy(
                () -> optNotExistActualMarkDeleted.orElseThrow(IllegalArgumentException::new)).isInstanceOf(
                IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> optNotExistActualMarkUndeletedNotExist.orElseThrow(
                IllegalArgumentException::new)).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("영속 상태와 비영속 상태에 있는 ID를 각각 조회하는 테스트")
    @Test
    void existsById() {
        // given
        final Question savedQuestion = questionRepository.save(Q1);
        final Long persistentId = savedQuestion.getId();
        final long notPersistentId = 2L;

        // when
        final boolean actualTrue = questionRepository.existsById(persistentId);
        final boolean actualFalse = questionRepository.existsById(notPersistentId);

        // then
        assertAll(
            () -> assertThat(actualTrue).isEqualTo(true),
            () -> assertThat(actualFalse).isEqualTo(false)
        );
    }

    @DisplayName("deteled = true 인 조건만 검색하는 테스트")
    @Test
    void findByDeletedFalse() {
        // given
        final Question savedQuestion1 = questionRepository.save(Q1);
        final Question savedQuestion2 = questionRepository.save(Q2);
        savedQuestion2.setDeleted(true);

        // when
        final List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0)).isEqualTo(savedQuestion1)
        );
    }

    @DisplayName("@Query 으로 Title 필드를 조건으로 검색하는 메소드 테스트")
    @Test
    void findByTitle() {
        // given
        final Question question1 = questionRepository.save(Q1);
        questionRepository.save(Q2);

        // when
        final List<Question> actual = questionRepository.findByTitle(question1.getTitle());

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0)).isEqualTo(question1)
        );
    }

    @DisplayName("contents 필드 contains 검색 테스트")
    @Test
    void findByContentContains() {
        // given
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        final String criteria = "contents";

        // when
        final List<Question> actual = questionRepository.findByContentsContains(criteria);

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(2),
            () -> assertThat(actual).containsExactly(Q1, Q2)
        );
    }
}
