package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Test
    @DisplayName("저장된 질문의 대해서 답변을 저장한 후 해당 답변 ID로 저장된 답변을 조회할 수 있다.")
    void save_답변생성() {
        final User user = 사용자_저장("writer");
        final Question question = 질문_저장(user);
        final Answer actual = 답변_저장(question, user);

        final Answer expected = 답변_조회(actual);

        assertAll(
                () -> assertThat(expected).isEqualTo(actual),
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(expected.getWriter()).isSameAs(actual.getWriter()),
                () -> assertThat(expected.getContents()).isEqualTo(actual.getContents()),
                () -> assertThat(expected.getQuestion()).isSameAs(actual.getQuestion()),
                () -> assertThat(expected.isDeleted()).isEqualTo(actual.isDeleted())
        );
    }

    @Test
    @DisplayName("저장된 질문의 답변을 저장한 후 해당 답변 ID와 삭제여부로 답변을 조회할 수 있다.")
    void findByIdAndDeletedFalse_조회() {
        final User user = 사용자_저장("writer");
        final Question question = 질문_저장(user);
        final Answer actual = 답변_저장(question, user);

        final Answer expected = 답변_삭제여부_조회(actual);

        assertAll(
                () -> assertThat(expected).isEqualTo(actual),
                () -> assertThat(expected.isDeleted()).isEqualTo(actual.isDeleted())
        );
    }

    @Test
    @DisplayName("DB에 저장된 질문의 대해서 답변을 저장한 후 삭제를 진행하고 해당 답변 ID와 삭제여부로 답변을 조회 시 NotFoundException 처리한다.")
    void findByIdAndDeletedFalse_예외_삭제_조회() {
        final User user = 사용자_저장("writer");
        final Question question = 질문_저장(user);
        final Answer actual = 답변_저장(question, user);

        답변_상태_삭제(actual);

        assertThatThrownBy(() -> answers.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("")
    void findByQuestionAndDeletedFalse_정상_질문_답변조회() {
        final User user = 사용자_저장("writer");
        final Question question = 질문_저장(user);
        final Answer actual = 답변_저장(question, user);

        List<Answer> answerList = answers.findByQuestionAndDeletedFalse(question);

        assertAll(
                () -> assertThat(answerList).hasSize(1),
                () -> assertThat(answerList.get(0)).isEqualTo(actual)
        );

    }

    private User 사용자_저장(String questionWriter) {
        return users.save(new User(questionWriter, "password", "lsh", "lsh@mail.com"));
    }

    private Question 질문_저장(User user) {
        return questions.save(new Question("title", "contents", user));
    }

    private Answer 답변_저장(Question question, User user) {
        return answers.save(new Answer(user, question, "contents"));
    }

    private void 답변_상태_삭제(Answer actual) {
        actual.deleteAnswer(actual.getWriter(), LocalDateTime.now());
    }

    private Answer 답변_조회(Answer actual) {
        return answers.findById(actual.getId()).orElseThrow(NotFoundException::new);
    }

    private Answer 답변_삭제여부_조회(Answer actual) {
        return answers.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new);
    }

}
