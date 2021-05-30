package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * AnswerRepository 인터페이스 선언 메소드 테스트
 */
@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository repository;

    private User firstUser;
    private User secondUser;
    private Question firstQuestion;
    private Question secondQuestion;
    private Answer answer;
    private Answer secondAnswer;
    private List<Answer> answers;

    @BeforeEach
    public void BeforeEach() {
        this.firstUser = User.copy(UserTest.JAVAJIGI);
        this.secondUser = User.copy(UserTest.SANJIGI);
        this.firstUser.setId(1L);
        this.secondUser.setId(2L);
        this.firstQuestion = Question.copy(QuestionTest.Q1);
        this.secondQuestion = Question.copy(QuestionTest.Q2);
        this.firstQuestion.setId(1L);
        this.secondQuestion.setId(2L);
        this.answer = new Answer(this.firstUser, this.firstQuestion, AnswerTest.A1.getContents());
        this.secondAnswer = new Answer(this.secondUser, this.firstQuestion, AnswerTest.A2.getContents());
        this.answers = Arrays.asList(this.answer, this.secondAnswer);
    }

    @Test
    @DisplayName("답변 저장")
    void save() {
        // when
        Answer resultAnswer = repository.save(this.answer);

        // then
        assertThat(resultAnswer).isSameAs(this.answer);
    }

    @Test
    @DisplayName("질문ID 기준 deleted가 false인 대상 목록 조회")
    void find_by_questionId_and_deleted_is_false() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> resultAnswers = repository.findByQuestionIdAndDeletedFalse(1L);

        // then
        assertThat(resultAnswers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("ID 기준 deleted가 false인 대상 조회")
    void find_by_id_and_deleted_is_false() {
        // given
        repository.saveAll(this.answers);

        // when
        Optional<Answer> findAnswer = repository.findByIdAndDeletedFalse(this.answer.getId());

        // then
        assertThat(findAnswer.get()).isSameAs(this.answer);
    }

    @Test
    @DisplayName("질문ID와 Contents에 'Content' 문자를 포함하는 대상 목록 찾기")
    void find_by_questionId_and_like_contents_words() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> findAnswers = repository.findByQuestionIdAndContentsContaining(this.answer.getQuestionId(),
                "Content");

        // then
        assertThat(findAnswers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("질문ID가 Not Null인 목록 조회")
    void find_by_questionId_is_not_null() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> findAnswers = repository.findByQuestionIdIsNotNull();

        // then
        assertThat(findAnswers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변 작성자 ID와 질문ID를 기준으로 대상 목록 조회")
    void find_by_writerId_and_questionId() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> findAnswers = repository.findByWriterIdAndQuestionId(this.firstUser.getId(),
                this.firstQuestion.getId());

        // then
        assertThat(findAnswers.size()).isEqualTo(1);
    }
}
