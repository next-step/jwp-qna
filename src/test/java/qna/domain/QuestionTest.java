package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager manager;

    private List<Question> expectList;

    private User writer1;
    private User writer2;
    Question expected1;
    Question expected2;



    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
//        userRepository.deleteAll();
        writer1 = userRepository.save(UserTest.JAVAJIGI);
        writer2 = userRepository.save(UserTest.SANJIGI);
        Q1.writeBy(writer1);
        Q2.writeBy(writer2);

//        expected1 = questionRepository.save(Q1);
//        expected2 = questionRepository.save(Q2);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(expected1.getId()).isNotNull(),
                () -> assertThat(expected2.getWriter()).isNotNull()
        );
    }

    @Test
    void 조회() {
        expectList = questionRepository.findAll();
        assertAll(
            () -> assertThat(expectList).isNotNull(),
            () -> assertThat(expectList.size()).isEqualTo(2),
            () -> assertThat(expected1.getWriter()).isEqualTo(writer1),
            () -> assertThat(expected2.getWriter()).isEqualTo(writer2)
        );
    }

    @Test
    @DisplayName("질문1의 상태를 삭제로 바꾼 후 해당 질문 고유아이디이면서 삭제되지 않은 것을 조회")
    void 수정() {
        expected1.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(expected1.getId()).orElse(null)).isNull();
    }

    @Test
    void 삭제() {
        questionRepository.deleteAll();

        assertThat(questionRepository.findAll().size()).isEqualTo(0);
    }

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
        Answer answer1 = answerRepository.save(new Answer(writer1,"Answers Contents1"));
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

        Answer answer2 = answerRepository.save(new Answer(writer2,"Answers Contents1"));
        question.addAnswer(answer2);
        manager.flush();
        assertThat(answer2.getQuestion()).isEqualTo(question);
        assertThat(question.getAnswers()).hasSize(2);

    }

}
