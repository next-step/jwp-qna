package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
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

    @Autowired
    UserRepository userRepository;

    private Answer a1 ;
    private Answer a2 ;
    private Question q1 ;
    private User u1;

    @BeforeEach
    public void setup() {
        u1 = new User("userid","password","name","email");
        q1 = new Question("title1", "contents1").writeBy(u1);
        a1= new Answer(u1, q1, "Answers Contents1");
        a2 = new Answer(u1, q1, "Answers Contents2");
        q1.setWriter(u1);
        a1.setWriter(u1);
        a2.setWriter(u1);

        // cascade persist로 question과 answer 생성
        userRepository.save(u1);
        userRepository.flush();

    }

    @Test
    @DisplayName("save 테스트")
    public void save() {
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
        userRepository.save(u1);
        Question q2 = questionRepository.save(q1);
        assertThat(q2).isSameAs(q1);

        Answer persistA1 = answerRepository.save(a1);
        Answer persistA2 = answerRepository.save(a2);

        assertThat(persistA1).isSameAs(a1);
        assertThat(persistA2).isSameAs(a2);

        a1.setQuestion(q1);
        a2.setQuestion(q1);

//        answerRepository.flush();

        // 2개가 조회
        assertThat(a1.getId()).isNotNull();
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(q1.getId());
        assertThat(answers).hasSize(2);
        assertThat(answers).contains(a1,a2);

        a2.setDeleted(true);

        // 1개 조회
        answers = answerRepository.findByQuestionIdAndDeletedFalse(a1.getQuestion().getId());
        assertThat(answers).hasSize(1);
        assertThat(answers).contains(a1);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    public void findByIdAndDeletedFalse() {

        assertThat(a1.getId()).isNotNull();
        assertThat(a2.getId()).isNotNull();
        assertThat(q1.getId()).isNotNull();

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
        Answer a2 = answerRepository.save(a1);
        // merge시 기존에 있다면 동일성 보장
        assertThat(a2).isSameAs(a1);
        assertThat(a1.getId()).isNotNull();
        a1.setDeleted(true);
        // 없으므로 throw exception
        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
                .isInstanceOf(NoSuchElementException.class);

//        detach를 조회하려고하면 영속성이 안되있으므로 에러가 발생한다.
        entityManager.detach(a1);
        // detach상태에서는 트래킹이 안된다.
        a1.setDeleted(false);
        // false임에도 트래킹이 안되기때문에 exception
        Answer a3 = answerRepository.findById(a1.getId()).get();
        assertThat(a3).isEqualTo(a1);
//        assertThatThrownBy(()->answerRepository.findByIdAndDeletedFalse(a1.getId()).get())
//                .isInstanceOf(NoSuchElementException.class);
//        Answer a3 = answerRepository.findByIdAndDeletedFalse(a1.getId()).get();
//        assertThat(a3).isSameAs(a1);

    }

    @Test
    @DisplayName("answer 삭제시 history 추가")
    public void historyTest() throws CannotDeleteException {
        a1.delete(u1);
        assertThat(u1.getDeleteHistories()).contains(new DeleteHistory(ContentType.ANSWER,a1.getId(),u1, LocalDateTime.now()));
        assertThat(a1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("answer 다른사람이 삭제시 exception")
    public void deleteByOther() {
        User u2 = new User("userid2","password","name","email1");
        assertThatThrownBy(()->a1.delete(u2))
                .isInstanceOf(CannotDeleteException.class);
        assertThat(a1.isDeleted()).isFalse();
    }

}
