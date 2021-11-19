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
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DB에 저장된 질문을 저장한 후 해당 질문의 ID로 조회할 수 있다.")
    void save_질문생성() {
        final Question actual = 질문_저장("questionWriter");

        final Question expected = 저장된_질문_조회(actual);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isSameAs(expected.getWriter()),
                () -> assertThat(actual.getAnswers()).isSameAs(expected.getAnswers()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("질문을 저장한 후 해당 ID와 삭제되지 않은 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_조회() {
        final Question actual = 질문_저장("questionWriter");

        final Question expected = 저장된_질문_삭제조건_조회(actual);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );

    }

    @Test
    @DisplayName("질문을 저장한 후 삭제 처리를 한 뒤 해당 ID와 삭제 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_삭제_조회() {
        final Question actual = 질문_저장("questionWriter");

        actual.setDeleted(true);

        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("로그인한 사용자와 질문자가 같은 경우 답변이 없다면 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteQuestion_정상_질문자_일치() throws CannotDeleteException {
        final Question actual = 질문_저장("questionWriter");

        final List<DeleteHistory> expecteddDleteHisoties = 질문_삭제_삭제이력_저장(actual);

        final Question expectedAnswer = questionRepository.findById(actual.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(expectedAnswer.isDeleted()).isTrue(),
                () -> assertThat(expecteddDleteHisoties).hasSize(1),
                () -> assertThat(expecteddDleteHisoties.get(0).getDeletedBy()).isEqualTo(actual.getWriter()),
                () -> assertThat(expecteddDleteHisoties.get(0).getCreateDate()).isNotNull(),
                () -> assertThat(expecteddDleteHisoties.get(0).getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }

    @Test
    @DisplayName("로그인한 사용자와 질문자가 다른 경우 질문을 삭제할 수 없다.")
    void deleteQuestion_예외_질문자_불일치() throws CannotDeleteException {
        final Question question = 질문_저장("questionWriter");

        assertThatThrownBy(() -> question.deleteQuestion(질문자_저장("anotherWriter")))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 모든 답변자가 질문자와 같다면 삭제할 수 있다")
    void deleteQuestion_정상_질문자_모든답변자_일치() {
        final Question question = 질문자_모든답변자_일치("questionWriter");

        final List<DeleteHistory> deleteHistories = 질문_모든답변_삭제(question);

        final List<Answer> answers = answerRepository.findByQuestion(question);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(3),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(answers.get(0).isDeleted()).isTrue(),
                () -> assertThat(answers.get(1).isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteQuestion_예외_질문자_다른답변자() {
        final Question question = 질문자_모든답변자_불일치("questionWriter");

        assertThatThrownBy(() -> question.deleteQuestion(question.getWriter()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private User 질문자_저장(String writer) {
        return userRepository.save(new User(writer, "password", "lsh", "lsh@mail.com"));
    }

    private Question 질문_저장(String writer) {
        return questionRepository.save(new Question("title", "contents", 질문자_저장(writer)));
    }

    private Question 저장된_질문_조회(Question actual) {
        return questionRepository.findById(actual.getId()).orElseThrow(NotFoundException::new);
    }

    private Question 저장된_질문_삭제조건_조회(Question actual) {
        return questionRepository.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new);
    }

    private Question 질문자_모든답변자_일치(String writer) {
        final Question question = 질문_저장(writer);
        final Answers answers = new Answers();
        answers.addAnswer(answerRepository.save(new Answer(question.getWriter(), question, "contents")));
        answers.addAnswer(answerRepository.save(new Answer(question.getWriter(), question, "contents")));
        question.setAnswers(answers);
        return question;
    }

    private Question 질문자_모든답변자_불일치(String writer) {
        final User questionWriter = 질문자_저장(writer);
        final User answerWriter = 질문자_저장("anotherWriter");
        final Question question = new Question("title", "contents", questionWriter);
        final Answers answers = new Answers();
        answers.addAnswer(answerRepository.save(new Answer(questionWriter, question, "contents")));
        answers.addAnswer(answerRepository.save(new Answer(answerWriter, question, "contents")));
        question.setAnswers(answers);
        return question;
    }

    private List<DeleteHistory> 질문_삭제_삭제이력_저장(Question actual) {
        List<DeleteHistory> deleteHistories = actual.deleteQuestion(actual.getWriter());
        return deleteHistoryRepository.saveAll(deleteHistories);
    }

    private List<DeleteHistory> 질문_모든답변_삭제(Question question) {
        return question.deleteQuestion(question.getWriter());
    }
}
