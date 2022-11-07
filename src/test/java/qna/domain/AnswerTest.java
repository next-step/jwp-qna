package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

@DataJpaTest
public class AnswerTest {


    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager manager;

    private Question question;

    private User writer;

    private Answer answer;

    public  final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public  final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @BeforeEach
    void before() {
        writer = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
        answer = new Answer(writer, question, "Answers Contents1");
    }


    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        Answer expected = answerRepository.save(answer);
        assertAll(
            () -> assertThat(expected.getId()).isNotNull(),
            () -> assertThat(expected.getWriter()).isEqualTo(answer.getWriter())
        );
    }

    /**
     * TODO
     * Question 뒤에 붙은 $HibernateProxy$EhB1T8HK은 hibernate에서 프록시객체를 만든 가짜 객체입니다.
     * 해당 부분은 연관관계시 Lazy 로딩으로 설정하여 가짜객체를 주입한 것인데요.
     * 이 프록시 객체를 호출하면 진짜객체인 Question을 호출하게 되는 구조입니다.
     *
     * (Question@5419366d) , (Question$HibernateProxy$EhB1T8HK@481153fa) 두 객체는 메모리 주소가 다르다고 다른 객체로 볼 것인가?
     *
     *  question과 user의 equals 구문을 오버라이딩하여 비교!
     *
     */
    @Test
    @DisplayName("식별자로 답변을 조회한다.")
    void findById() {
        Answer expected = answerRepository.save(answer);
        manager.clear(); // 1차 캐시 제거
        Answer answer = answerRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(expected.getWriter()),
                () -> assertThat(answer.getWriter().getId()).isEqualTo(expected.getWriter().getId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete() {
        Answer expected = answerRepository.save(answer);
        answerRepository.delete(expected);
        manager.flush(); // 비워주면 영속컨텍스트에 저장되어있던것이 디비로 넘어가므로 삭제쿼리를 직접 디비를 호출

        assertThat(answerRepository.findById(expected.getId())).isEmpty();

        manager.clear();
    }

}
