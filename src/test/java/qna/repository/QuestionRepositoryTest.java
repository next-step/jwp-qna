package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;
import qna.exception.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("질문 테스트")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("질문 저장 확인")
    void save() {
        User questionWriter = save_user_1();
        Question question = save_question_1(questionWriter);
        Question savedQuestion = questionRepository.save(question);

        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion).isEqualTo(question)
        );
        System.out.println(savedQuestion);
    }

    @Test
    @DisplayName("저장한 질문과 해당 질문이 같은지 확인")
    void read() {
        User questionWriter = save_user_1();
        Question savedQuestion = save_question_1(questionWriter);

        Optional<Question> question = questionRepository.findById(savedQuestion.getId());

        assertThat(savedQuestion).isEqualTo(question.get());
        System.out.println(savedQuestion);
    }

    @Test
    @DisplayName("저장한 질문 내용 변경 시 내용 일치 여부 확인")
    void update() {
        User questionWriter = save_user_1();
        Question savedQuestion = save_question_1(questionWriter);
        savedQuestion.setContents("new Contents");

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(savedQuestion.getContents()).isEqualTo(findQuestion.get().getContents());
    }

    @Test
    @DisplayName("저장한 질문 삭제 확인")
    void delete() {
        User questionWriter = save_user_1();
        Question savedQuestion = save_question_1(questionWriter);

        questionRepository.delete(savedQuestion);

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(findQuestion).isEmpty();
        assertThat(findQuestion).isNotPresent();
    }

    @Test
    @DisplayName("질문 삭제 불가 확인")
    void question_cannot_deleted() {
        User questionWriter = save_user_1();
        Question savedQuestion = save_question_1(questionWriter);
        User user = save_user_2();

        assertThatThrownBy(() -> savedQuestion.delete(user))
                .isInstanceOf(CannotDeleteException.class);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertThat(savedQuestion).isEqualTo(findQuestion.get());
    }

    @Test
    @DisplayName("질문 삭제 확인")
    void question_can_deleted() throws CannotDeleteException {
        User questionWriter = save_user_1();
        Question saveQuestion = save_question_1(questionWriter);

        saveQuestion.delete(questionWriter);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertThat(findQuestion).isEmpty();
    }

    @Test
    @DisplayName("질문 생성 후 모두 삭제 시 남은 개수 확인")
    void find_question_not_deleted_count() throws CannotDeleteException {
        User questionWriter = save_user_1();
        Question savedQuestion1 = save_question_1(questionWriter);
        Question savedQuestion2 = save_question_2(questionWriter);

        savedQuestion1.delete(questionWriter);
        savedQuestion2.delete(questionWriter);
        List<Question> findQuestions = questionRepository.findByDeletedTrue();

        assertThat(findQuestions).contains(savedQuestion1, savedQuestion2);
        assertThat(findQuestions).hasSize(2);
    }

    @Test
    @DisplayName("질문 생성 후 1개만 삭제 시 남은 질문 개수 확인")
    void remain_question_delete_count() throws CannotDeleteException {
        User user1 = save_user_1();
        Question savedQuestion1 = save_question_1(user1);

        User user2 = save_user_2();
        Question savedQuestion2 = save_question_2(user2);
        savedQuestion2.delete(user2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion1);
        assertThat(findQuestions).hasSize(1);
    }

    @Test
    @DisplayName("질문자가 답변과 질문이 같은 경우 삭제 확인")
    public void question_answer_same_writer_delete_confirm() throws CannotDeleteException {
        User questionWriter = save_user_1();
        Question question = save_question_1(questionWriter);
        Answer answer = new Answer(1L, questionWriter, question, "Answers Contents1");
        question.addAnswer(answer);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
        System.out.println(answer);

        question.delete(questionWriter);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        System.out.println(answer);
    }

    @Test
    @DisplayName("질문자가 답변과 질문이 다른 경우 삭제 확인")
    public void question_answer_diff_writer_delete_confirm() {
        User questionWriter = save_user_1();
        Question question = save_question_1(questionWriter);
        Answer answer = new Answer(1L, questionWriter, question, "Answers Contents1");
        User answerWriter = save_user_2();

        question.addAnswer(answer);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();

        assertThatThrownBy(() -> {
            question.delete(answerWriter);
        }).isInstanceOf(CannotDeleteException.class);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
    }


    private User save_user_1() {
        User user = new User(null, "javajigi", "passwrod", "name", "email");
        return userRepository.save(user);
    }

    private User save_user_2() {
        User user = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");
        return userRepository.save(user);
    }

    private Question save_question_1(User user) {
        Question Q1 = new Question("title1", "contents1").writeBy(user);
        return questionRepository.save(Q1);
    }

    private Question save_question_2(User user) {
        Question Q2 = new Question("title2", "contents2").writeBy(user);
        return questionRepository.save(Q2);
    }

    private Answer save_answer_1(User user, Question question) {
        Answer A1 = new Answer(user, question, "content");
        return answerRepository.save(A1);

    }

}
