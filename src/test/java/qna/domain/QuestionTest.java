package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("Question 테스트")
class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = UserTestFactory.create("user");
        Question question = QuestionTestFactory.create("title", "content", user);

        // when
        Question actual = questionRepository.save(question);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(question.getId()),
                () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(actual.isDeleted()).isEqualTo(question.isDeleted())
        );
    }
}
