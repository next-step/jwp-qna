package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @AfterEach
    public void tearDown() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("자바지기 사용자가 a1 등록 성공")
    public void saveAnswerByJavajigiSuccess() {
        Answer answer1 = AnswerTest.A1;
        Answer save = answerRepository.save(answer1);

        assertAll(() -> {
            assertThat(save.getWriterId()).isEqualTo(answer1.getWriterId());
            assertThat(save.getQuestionId()).isEqualTo(answer1.getQuestionId());
        });

    }

    @Test
    @DisplayName("답변 찾기 by id 성공")
    public void findByIdSuccess() {
        Answer answer1 = AnswerTest.A1;
        answerRepository.save(answer1);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer1.getId());

        assertAll(() -> {
            assertThat(optionalAnswer.isPresent()).isTrue();
            Answer findAnswer = optionalAnswer.get();
            assertThat(findAnswer.getQuestionId()).isEqualTo(answer1.getQuestionId());
        });

    }

    @Test
    @DisplayName("답변 찾기 by question id 성공")
    public void findByQuestionIdSuccess() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
        List<Answer> answers = answerRepository
            .findByQuestionIdAndDeletedFalse(AnswerTest.A2.getQuestionId());

        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변이 해당 사용자의 답변인지 체크 성공")
    public void isOwnerSuccess() {
        Answer save = answerRepository.save(AnswerTest.A1);
        assertThat(save.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 플래그 true 변경 성공")
    public void updateDeletedSuccess() {
        Answer save = answerRepository.save(AnswerTest.A1);

        save.setDeleted(true);
        Answer deleted = answerRepository.save(save);

        assertAll(() -> {
            assertThat(deleted.getId()).isEqualTo(save.getId());
            assertThat(deleted.isDeleted()).isTrue();
        });
    }

}