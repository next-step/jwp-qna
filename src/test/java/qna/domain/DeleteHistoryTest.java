package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.UnAuthorizedException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    private static Stream<Arguments> providerDeleteHistory() {
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");
        User JH = new User("지호님", "A", "지호님", "지호님_email");

        Question QUESTION1 = new Question(US, "[1] JPA 질문있습니다.", "질문 내용");

        Answer ANSWER1 = new Answer(JH, QUESTION1, "[1] JPA 답변 내용입니다.");

        DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, ANSWER1.getId(), JH);
        DeleteHistory D2 = new DeleteHistory(ContentType.QUESTION, QUESTION1.getId(), US);

        return Stream.of(
                Arguments.of(D1),
                Arguments.of(D2)
        );
    }

    private static Stream<Arguments> providerDeleteHistories() {
        User JH = new User("지호님", "A", "지호님", "지호님_email");
        User US = new User("최웅석", "A", "최웅석", "최웅석_email");

        Question QUESTION = new Question(US, "[1] JPA & 엔티티 질문있습니다.", "질문 내용");

        Answer ANSWER1 = new Answer(JH, QUESTION, "[1] JPA 답변 내용입니다.");
        Answer ANSWER2 = new Answer(JH, QUESTION, "[2] 엔티티 답변 내용입니다.");

        return Stream.of(
                Arguments.of(QUESTION, ANSWER1, ANSWER2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerDeleteHistory")
    @DisplayName("단건의 삭제 이력 테스트")
    public void 삭제_이력_생성(DeleteHistory excepted) {
        DeleteHistory actual = deleteHistoryRepository.save(excepted);

        assertThat(actual).isEqualTo(excepted);
    }

    @ParameterizedTest
    @MethodSource("providerDeleteHistories")
    @DisplayName("복수건의 삭제 이력 테스트")
    public void 삭제_이력_복수_생성(Question questionExcepted, Answer answerExcepted1, Answer answerExcepted2) {
        List<DeleteHistory> deleteHistories = Arrays.asList(new DeleteHistory(ContentType.QUESTION, questionExcepted.getId(), questionExcepted.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answerExcepted1.getId(), answerExcepted1.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answerExcepted2.getId(), answerExcepted2.getWriter()));


        List<DeleteHistory> actual = deleteHistoryRepository.saveAll(deleteHistories);

        assertAll(
                () -> assertThat(actual.get(0).getDeletedByUser()).isEqualTo(questionExcepted.getWriter()),
                () -> assertThat(actual.get(1).getDeletedByUser()).isEqualTo(answerExcepted1.getWriter()),
                () -> assertThat(actual.get(2).getDeletedByUser()).isEqualTo(answerExcepted2.getWriter())
        );
    }

    @Test
    @DisplayName("히스토리 객체 생성시 삭제 유저 검증")
    public void 필수값_검증() {
        assertThatThrownBy(() -> {
            new DeleteHistory(ContentType.QUESTION, 1L, null);
        }).isInstanceOf(UnAuthorizedException.class);
    }

}