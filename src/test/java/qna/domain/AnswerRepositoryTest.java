package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변이 정상적으로 등록되있는지 테스트 한다")
    void saveTest(){
        Answer answer = answerRepository.save(A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getId()).isEqualTo(A1.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(answer.getCreatedAt()).isEqualTo(A1.getCreatedAt()),
                () -> assertThat(answer.getUpdatedAt()).isEqualTo(A1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 답변 조회를 테스트한다")
    void byIdAndDeletedFalseTest(){
        Answer answer = answerRepository.save(A1);
        Answer getAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(getAnswer).isNotNull(),
                () -> assertThat(getAnswer.getId()).isEqualTo(A1.getId()),
                () -> assertThat(getAnswer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(getAnswer.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(getAnswer.getWriterId()).isEqualTo(A1.getWriterId()),
                () -> assertThat(getAnswer.getCreatedAt()).isEqualTo(A1.getCreatedAt()),
                () -> assertThat(getAnswer.getUpdatedAt()).isEqualTo(A1.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("답변의 삭제여부가 true로 변경되었는지 테스트한다")
    void IsDeleteChangeTest(){
        Answer answer = answerRepository.save(A1);
        answer.setDeleted(true);
        Long id = answer.getId();
        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(id);
        assertAll(
                () -> assertThat(byIdAndDeletedFalse).isEmpty(),
                () -> assertThat(answer.isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("답변이 실제 삭제되었는지 테스트한다")
    void deleteByIdTest(){
        Answer answer = answerRepository.save(A1);
        answerRepository.deleteById(answer.getId());
        assertAll(
                () -> assertThat(answerRepository.findById(answer.getId())).isEmpty(),
                () -> assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isEmpty()
        );
    }

}
