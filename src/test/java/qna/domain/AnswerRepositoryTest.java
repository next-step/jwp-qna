package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answer id로 deleted 제거가 되지 않은것을 찾는다.")
    void findByIdAndDeletedFalse() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(AnswerTest.A1.getId());

        assertThat(byIdAndDeletedFalse.isPresent()).isTrue();
        assertThat(byIdAndDeletedFalse.get()).isEqualTo(AnswerTest.A1);
    }

    @Test
    @DisplayName("전체 데이터를 찾는다.")
    @Transactional
    void findAll() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        List<Answer> list = answerRepository.findAll();

        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("해당 id가 아닌 값들을 찾는다.")
    void findByIdNot() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        Answer a2 = answerRepository.save(AnswerTest.A2);

        List<Answer> list = answerRepository.findByIdNot(a1.getId());

        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo(a2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"변경 컨텐츠", "컨텐츠 변경"})
    @DisplayName("해당  Content 값이 변경된다 변경된다.")
    void changeContent(String text) {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answerRepository.flush();

        Answer find = answerRepository.findById(answer.getId()).orElseGet(Answer::new);
        find.setContents(text);

        assertThat(answerRepository.findById(answer.getId()).orElseGet(Answer::new)
                .getContents()).isEqualTo(text);

    }


    @Test
    @DisplayName("퀘스트 ID와 삭제되지 않은 질문들을 구한다")
    void findByQuestionIdAndDeletedFalse() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        Answer a2 = answerRepository.save(AnswerTest.A2);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestionId()))
                .hasSize(2);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        answerRepository.flush();

        answerRepository.delete(a1);
        answerRepository.flush();

        assertThat(answerRepository.findById(a1.getId()).isPresent())
                .isFalse();
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        Answer save = answerRepository.save(AnswerTest.A1);

        assertThat(answerRepository.findById(save.getId()).isPresent()).isTrue();
    }



}