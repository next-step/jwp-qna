package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("저장")
    void save() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        //When
        Question question = new Question("질문있어요", "내용입니다.", user);
        Question savedQuestion = questionRepository.save(question);

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회 (getOne)")
    void getOne() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        //When
        Question savedQuestion = questionRepository.getOne(question.getId());

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회시 (getOne) 데이터 없을경우 참조(lazyLoad)하는 시점에 예외발생")
    void getOne_null_lazy() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        //Then
        Question savedQuestion = questionRepository.getOne(12345L);

        //Then
        assertThatThrownBy(() -> System.out.println(savedQuestion))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("단건조회 (findOne)")
    void findOne() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        //When
        Question findQuestion = new Question(question.getTitle(), question.getContents());
        Question savedQuestion = questionRepository.findOne(Example.of(findQuestion)).get();

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("단건조회 (findById)")
    void findById() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        //When
        Question savedQuestion = questionRepository.findById(question.getId()).get();

        //Then
        assertAll(
            () -> assertThat(savedQuestion).isNotNull(),
            () -> assertThat(savedQuestion).isEqualTo(question)
        );
    }

    @Test
    @DisplayName("전체 리스트 조회 (findAll)")
    void findAll() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question1 = new Question("질문있어요1", "내용입니다1", user);
        Question question2 = new Question("질문있어요2", "내용입니다2", user);
        Question question3 = new Question("질문있어요3", "내용입니다3", user);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        entityManager.flush();
        entityManager.clear();

        //When
        List<Question> questions = questionRepository.findAll();

        //Then
        assertThat(questions).hasSize(3);
        assertThat(questions).contains(question1, question2, question3);
    }

    @Test
    @DisplayName("전체 리스트 조회 (삭제여부가 false인것)")
    void findByDeletedFalse() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question1 = new Question("질문있어요1", "내용입니다1", user);
        Question question2 = new Question("질문있어요2", "내용입니다2", user);
        Question question3 = new Question("질문있어요3", "내용입니다3", user);
        question3.setDeleted(true);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        entityManager.flush();
        entityManager.clear();

        //When
        List<Question> questions = questionRepository.findByDeletedFalse();

        //Then
        assertThat(questions).hasSize(2);
        assertThat(questions).contains(question1, question2);
    }

    @Test
    @DisplayName("리스트 조회 (findBy)")
    void findBy() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question1 = new Question("질문있어요1", "내용입니다1", user);
        Question question2 = new Question("질문있어요2", "내용입니다2", user);
        Question question3 = new Question("질문있어요1", "내용입니다1", user);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        entityManager.flush();
        entityManager.clear();

        //When
        List<Question> questions = questionRepository.findByTitleAndContents("질문있어요1", "내용입니다1");

        //Then
        assertThat(questions).hasSize(2);
        assertThat(questions).contains(question1, question3);
    }

    @Test
    @DisplayName("카운트")
    void count() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question1 = new Question("질문있어요1", "내용입니다1", user);
        Question question2 = new Question("질문있어요2", "내용입니다2", user);
        Question question3 = new Question("질문있어요3", "내용입니다3", user);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        entityManager.flush();
        entityManager.clear();

        //When
        long count = questionRepository.count(Example.of(new Question(question1.getTitle(), question1.getContents())));

        //Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("카운트 (countBy)")
    void countBy() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question1 = new Question("질문있어요1", "내용입니다1", user);
        Question question2 = new Question("질문있어요2", "내용입니다2", user);
        Question question3 = new Question("질문있어요3", "내용입니다3", user);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        entityManager.flush();
        entityManager.clear();

        //When
        long count = questionRepository.countById(question1.getId());

        //Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("수정")
    void update() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        Question savedQuestion = questionRepository.findById(question.getId()).get();
        savedQuestion.setTitle("제목 변경");

        Question actual = questionRepository.findById(savedQuestion.getId()).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(savedQuestion.getTitle())
        );
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        questionRepository.delete(question);
        questionRepository.flush();

        assertThat(questionRepository.findById(question.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("writer 연관관계")
    void relation_writer() {
        //Given
        User user = new User("test1", "1234", "홍길동", "hong@test.com");
        userRepository.save(user);

        Question question = new Question("질문있어요", "내용입니다.", user);
        questionRepository.save(question);

        entityManager.flush();
        entityManager.clear();

        Question savedQuestion = questionRepository.getOne(question.getId());
        User writer = savedQuestion.getWriter();
        assertAll(
            () -> assertThat(writer).isNotNull(),
            () -> assertThat(writer).isEqualTo(user)
        );
    }

    @Test
    @DisplayName("answer 연관관계")
    void relation_answer() {
        //Given
        User user1 = new User("test1", "1234", "홍길동", "hong@test.com");
        User user2 = new User("test2", "1234", "아무개", "anyone@test.com");
        User user3 = new User("test3", "1234", "정훈희", "jhh992000@google.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Question question = new Question("질문있어요", "내용입니다.", user1);
        questionRepository.save(question);

        Answer answer1 = new Answer(user1, question, "답변내용입니다1");
        Answer answer2 = new Answer(user2, question, "답변내용입니다2");
        Answer answer3 = new Answer(user3, question, "답변내용입니다3");
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.save(answer3);

        entityManager.flush();
        entityManager.clear();

        Question savedQuestion = questionRepository.getOne(question.getId());
        List<Answer> answers = savedQuestion.getAnswers();
        assertAll(
            () -> assertThat(answers).isNotEmpty(),
            () -> assertThat(answers).hasSize(3)
        );
    }

}
