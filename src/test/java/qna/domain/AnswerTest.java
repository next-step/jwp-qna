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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;
    @Autowired
    private DeleteHistoryRepository deletes;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;

    @BeforeAll
    private static void init(@Autowired UserRepository users, @Autowired QuestionRepository questions) {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
        questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
    }

    private static Stream<Answer> save_entity_and_find_test() {
        return Stream.of(A1,A2);
    }

    @ParameterizedTest
    @DisplayName("엔티티 저장 후 찾기 테스트")
    @MethodSource
    void save_entity_and_find_test(Answer input) {
        final Answer answer = answers.save(input);
        assertThat(answers.findById(answer.getId()).get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("엔티티 저장 후 수정 테스트")
    void save_entity_and_update_test() {
        //given
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(QuestionTest.Q1);
        final Answer answer = answers.save(new Answer(user, question, "답변테스트"));
        final Long id = answer.getId();
        //when
        final Answer searchResult = answers.findById(id).get();
        final String updateContents = "수정된 컨텐츠";
        answers.save(new Answer(searchResult.getId(),
                    users.findById(searchResult.getWriterId()).get(),
                    questions.findById(searchResult.getQuestionId()).get(),
                updateContents));
        //then
        assertThat(answers.findById(id).get().getContents()).isEqualTo(updateContents);
    }


    private static Stream<Answer> save_entity_and_delete_test() {
        return Stream.of(A1,A2);
    }

    @ParameterizedTest
    @DisplayName("엔티티 저장 후 삭제 테스트")
    @MethodSource
    void save_entity_and_delete_test(Answer input) {
        //given
        final Answer answer = answers.save(input);
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER,
                input.getId(), input.getWriter(), LocalDateTime.now());
        //when
        answers.deleteById(answer.getId());
        final DeleteHistory expected = deletes.save(deleteHistory);
        //then
        assertThat(answers.findById(answer.getId()).orElse(null)).isNull();
        assertThat(deleteHistory).isEqualTo(expected);
    }

    @Test
    @DisplayName("유저에게 정상 삭제권한이 있는 경우 테스트")
    void checkDeleteAnswer_validate_test() {
        assertThatCode(() -> A1.checkDeleteAuth(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제권한이 없는 유저가 삭제 테스트")
    void checkDeleteAnswer_invalidate_test() {
        assertThatThrownBy(() -> A1.checkDeleteAuth(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
