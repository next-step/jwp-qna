package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

class QuestionRepositoryTest extends JpaTest {

    @Autowired
    private QuestionRepository questions;

    @DisplayName("삭제 상태가 아닌 질문들을 조회한다.")
    @Test
    void findByDeletedFalse() {
        //given
        Question question1 = questions.save(Q1);
        Question question2 = questions.save(Q2);
        question1.setDeleted(false);
        question2.setDeleted(true);

        //when
        List<Question> actual = questions.findByDeletedFalse();

        //then
        assertThat(actual.size()).isEqualTo(1);
    }

    @DisplayName("질문 아이디에 해당하는 질문이 삭제 상태가 아닌 것을 리턴한다.")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        Question question1 = questions.save(Q1);
        question1.setDeleted(false);

        //when
        Question actual = questions.findByIdAndDeletedFalse(question1.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(question1);
    }

    @DisplayName("질문 아이디에 해당하는 질문이 삭제 상태가 아닌 것이 없다면 예외를 발생시킨다.")
    @Test
    void findByIdAndDeletedFalseException() {
        //given
        Question question1 = questions.save(Q1);
        question1.setDeleted(true);

        //when
        assertThatThrownBy(() -> questions.findByIdAndDeletedFalse(question1.getId())
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}