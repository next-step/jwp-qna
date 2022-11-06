package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest extends NewEntityTestBase {

    @Test
    @DisplayName("조회 후 하나를 Deleted True로 설정하면 Delete False인 것이 하나만 조회됨")
    void findByDeletedFalse() {

        List<Question> all = questionRepository.findAll();
        all.get(0).markDeleted(true);

        List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();

        assertThat(byDeletedFalse.size()).isEqualTo(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> found = questionRepository.findByIdAndDeletedFalse(Q1.getId());

        assertThat(found.get().getContents()).isEqualTo(Q1.getContents());
    }

    @Test
    @DisplayName("column 길이 제약조건을 어기면 DataIntegrityViolationException 이 발생함")
    void test4() {
        String stringLengthOver = prepareContentsOverLength(101);

        Question question = new Question(null, stringLengthOver, "contents", NEWUSER1);

        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement; SQL [n/a]");
    }

    @Test
    @DisplayName("신규 Question 엔티티와 신규 User 엔티티를 연결하여 저장하면 모두 Id가 생김")
    void test5() {
        Question save = questionRepository.save(Q1);

        assertAll(
                () -> assertThat(save.getWriter().getId()).isPositive(),
                () -> assertThat(save.getId()).isPositive()
        );
    }


    private static String prepareContentsOverLength(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('a');
        }
        return sb.toString();
    }
}