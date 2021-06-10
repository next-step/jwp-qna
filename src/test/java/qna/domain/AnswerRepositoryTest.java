package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
// DataJpaTest는 default로 내장 db를 사용한다.
// application properties로 적용된 datasource를 사용하고 싶다면 AutoConfigureTestDatabase를 추가해줘야함
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    QuestionRepository questionRepository;

    private Answer a1 ;
    private Answer a2 ;
    private Question q1 ;

    @BeforeEach
    public void setup() {
        a1= new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        a2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
        q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("save 테스트")
    public void save() {
        assertThat(a1.getId()).isNull();
        Answer a2 = answerRepository.save(a1);
        assertThat(a2).isSameAs(a1);
        assertThat(a2.getId()).isNotNull();
    }

    @Test
    @DisplayName("1차 캐시로 조회로 인한 sql 체크")
    public void findByName() {
        Answer a2 = answerRepository.save(a1);
        assertThat(a2).isSameAs(a1);
        assertThat(a2.getId()).isNotNull();
        Answer a3 = answerRepository.findById(a2.getId()).get();
        assertThat(a3).isSameAs(a2);
    }

    @Test
    @DisplayName("questionId이면서 삭제되지 않은건 조회")
    public void findByQuestionIdAndDeletedFalse() {
        // Question을 persist로 id 생성
        Question q2 = questionRepository.save(q1);
        assertThat(q2).isSameAs(q1);

        Answer persistA1 = answerRepository.save(a1);
        Answer persistA2 = answerRepository.save(a2);

        assertThat(persistA1).isSameAs(a1);
        assertThat(persistA2).isSameAs(a2);

        a1.setQuestionId(q1.getId());
        a2.setQuestionId(q1.getId());

//        answerRepository.flush();

        // 2개가 조회
        assertThat(a1.getId()).isNotNull();
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(q1.getId());
        assertThat(answers).hasSize(2);
        assertThat(answers).contains(a1,a2);

        a2.setDeleted(true);

        // 1개 조회
        answers = answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestionId());
        assertThat(answers).hasSize(1);
        assertThat(answers).contains(a1);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    public void findByIdAndDeletedFalse() {
        Answer a1 = answerRepository.save(AnswerTest.A1);

        // 1개 조회
        Answer a2 = answerRepository.findByIdAndDeletedFalse(a1.getId()).get();

        assertThat(a2).isSameAs(a1);

        // deleted가 적용되므로 0개
        a1.setDeleted(true);
        assertThatThrownBy(()->answerRepository
                .findByIdAndDeletedFalse(a1.getId())
                .orElseThrow(()->new RuntimeException("invalid")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("invalid");
    }

    @Test
    public void detached() {
        Answer a1 = answerRepository.save(AnswerTest.A1);
        a1.setDeleted(true);
        // 없으므로 throw exception
        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
                .isInstanceOf(NoSuchElementException.class);

        entityManager.detach(a1);
        // detach상태에서는 트래킹이 안된다.
        a1.setDeleted(false);
        // false임에도 트래킹이 안되기때문에 exception
        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

}
