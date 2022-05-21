package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문을 등록한 내용이 정상적으로 등록되었는지 확인 테스트")
    void save() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        Assertions.assertThat(questionRepository.findAll()).size().isEqualTo(2);
    }

    @Test
    @DisplayName("질문을 등록 후 답변이 정상적으로 저장 되었는지 확인 테스트")
    void find() {
        questionRepository.save(Q1);

        Question question = questionRepository.findByIdAndDeletedFalse(Q1.getId()).get();

        Assertions.assertThat(question).isNotNull();
        Assertions.assertThat(question.getId()).isEqualTo(Q1.getId());
        Assertions.assertThat(question.isDeleted()).isFalse();
    }

}
