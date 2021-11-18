package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    private final User user = new User(1L, "lsm", "password", "이승민", "test@test.com");
    private final Answer A1 = new Answer(1L, user, QuestionTest.Q1, "Answers Contents1");
    private final Answer A2 = new Answer(2L, user, QuestionTest.Q1, "Answers Contents2");
    private final Answers answers = Answers.of();

    @BeforeEach
    void setUp() {
        answers.add(A1);
        answers.add(A2);
    }

    @DisplayName("Answers일급 콜렉션 객체 생성")
    @Test
    void create() {
        assertThat(answers.size()).isEqualTo(2);
    }

    @DisplayName("답변들을 삭제 상태로 변경시켜준다.")
    @Test
    void delete() throws CannotDeleteException {
        answers.delete(user);

        assertThat(answers.getAnswerGroup().get(0).isDeleted()).isTrue();
        assertThat(answers.getAnswerGroup().get(1).isDeleted()).isTrue();
    }


    @DisplayName("답변들 삭제시 작성자와 사용자가 같을때 삭제 되는지 검증")
    @Test
    void deleteAnswersByUser() throws CannotDeleteException {
        answers.delete(A1.getWriter());

        assertThat(answers.getAnswerGroup().get(0).isDeleted()).isTrue();
        assertThat(answers.getAnswerGroup().get(1).isDeleted()).isTrue();
    }

    @DisplayName("답변들 삭제시 작성자와 사용자가 다를때 에러가 생기는지 검증")
    @Test
    void deleteAnswersByUserError() throws CannotDeleteException {
        assertThatThrownBy(() -> {
            answers.delete(new User("lsm", "password", "이승민", "test@test.com"));
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("답변List 삭제시 DeleteHistory List 생성 기능")
    @Test
    void createDeleteHistorys() throws CannotDeleteException{
        List<DeleteHistory> deleteHistorys = answers.delete(A1.getWriter());

        assertThat(deleteHistorys.size()).isEqualTo(2);
        assertThat(deleteHistorys.get(0).getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
