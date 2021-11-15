package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {
    private User questionWriter;
    private User answerWriter;
    private Question question;
    private Answer answer;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @BeforeEach
    void setUp() {
        questionWriter = new User("questionWriter", "password", "lsh", "lsh@mail.com");
        answerWriter = new User("answerWriter", "password", "lsh", "lsh@mail.com");
        question = new Question("title", "contents");
        answer = new Answer(answerWriter, question, "contents");
    }

    @Test
    @DisplayName("DB에 저장된 질문의 대해서 답변을 저장한 후 해당 답변 ID로 저장된 답변을 조회할 수 있다.")
    void save_답변생성() {
        final Answer expected = answer;

        final Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isSameAs(expected.getWriter()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getQuestion()).isSameAs(expected.getQuestion()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("DB에 저장된 질문의 대해서 답변을 저장한 후 해당 답변 ID와 삭제여부로 답변을 조회할 수 있다.")
    void findByIdAndDeletedFalse_조회() {
        final Answer expected = initSaved(answer);

        final Answer actual = answers.findByIdAndDeletedFalse(expected.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("DB에 저장된 질문의 대해서 답변을 저장한 후 삭제를 진행하고 해당 답변 ID와 삭제여부로 답변을 조회 시 NotFoundException 처리한다.")
    void findByIdAndDeletedFalse_예외_삭제_조회() {
        final Answer actual = initSaved(answer);

        actual.setDeleted(true);

        assertThatThrownBy(() ->
                answers.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    private Answer initSaved(Answer answer) {
        users.save(questionWriter);
        users.save(answerWriter);
        question.writeBy(questionWriter);
        questions.save(question);
        return answers.save(answer);
    }
}
