package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
@DisplayName("질문 데이터")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1")
        .writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2")
        .writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    static Stream<Arguments> example() {
        return Stream.of(Arguments.of(Q1), Arguments.of(Q2));
    }

    @ParameterizedTest
    @DisplayName("저장")
    @MethodSource("example")
    void save(Question question) {
        //when
        Question actual = questionRepository.save(question);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @ParameterizedTest
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findByIdAndDeletedFalse(Question question) {
        //given
        Question expected = questionRepository.save(question);

        //when
        Question actual = questionById(expected.getId());

        //then
        assertThat(actual)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("삭제되지 않은 질문들 검색")
    @MethodSource("example")
    void findByQuestionIdAndDeletedFalse(Question question) {
        //given
        Question expected = questionRepository.save(question);

        // when
        List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertThat(actual).contains(expected);
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        //given
        Question question = new Question("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        //then
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(answer.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories)
                .hasSize(2)
                .extracting("contentType", "deletedByUser")
                .contains(
                    tuple(ContentType.QUESTION, UserTest.JAVAJIGI),
                    tuple(ContentType.ANSWER, UserTest.JAVAJIGI)
                )
        );
    }

    @Test
    @DisplayName("본인의 질문이 아닌 경우 삭제하면 CannotDeleteException")
    void delete_NotOwn_thrownCannotDeleteException() {
        //given
        Question question = new Question("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        ThrowingCallable deleteCall = () -> question.delete(UserTest.SANJIGI);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(deleteCall)
            .withMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("다른 사람의 답변이 있는 경우 삭제하면 CannotDeleteException")
    void delete_containsOtherUsersAnswer_thrownCannotDeleteException() {
        //given
        Question question = new Question("title", "contents")
            .writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.SANJIGI, question, "contents");
        question.addAnswer(answer);

        //when
        ThrowingCallable deleteCall = () -> question.delete(UserTest.JAVAJIGI);

        //then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(deleteCall)
            .withMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }


    private Question questionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }
}
