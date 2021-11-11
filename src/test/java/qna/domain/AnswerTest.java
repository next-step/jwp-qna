package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@DisplayName("Answer 테스트")
class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = UserTestFactory.create("user");
        Question question = QuestionTestFactory.create("title", "contents", user);
        Answer answer = AnswerTestFactory.create(user, question, "Answers Contents");

        // when
        Answer result = answerRepository.save(answer);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(answer.getId()),
                () -> assertThat(result.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(result.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(result.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(result.isDeleted()).isEqualTo(answer.isDeleted())
        );
    }
}
