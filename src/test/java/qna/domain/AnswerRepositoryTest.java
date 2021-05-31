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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User javajigiUser;
    private User sanjigiUser;
    private Question firstQuestion;
    private Question secondQuestion;
    private Answer firstAnswer;
    private Answer secondAnswer;
    private List<Answer> answers;

    @BeforeEach
    public void BeforeEach() {
        this.javajigiUser = User.copy(UserTest.JAVAJIGI);
        this.sanjigiUser = User.copy(UserTest.SANJIGI);
        userRepository.saveAll(Arrays.asList(this.javajigiUser, this.sanjigiUser));

        this.firstQuestion = Question.copy(QuestionTest.Q1);
        this.secondQuestion = Question.copy(QuestionTest.Q2);
        questionRepository.saveAll(Arrays.asList(this.firstQuestion, this.secondQuestion));

        this.firstAnswer = new Answer(this.javajigiUser, this.firstQuestion, AnswerTest.A1.getContents());
        this.secondAnswer = new Answer(this.sanjigiUser, this.firstQuestion, AnswerTest.A2.getContents());
        this.answers = Arrays.asList(this.firstAnswer, this.secondAnswer);
    }

    @Test
    @DisplayName("답변 저장")
    void save() {
        // when
        Answer resultAnswer = repository.save(this.firstAnswer);

        // then
        assertThat(resultAnswer).isSameAs(this.firstAnswer);
    }

    @Test
    @DisplayName("질문ID 기준 deleted가 false인 대상 목록 조회")
    void find_by_questionId_and_deleted_is_false() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> resultAnswers = repository.findByQuestionAndDeletedFalse(this.firstQuestion);

        // then
        assertThat(resultAnswers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("ID 기준 deleted가 false인 대상 조회")
    void find_by_id_and_deleted_is_false() {
        // given
        repository.saveAll(this.answers);

        // when
        Optional<Answer> findAnswer = repository.findByIdAndDeletedFalse(this.firstAnswer.getId());

        // then
        assertThat(findAnswer.get()).isSameAs(this.firstAnswer);
    }

    @Test
    @DisplayName("질문ID와 Contents에 'Content' 문자를 포함하는 대상 목록 찾기")
    void find_by_questionId_and_like_contents_words() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> findAnswers = repository.findByQuestionAndContentsContaining(this.firstAnswer.getQuestion(),
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
        List<Answer> findAnswers = repository.findByQuestionIsNotNull();

        // then
        assertThat(findAnswers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변 작성자 ID와 질문ID를 기준으로 대상 목록 조회")
    void find_by_writerId_and_questionId() {
        // given
        repository.saveAll(this.answers);

        // when
        List<Answer> findAnswers = repository.findByWriterAndQuestion(this.javajigiUser, this.firstQuestion);

        // then
        assertThat(findAnswers.size()).isEqualTo(1);
    }
}
