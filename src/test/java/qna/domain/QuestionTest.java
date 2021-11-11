package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repos.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository repository;

    @DisplayName("Question 저장 테스트")
    @Test
    void save() {
        Question expected = Q1;
        Question actual = repository.save(Q1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @DisplayName("Question deleted=false 경우 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question q1 = repository.save(Q1);
        Question q2 = repository.save(Q2);

        Q2.setDeleted(true);

        repository.save(Q2);

        Optional<Question> a1Result = repository.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> a2Result = repository.findByIdAndDeletedFalse(q2.getId());
        List<Question> result = repository.findByDeletedFalse();

        assertAll(
                () -> assertThat(a1Result).isNotEmpty(),
                () -> assertThat(a2Result).isEmpty(),
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }
}
