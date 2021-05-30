package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("질문 테이블 정상 저장 테스트")
    void save() {
        Question expected = QuestionTest.Q1;
        Question actual = questions.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    @DisplayName("질문 테이블 아이디 정상 조회 테스트")
    void findById() {
        Question expected = questions.save(QuestionTest.Q2);
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    @DisplayName("질문 테이블 정상 수정 테스트")
    void update() {
        Question expected = questions.save(QuestionTest.Q1);
        expected.setDeleted(true);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 테이블 정상 삭제 테스트")
    void delete() {
        Question expected = questions.save(QuestionTest.Q2);
        questions.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<Question> actual = questions.findById(expected.getId());
        assertThat(actual.isPresent()).isFalse();
    }
}
