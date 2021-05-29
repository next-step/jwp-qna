package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer answer_1;
    private Answer answer_2;
    private Answer saved;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "question", "질문내용");
        answer_1 = new Answer(UserTest.JAVAJIGI, question, AnswerTest.A1.getContents());
        answer_2 = new Answer(UserTest.SANJIGI, question, AnswerTest.A2.getContents());
        saved = answerRepository.save(answer_1);
    }

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getWriterId()).isEqualTo(answer_1.getWriterId()),
                () -> assertThat(saved.getWriterId()).isEqualTo(answer_1.getWriterId()),
                () -> assertThat(saved.getContents()).isEqualTo(answer_1.getContents())
        );
    }

    @Test
    @DisplayName("answer 수정 테스트")
    void update() {
        String changedContents = "내용 바꿔보기";
        saved.setContents(changedContents);

        Optional<Answer> updatedAnswer = answerRepository.findById(saved.getId());

        assertThat(updatedAnswer.get().getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("answer 제거 테스트")
    void delete() {
        answerRepository.delete(saved);

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("questionId로 매칭되는 질문에 대한 답변들 정상 조회하는지 테스트")
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.saveAll(Arrays.asList(answer_1, answer_2));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(answers).contains(answer_1, answer_2)
        );
    }
//
    @Test
    @DisplayName("questionId로 매칭되는 answer값 삭제처리 되었을 시 조회 불가 테스트")
    void findByQuestionIdAndDeletedFalse_failCase() {
        answer_2.setDeleted(true);

        answerRepository.saveAll(Arrays.asList(answer_1, answer_2));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(answers).contains(answer_1),
                () -> assertThat(answers).doesNotContain(answer_2)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 정상 조회하는지 테스트")
    public void findByIdAndDeletedFalse() {
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(saved.getId());

        assertAll(
                () -> assertThat(findedAnswer.isPresent()).isTrue(),
                () -> assertThat(findedAnswer.get()).isEqualTo(saved)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 삭제시 조회 불가 테스트")
    public void findByIdAndDeletedFalse_failCase() {
        saved.setDeleted(true);
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(saved.getId());

        assertThat(findedAnswer.isPresent()).isFalse();
    }
}
