package qna.domain.answer;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.question.QuestionTest;
import qna.domain.user.User;
import qna.domain.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    private Answers sut;
    private Answer answer;
    private Answer answer2;
    private Answer answer3;

    @BeforeEach
    void setUp() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
        answer2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents2"));
        answer3 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents3"));
    }

    @Test
    void create() {
        sut = new Answers(Lists.newArrayList(answer));
        assertThat(sut.getValue()).hasSize(1);
    }

    @Test
    void add() {
        sut = new Answers(Lists.newArrayList(answer));
        sut.add(answer2);
        assertThat(sut.getValue()).hasSize(2);
    }

    @Test
    void 복수의_Answers_를_Owner가_삭제할_경우() {
        User owner = UserTest.JAVAJIGI;
        sut = new Answers(Lists.newArrayList(answer, answer2, answer3));

        sut.deletedBy(owner);

        assertThat(sut.getValue())
                .allSatisfy(ans -> assertThat(ans.deleted()).isTrue());
    }

    @Test
    void 복수의_Answers_를_Owner가_아닌_사람이_삭제할_경우() {
        User notOwnerUser = UserTest.SANJIGI;
        sut = new Answers(Lists.newArrayList(answer, answer2, answer3));

        assertThatThrownBy(() -> sut.deletedBy(notOwnerUser)).isInstanceOf(CannotDeleteException.class);
    }
}
