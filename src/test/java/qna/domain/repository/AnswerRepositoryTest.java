package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        User user = new User(1L, null, null, null, null);
        Question question = new Question(user.getId(), null, null);
        Answer expected = new Answer(user, question, "오늘 날씨 괜찮아서 패딩보다는 가디건 추천드립니다.");

        Answer actual = answerRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(user.getId())
        );
    }

    @DisplayName("Question ID 기준으로 삭제되지 않은 Answer 목록조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(3L);

        for (Answer answer : answerList) {
            assertThat(answer.getId()).isNotNull();
        }
        assertThat(answerList).hasSize(1);
    }

    @DisplayName("삭제되지 않은 Answer 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.findByIdAndDeletedFalse(3L).get();

        assertAll(
                () -> assertThat(answer.getContents()).isEqualTo("더워서 패딩은 아닌 것 같아요"),
                () -> assertThat(answer.isDeleted()).isFalse(),
                () -> assertThat(answerRepository.findByIdAndDeletedFalse(4L)).isNotPresent()
        );

    }
}
