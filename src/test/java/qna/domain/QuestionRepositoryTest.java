package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        //given
        user = users.save(UserTest.JAVAJIGI);
        question = new Question("title1", "contents1").writeBy(user);
    }

    @DisplayName("저장 테스트")
    @Test
    void saveWithUser() {

        Question result = questions.save(question);

        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(question);
    }

    @DisplayName("검색 테스트")
    @Test
    void findById() {
        questions.save(question);

        Question result = questions.findById(question.getId()).get();

        assertThat(result).isEqualTo(question);
    }

    @DisplayName("삭제 테스트")
    @Test
    void delete() {
        questions.save(question);

        questions.delete(question);

        Question result = questions.findById(question.getId()).orElseGet(() -> null);
        assertThat(result).isNull();
    }


}
