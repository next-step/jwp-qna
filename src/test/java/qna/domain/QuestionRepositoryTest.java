package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private Question savedQuestion;
    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = users.save(new User("fdevjc", "password", "yang", "email@email.com"));
        savedQuestion = questions.save(new Question("title1", "content1").writeBy(savedUser));
    }

    @Test
    @DisplayName("deleted가 false인 질문을 조회한다.")
    void findByDeletedFalse() {
        //when
        Question expected = questions.findByDeletedFalse().get(0);

        //then
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );

        //when
        expected.setDeleted(true);
        List<Question> expectedNotFind = questions.findByDeletedFalse();

        //then
        assertThat(expectedNotFind.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("질문_id를 통해 deleted가 false인 질문을 조회한다.")
    void findByIdAndDeletedFalse() {
        //when
        Question expected = questions.findByIdAndDeletedFalse(savedQuestion.getId()).get();

        //then
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("글쓴이를 통해 질문을 조회한다.")
    void findByWriter() {
        assertThat(questions.findByWriter(savedUser).get(0)).isEqualTo(savedQuestion);
    }
}