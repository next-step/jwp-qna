package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.SANJIGI;

class AnswersTest {

    @Test
    @DisplayName("댓글들의 작성자가 아닌 경우 예외처리한다.")
    void validateOwners_test() {
        //given
        Answers answers = new Answers(List.of(A1));

        //when
        assertThrows(CannotDeleteException.class,
                () -> answers.validateOwners(SANJIGI)
        );
    }

    @Test
    void deleteAllAndAddHistories_test() {
        //given
        Answers answers = new Answers(List.of(A1, A2));
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        //when
        List<DeleteHistory> newDeleteHistories = answers.deleteAllAndAddHistories(deleteHistories);

        //then
        assertThat(newDeleteHistories).containsExactly(
                new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, A2.getId(), A2.getWriter(), LocalDateTime.now())
        );
    }
}
