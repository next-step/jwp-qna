package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

/**
 * QuestionRepository 인터페이스 선언 메소드 테스트
 */
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    private Question question;
    private Question firstQuestion;
    private Question secondQuestion;

    @BeforeEach
    public void beforeEach() {
        this.question = Question.copy(QuestionTest.Q1);
        this.firstQuestion = Question.copy(QuestionTest.Q1);
        this.secondQuestion = Question.copy(QuestionTest.Q2);
    }

    @Test
    @DisplayName("징문 저장")
    void save() {
        // given
        Question savedQuestion = repository.save(this.question);

        // then
        assertThat(savedQuestion).isSameAs(this.question);
    }

    @Test
    @DisplayName("PK로 질문 조회")
    void find() {
        // given
        repository.save(this.question);

        // when
        Question findQuestion = repository.findById(this.question.getId()).get();

        // then
        assertThat(findQuestion).isSameAs(this.question);
    }

    @Test
    @DisplayName("삭제 여부가 Fasle인 게시물을 ID기준으로 찾기")
    void find_by_id_and_deleted_is_false() {
        // given
        this.firstQuestion.setDeleted(false);
        this.secondQuestion.setDeleted(true);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        Optional<Question> findQuestionDeletedFalse = repository.findByIdAndDeletedFalse(this.firstQuestion.getId());
        Optional<Question> findQuestionDeletedTrue = repository.findByIdAndDeletedFalse(this.secondQuestion.getId());

        // then
        assertAll(
                () -> assertThat(findQuestionDeletedFalse.get()).isSameAs(this.firstQuestion),
                () -> assertThatThrownBy(() -> findQuestionDeletedTrue.orElseThrow(NotFoundException::new))
                        .isInstanceOf(NotFoundException.class)
        );
    }

    @Test
    @DisplayName("삭제여부가 Fasle인 질문 목록 조회")
    void find_deleted_is_false() {
        // given
        this.firstQuestion.setDeleted(false);
        this.secondQuestion.setDeleted(true);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        List<Question> findDeletedFalseQuestions = repository.findByDeletedFalse();

        // then
        assertThat(findDeletedFalseQuestions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("작성자 ID기준 질문 목록 조회")
    void find_by_writerId() {
        // given
        this.firstQuestion.writeBy(UserTest.JAVAJIGI).setDeleted(false);
        this.secondQuestion.writeBy(UserTest.JAVAJIGI).setDeleted(true);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        List<Question> questions = repository.findByWriterId(UserTest.JAVAJIGI.getId());

        // then
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("작성자ID 기준 삭제여부 true인 대상 건 삭제처리")
    void delete_by_writerId_and_deleted_true() {
        // given
        this.firstQuestion.writeBy(UserTest.JAVAJIGI).setDeleted(false);
        this.secondQuestion.writeBy(UserTest.JAVAJIGI).setDeleted(true);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        repository.deleteByWriterIdAndDeletedTrue(UserTest.JAVAJIGI.getId());
        List<Question> findQuestions = repository.findByWriterId(UserTest.JAVAJIGI.getId());

        // then
        assertThat(findQuestions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Contents에 검색대상 문자열이 포함된 목록 조회")
    void find_contents_like() {
        // given
        this.repository.save(firstQuestion);
        this.repository.save(secondQuestion);

        // when
        List<Question> findQuestions = repository.findByContentsContaining("contents");

        // then
        assertThat(findQuestions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("In절 사용한 작성자 ID기준 목록 조회")
    void find_by_writerId_in() {
        // given
        this.firstQuestion.writeBy(UserTest.JAVAJIGI).setDeleted(false);
        this.secondQuestion.writeBy(UserTest.SANJIGI).setDeleted(true);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        List<Question> findQuestions = repository.findByWriterIdIn(Arrays.asList(1L, 2L));

        // then
        assertThat(findQuestions.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("작성자ID기준 조회 및 ID 기줄 내림차순 정렬")
    void find_by_writerId_orderBy_desc() {
        // given
        this.firstQuestion.writeBy(UserTest.JAVAJIGI);
        this.secondQuestion.writeBy(UserTest.JAVAJIGI);
        repository.save(this.firstQuestion);
        repository.save(this.secondQuestion);

        // when
        List<Question> findQuestions = repository.findByWriterIdOrderByIdDesc(1L);

        // then
        assertThat(findQuestions.get(0).getId()).isGreaterThan(findQuestions.get(1).getId());
    }
}
