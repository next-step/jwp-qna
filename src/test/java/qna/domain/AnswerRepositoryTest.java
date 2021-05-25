package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        // given
        Answer expected = AnswerTest.A1;

        // when
        Answer actual = answerRepository.save(expected);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q1.getId());
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        // when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        // then
        assertThat(answers).hasSize(2);
    }

    @DisplayName("spring data jpa에서 return type을 Optional로 감싸지 않았을때 해당되는 검색 데이터가 없을 경우 null 인지 확인한다")
    @Test
    void findByQuestionIdAndDeletedTrue() {
        // given
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        // when
        Answer answer = answerRepository.findByQuestionIdAndDeletedTrue(QuestionTest.Q1.getId());

        // then
        assertThat(answer).isNull();
    }

    @DisplayName("동일 트랜잭션 내에서 동일성 보장을 확인한다")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        Answer answer = answerRepository.save(AnswerTest.A1);

        // when
        Answer expected = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

        // then
        assertThat(answer == expected).isTrue();
    }

    @DisplayName("answer를 저장하고 수정한 후 실행되는 쿼리를 확인하고 실제 수정이 되었는지 확인한다")
    @Test
    void update() {
        // given
        Answer answer = answerRepository.save(AnswerTest.A1);

        // when
        answer.setContents("test");
        answerRepository.flush();

        // then
        assertThat(answer.getContents()).isEqualTo("test");
    }

    @DisplayName("answer를 저장하고 삭제한 후 실행되는 쿼리를 확인하고 실제 삭제가 되었는지 확인한다")
    @Test
    void delete() {
        // given
        Answer answer = answerRepository.save(AnswerTest.A1);

        // when
        answerRepository.delete(answer);
        List<Answer> answers = answerRepository.findAll();

        // then
        assertThat(answers.size()).isEqualTo(0);
    }
}
