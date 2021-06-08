package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Question q1 = QuestionTest.Q1;
    private Answer a1 = AnswerTest.A1;
    private Answer a2 = AnswerTest.A2;

    @BeforeEach
    public void setUp() {
        questionRepository.save(q1);
        answerRepository.save(a1);
        answerRepository.save(a2);
    }

    @Test
    @DisplayName("등록한 답변 확인")
    public void save(){
        assertAll(
                () -> assertThat(a1.getId()).isNotNull(),
                () -> assertThat(a1.getId()).isEqualTo(AnswerTest.A1.getId()),
                () -> assertThat(a2.getId()).isNotNull(),
                () -> assertThat(a2.getId()).isEqualTo(AnswerTest.A2.getId())
        );
    }

    @Test
    @DisplayName("질문 아이디로 삭제되지 않은 답변 목록 가져오기")
    public void findByQuestionIdAndDeletedFalse() {
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestionId()).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("아이디로 삭제되지 않은 답변 가져오기")
    public void findByIdAndDeletedFalse() {
        assertThat(answerRepository.findByIdAndDeletedFalse(a1.getId()).get().getId()).isEqualTo(a1.getId());
    }

}