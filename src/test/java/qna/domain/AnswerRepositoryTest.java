package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @DisplayName("질문 아이디에 해당하는 답변 목록 중 삭제 상태가 아닌 것들만 리턴한다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        //given
        answers.save(A1);
        answers.save(AnswerTest.A2);
        A1.setDeleted(false);
        AnswerTest.A2.setDeleted(true);

        //when
        List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

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
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(A1.getId());

        //then
        assertThat(actual.get()).isSameAs(A1);
    }

    @DisplayName("답변 아이디에 해당하는 답변이 삭제 상태라면 빈 Optional 객체를 리턴한다.")
    @Test
    void findByIdAndDeletedFalseIfPresent() {
        //given
        answers.save(A1);
        A1.setDeleted(true);

        //when
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(A1.getId());

        //then
        assertThat(actual.isPresent()).isFalse();
    }
}