package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.content.Contents;
import qna.domain.email.Email;
import qna.domain.password.Password;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.title.Title;
import qna.domain.user.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question(Title.of("title1"),  Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(Title.of("title2"),  Contents.of("contents2")).writeBy(UserTest.SANJIGI);


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    TestEntityManager manager;

    @Test
    @DisplayName("Question 생성 시 id가 null 이 아닌지, 저장 전/후의 데이터 값이 같은지 확인")
    void create() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question savedQuestion = questionRepository.save(question);

        Assertions.assertThat(savedQuestion.getId()).isNotNull();
    }

    @Test
    @DisplayName("저장한 Question 와 조회한 Question 가 같은지 (동일성)확인")
    void read() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question savedQuestion = questionRepository.save(question);

        Optional<Question> Question = questionRepository.findById(savedQuestion.getId());

        assertAll(
                () -> assertThat(Question).isPresent(),
                () -> assertThat(savedQuestion).isSameAs(Question.get())
        );
    }

    @Transactional
    @Test
    @DisplayName("Question 의 deleted 가 false 일 때 findByIdAndDeletedFalse 로 조회된 Question 가 있는지 확인")
    void findByIdAndDeletedFalse() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question saveQuestion = questionRepository.save(question);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        Assertions.assertThat(saveQuestion.getId()).isEqualTo(findQuestion.get().getId());
    }

    @Transactional
    @Test
    @DisplayName("Question 의 deleted 가 true 일 때 findByIdAndDeletedFalse 조회 시 empty 로 조회되는지 확인")
    void findByIdAndDeletedFalse2() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question saveQuestion = questionRepository.save(question);
        saveQuestion.setDeleted(true);

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        Assertions.assertThat(findQuestion).isNotPresent();
    }

    @Transactional
    @Test
    @DisplayName("id 로 Question 조회 시 Question 의 deleted 가 false 인 것만 조회되는지 확인 (모두 false 였을 때)")
    void findByQuestionIdAndDeletedFalse() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question1 = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question question2 = new Question(Title.of("title2"), Contents.of("contents")).writeBy(writer);
        Question savedQuestion1 = questionRepository.save(question1);
        Question savedQuestion2 = questionRepository.save(question2);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestions).hasSize(2),
                () -> assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion1, savedQuestion2)
        );
    }

    @Transactional
    @Test
    @DisplayName("question id 로 Question 조회 시 Question 의 deleted 가 false 인 것만 조회되는지 확인 (각각 true, false 였을 때)")
    void findByQuestionIdAndDeletedFalse2() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question1 = new Question(Title.of("title1"), Contents.of("contents")).writeBy(writer);
        Question question2 = new Question(Title.of("title2"), Contents.of("contents")).writeBy(writer);
        Question savedQuestion1 = questionRepository.save(question1);
        Question savedQuestion2 = questionRepository.save(question2);

        savedQuestion1.setDeleted(true);

        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(findQuestions).hasSize(1),
                () -> assertThat(findQuestions).containsExactlyInAnyOrder(savedQuestion2)
        );
    }

    @Transactional
    @Test
    @DisplayName("저장한 Question 삭제 후 조회 시 Question 가 없는지 확인")
    void delete() {
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        userRepository.save(writer);

        Question question = new Question(Title.of("title2"), Contents.of("contents")).writeBy(writer);
        Question savedQuestion = questionRepository.save(question);

        questionRepository.delete(savedQuestion);

        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());

        assertThat(findQuestion).isNotPresent();
    }

    @Transactional
    @Test
    void oneToMany_양방향 () {
        /**
         *     insert
         *     into
         *         answer
         *         (id, created_at, updated_at, contents, deleted, question_id, writer_id)
         *     values
         *         (null, ?, ?, ?, ?, ?, ?)
         * binding parameter [1] as [TIMESTAMP] - [2022-11-06T22:02:39.024]
         * binding parameter [2] as [TIMESTAMP] - [2022-11-06T22:02:39.024]
         * binding parameter [3] as [CLOB] - [Answers Contents1]
         * binding parameter [4] as [BOOLEAN] - [false]
         * binding parameter [5] as [BIGINT] - [null]  TODO: null값으로 넣어진것 확인
         * binding parameter [6] as [BIGINT] - [1]
         */
        User writer = new User(UserId.of("user"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        Answer answer1 = answerRepository.save(new Answer(writer,new Contents("Answers Contents1")));
        assertThat(answer1.getQuestion()).isNull();
        Q1.addAnswer(answer1);
        Question question = questionRepository.save(Q1);
        /** TODO 업데이트 쿼리 날라가는 것 확인!
         *     update
         *         answer
         *     set
         *         updated_at=?,
         *         contents=?,
         *         deleted=?,
         *         question_id=?,
         *         writer_id=?
         *     where
         *         id=?
         */
        manager.flush();
        assertThat(answer1.getQuestion()).isNotNull();
        assertThat(question.getAnswers()).hasSize(1);

        User writer2 = new User(UserId.of("user2"), Password.of("password"), Name.of("name"), Email.of("test@email.com"));
        Answer answer2 = answerRepository.save(new Answer(writer2, Contents.of("Answers Contents1")));
        question.addAnswer(answer2);
        manager.flush();
        assertThat(answer2.getQuestion()).isEqualTo(question);
        assertThat(question.getAnswers()).hasSize(2);

    }

}
