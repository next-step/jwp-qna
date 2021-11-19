package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.NotFoundException;

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
        final Answer actual = 질문_답변_일치_저장();

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
        final Answer actual = 질문_답변_일치_저장();

        final Answer expected = 답변_삭제여부_조회(actual);

        assertAll(
                () -> assertThat(expected).isEqualTo(actual),
                () -> assertThat(expected.isDeleted()).isEqualTo(actual.isDeleted())
        );
    }

    @Test
    @DisplayName("DB에 저장된 질문의 대해서 답변을 저장한 후 삭제를 진행하고 해당 답변 ID와 삭제여부로 답변을 조회 시 NotFoundException 처리한다.")
    void findByIdAndDeletedFalse_예외_삭제_조회() {
        final Answer actual = 질문_답변_일치_저장();

        답변_상태_삭제(actual);

        assertThatThrownBy(() -> answers.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같은 경우 답변을 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteAnswer_정상_답변자_일치() throws CannotDeleteException {
        final Answer answer = 질문_답변_일치_저장();

        final DeleteHistory deleteHistory = 답변_삭제(answer);

        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(answer.getWriter()),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }


    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 다른 경우 해당 답변을 삭제할 수 없다.")
    void deleteAnswer_예외_답변자_불일치() {
        final Answer answer = 질문_답변_불일치_저장();

        assertThatThrownBy(() -> answer.deleteAnswer(answer.getQuestion().getWriter()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 모든 답변의 사용자가 같다면 삭제할 수 있다.")
    void deleteAnswer_정상_질문자_모든답변자_일치() {
        final Question question = 질문_저장("questionWriter");

        final Answers answers = 질문_답변들_일치_저장(question);

        final List<DeleteHistory> deleteHistorys = 답변들_삭제(question, answers);

        assertAll(
                () -> assertThat(deleteHistorys).hasSize(2),
                () -> assertThat(deleteHistorys.get(0).getDeletedBy()).isEqualTo(question.getWriter()),
                () -> assertThat(deleteHistorys.get(1).getDeletedBy()).isEqualTo(question.getWriter()),
                () -> assertThat(deleteHistorys.get(0).getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistorys.get(1).getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteAnswer_예외_질문자_모든답변자_불일치() {
        final Question question = 질문_저장("questionWriter");

        final Answers answers = 질문_답변들_불일치_저장(question);

        assertThatThrownBy(() -> answers.deleteAnswers(question.getWriter()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private User 질문자_저장(String questionWriter) {
        return users.save(new User(questionWriter, "password", "lsh", "lsh@mail.com"));
    }

    private User 답변자_저장(String answernWriter) {
        return users.save(new User(answernWriter, "password", "lsh", "lsh@mail.com"));
    }

    private Question 질문_저장(String writer) {
        return questions.save(new Question("title", "contents", 질문자_저장(writer)));
    }

    private Answer 질문_답변_일치_저장() {
        final User questionWriter = 질문자_저장("questionWriter");
        final Question question = questions.save(new Question("title", "contents", questionWriter));
        return answers.save(new Answer(questionWriter, question, "contents"));
    }

    private Answer 질문_답변_불일치_저장() {
        final Question question = questions.save(new Question("title", "contents", 질문자_저장("questionWriter")));
        return answers.save(new Answer(답변자_저장("answernWriter"), question, "contents"));
    }

    private Answers 질문_답변들_일치_저장(Question question) {
        final Answers answers = new Answers();
        answers.addAnswer(new Answer(question.getWriter(), question, "contents"));
        answers.addAnswer(new Answer(question.getWriter(), question, "contents"));
        return answers;
    }

    private Answers 질문_답변들_불일치_저장(Question question) {
        final Answers answers = new Answers();
        answers.addAnswer(new Answer(question.getWriter(), question, "contents"));
        answers.addAnswer(new Answer(답변자_저장("answernWriter"), question, "contents"));
        return answers;
    }

    private DeleteHistory 답변_삭제(Answer answer) {
        return answer.deleteAnswer(answer.getWriter());
    }

    private List<DeleteHistory> 답변들_삭제(Question question, Answers answers) {
        return answers.deleteAnswers(question.getWriter());
    }

    private void 답변_상태_삭제(Answer actual) {
        actual.setDeleted(true);
    }

    private Answer 답변_조회(Answer actual) {
        return answers.findById(actual.getId()).orElseThrow(NotFoundException::new);
    }

    private Answer 답변_삭제여부_조회(Answer actual) {
        return answers.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new);
    }

}
