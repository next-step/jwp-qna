package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

class AnswerRepositoryTest extends JpaTest {

    @Autowired
    private AnswerRepository answers;

    @DisplayName("질문 아이디에 해당하는 답변 목록 중 삭제 상태가 아닌 것들만 리턴한다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        answers.save(A1);
        answers.save(A2);
        A1.setDeleted(false);
        A2.setDeleted(true);

        //when
        List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(Q1.getId());

        //then
        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태가 아니라면 리턴한다.")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        answers.save(A1);
        A1.setDeleted(false);

        //when
        Answer actual = answers.findByIdAndDeletedFalse(A1.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(A1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태라면 EntityNotFoundException 을 발생시킨다.")
    @Test
    void findByIdAndDeletedFalseIfPresent() {
        //given
        answers.save(A1);
        A1.setDeleted(true);

        //when
        assertThatThrownBy(() -> answers.findByIdAndDeletedFalse(A1.getId())
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}