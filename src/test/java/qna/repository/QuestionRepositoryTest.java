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

@DataJpaTest
@DisplayName("질문 테스트")
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

//    @PersistenceContext
//    private EntityManager entityManager;

//    private User questionWriter;
//    private User answerWriter;
//    private Question question;

//    private Answer answer;

//    @BeforeEach
//    void init() {
//        User user = new User(null, "userId", "password", "name", "email");
//        questionWriter = userRepository.save(user);
//        question = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
//        answer = answerRepository.save(new Answer(questionWriter, question, "contents1"));
//    }

//    @BeforeEach
//    void clear() {
//        answerRepository.deleteAllInBatch();
//        questionRepository.deleteAllInBatch();
//        userRepository.deleteAllInBatch();
//    }

    //    @Test
//    @DisplayName("01_질문 저장 확인")
//    void save() {
//        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
//        Question question = QuestionTest.Q1.writeBy(questionWriter);
//        Question savedQuestion = questionRepository.save(question);
//
//        assertAll(
//                () -> assertThat(savedQuestion.getId()).isNotNull(),
//                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
//                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
//                () -> assertThat(savedQuestion.getQuestionWriter()).isEqualTo(question.getQuestionWriter())
//        );
//        System.out.println(savedQuestion);
//    }
//
//    @Test
//    @DisplayName("02_저장한 질문과 해당 질문이 같은지 확인")
//    void read() {
//        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
//        Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
//
//        Optional<Question> question = questionRepository.findById(savedQuestion.getId());
//
//        assertThat(savedQuestion).isEqualTo(question.get());
//        System.out.println(savedQuestion);
//    }
//
//    @Test
//    @DisplayName("03_저장한 질문 내용 변경 시 내용 일치 여부 확인")
//    void update() {
//        System.out.println("------------------------>");
//        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
//        System.out.println("------------------------>");
//        Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
//        System.out.println("------------------------>");
//        savedQuestion.setContents(QuestionTest.Q2.getContents());
//        System.out.println("------------------------>");
//
//        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());
//
//        assertThat(savedQuestion.getContents()).isEqualTo(findQuestion.get().getContents());
//        System.out.println(savedQuestion);
//    }
//
//    @Test
//    @DisplayName("저장한 질문 삭제 확인")
//    void delete() {
//        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
//        Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
//
//        questionRepository.delete(savedQuestion);
//
//        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());
//
//        assertThat(findQuestion).isEmpty();
//        assertThat(findQuestion).isNotPresent();
//    }
//
//    @Test
//    @DisplayName("질문 삭제 불가 확인")
//    void question_cannot_deleted() {
//        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
//        Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
//        User user = userRepository.save(UserTest.SANJIGI);
//
//        assertThatThrownBy(() -> savedQuestion.delete(user))
//                .isInstanceOf(CannotDeleteException.class);
//
//        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
//
//        assertThat(savedQuestion).isEqualTo(findQuestion.get());
//    }
//
    @Test
    @DisplayName("질문 삭제 확인")
    void question_can_deleted() throws CannotDeleteException {
        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
        Question saveQuestion = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));

        saveQuestion.delete(questionWriter);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertThat(findQuestion).isEmpty();
    }

    @Test
    @DisplayName("질문 생성 후 모두 삭제 시 남은 개수 확인")
    void find_question_not_deleted_count() throws CannotDeleteException {
        User questionWriter = userRepository.save(UserTest.JAVAJIGI);
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1.writeBy(questionWriter));
        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2.writeBy(questionWriter));
        System.out.println("---***" + savedQuestion1);
        System.out.println("---***" + savedQuestion2);

        savedQuestion1.delete(questionWriter);
        savedQuestion2.delete(questionWriter);
        List<Question> findQuestions = questionRepository.findByDeletedTrue();
        System.out.println("***" + findQuestions);
        System.out.println("***" + savedQuestion1);
        System.out.println("***" + savedQuestion2);

        assertThat(findQuestions).contains(savedQuestion1, savedQuestion2);
        assertThat(findQuestions).hasSize(2);
    }

    @Test
    @DisplayName("질문 생성 후 1개만 삭제 시 남은 질문 개수 확인")
    void remain_question_delete_count() throws CannotDeleteException {
//        User user1 = userRepository.save(UserTest.JAVAJIGI);
        User user1 = userRepository.save(UserTest.JAVAJIGI);
//        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1.writeBy(user1));
        Question savedQuestion1 = save_question_1(user1);
        System.out.println("&&&" + savedQuestion1);
//        Question savedQuestion1 = QuestionTest.Q1.writeBy(UserTest.JAVAJIGI);

//        User user2 = userRepository.save(UserTest.SANJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);
        Question savedQuestion2 = save_question_2(user2);
        System.out.println("&&&" + savedQuestion2);
//        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2.writeBy(user2));
//        Question savedQuestion2 = QuestionTest.Q2.writeBy(UserTest.SANJIGI);
        savedQuestion2.delete(user2);

//        entityManager.flush();
//        entityManager.clear();

        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        System.out.println("(((" + findQuestions);
        System.out.println("(((" + savedQuestion1);

        assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion1);
        assertThat(findQuestions).hasSize(1);
    }


    @Test
    @DisplayName("질문자가 답변과 질문이 같은 경우 삭제 확인")
    public void question_answer_same_writer_delete_confirm() throws CannotDeleteException {
        User questionWriter = save_user_1();
        Question question = new Question(1L, "title1", "contents1").writeBy(questionWriter);
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
}
