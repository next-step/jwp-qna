package qna.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;
    @Autowired
    private DeleteHistoryRepository deletes;

    @BeforeAll
    private static void init(@Autowired UserRepository users) {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
    }

    private static Stream<Question> save_question_and_find_test() {
        return Stream.of(Q1,Q2);
    }

    @ParameterizedTest
    @DisplayName("question 엔티티 저장 후 찾기 테스트")
    @MethodSource
    void save_question_and_find_test(Question input) {
        final Question question = questions.save(input);
        assertThat(questions.findById(question.getId()).get()).isEqualTo(question);
    }

    @Test
    @DisplayName("question 엔티티 저장 후 수정 테스트")
    void save_question_and_update_test() {
        //given
        final Question question = questions.save(Q1);
        final Long id = question.getId();
        //when
        final Question searchResult = questions.findById(id).get();
        final String updateContents = "수정된 컨텐츠";
        questions.save(new Question(id, searchResult.getTitle(), updateContents));
        //then
        assertThat(questions.findById(id).get().getContents()).isEqualTo(updateContents);
    }

    private static Stream<Question> save_question_and_delete_test() {
        return Stream.of(Q1,Q2);
    }

    @ParameterizedTest
    @DisplayName("question 엔티티 저장 후 삭제 테스트")
    @MethodSource
    void save_question_and_delete_test(Question input) {
        //given
        final Question question = questions.save(input);
        final Long id = question.getId();
        final DeleteHistory deleteHistory = deletes
                .save(new DeleteHistory(ContentType.QUESTION, id, input.getWriter()));
        //when
        questions.deleteById(id);
        final DeleteHistory expected = deletes.save(deleteHistory);
        //then
        assertThat(questions.findById(id).orElse(null)).isNull();
        assertThat(deleteHistory).isEqualTo(expected);
    }

    @Test
    @DisplayName("정답이 안달려 있는 질문에 유저에게 정상 삭제권한이 있는 경우 오류가 발생하지 않는지 테스트")
    void checkDeleteAnswer_no_answer_validate_test() {
        assertThatCode(() -> Q1.checkDeleteAuth(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("정답이 달려 있는 질문에 유저에게 정상 삭제권한이 있는 경우 오류가 발생하지 않는지 테스트")
    void checkDeleteAnswer_contain_answer_validate_test() {
        final Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        q1.addAnswer(AnswerTest.A1);
        assertThatCode(() -> q1.checkDeleteAuth(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제권한이 없는 유저가 정답이 안달려 있는 질문을 삭제할 경우 오류가 발생하는지 테스트")
    void checkDeleteAnswer_no_answer_invalidate_test() {
        assertThatThrownBy(() -> Q1.checkDeleteAuth(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorMessage.CANNOT_DELETE_QUESTION_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("삭제권한이 없는 유저가 정답이 달려있는 질문을 삭제할 경우 오류가 발생하는지 테스트")
    void checkDeleteAnswer_contain_answer_invalidate_test() {
        final Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        q1.addAnswer(AnswerTest.A2);
        assertThatThrownBy(() -> q1.checkDeleteAuth(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(ErrorMessage.CANNOT_DELETE_ANSWER_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("질문만 있는 경우 저장할 삭제목록 생성 테스트")
    void deleteAndGetDeleteHistories_no_answer_test() {
        final Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        final List<DeleteHistory> deleteHistoriesNoAnswer = q1.deleteAndGetDeleteHistories();
        //list contain test
        assertThat(deleteHistoriesNoAnswer
                .contains(new DeleteHistory(ContentType.QUESTION, q1.getId(), q1.getWriter())))
                .isTrue();
        //list size test
        assertThat(deleteHistoriesNoAnswer.size()).isEqualTo(1);
        //delete flag test
        assertThat(q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문에 정답이 같이 있는 경우 저장할 삭제목록 생성 테스트")
    void deleteAndGetDeleteHistories_contain_answer_test() {
        final Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        final Answer a1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        q1.addAnswer(a1);
        final List<DeleteHistory> deleteHistories = q1.deleteAndGetDeleteHistories();
        //list contain test
        assertThat(deleteHistories
                .contains(new DeleteHistory(ContentType.ANSWER, a1.getId(), a1.getWriter())))
                .isTrue();
        //list size test
        assertThat(deleteHistories.size()).isEqualTo(2);
        //delete flag test
        assertThat(a1.isDeleted()).isTrue();
    }
}
