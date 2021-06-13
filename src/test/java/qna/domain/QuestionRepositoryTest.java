package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;


    private Answer a1 ;
    private Answer a2 ;
    private Question q1 ;
    private Question q2 ;
    private User u1;

    @BeforeEach
    public void setup() {
        u1 = new User("userid","password","name","email");
        q1 = new Question("title1", "contents1").writeBy(u1);
        q2 = new Question("title2", "contents2").writeBy(u1);
        a1= new Answer(u1, q1, "Answers Contents1");
        a2 = new Answer(u1, q1, "Answers Contents2");
        q1.setWriter(u1);
        q2.setWriter(u1);
        a1.setWriter(u1);
        a2.setWriter(u1);

        q1.addAnswer(a1);
        q1.addAnswer(a2);

        // cascade persist로 question과 answer 생성
        userRepository.save(u1);
        userRepository.flush();

    }
    @Test
    @DisplayName("save merge비교")
    public void saveAndMerge() {
        Question q1 = new Question("title1", "contents1");
        Question q2 = questionRepository.save(q1);

        assertThat(q1).isSameAs(q2);

        Question q3 = new Question("title2", "contents2");
        // id 가 존재하면 merge를 수행
        q3.setId(100L);

        // 왜 persist에는 No default constructor for entity가 발생하지 않고 merge에서는 발생할까?
        // => merge를 수행하면 새로운 객체를 리턴해줘야하므로 빈 생성자가 필요하다
        // https://stackoverflow.com/questions/1069992/jpa-entitymanager-why-use-persist-over-merge
        // merge의 경우에 새로 객체를 생성해서 리턴하므로 같은 객체는 아니나 내용은 같다.
        Question q4 = questionRepository.save(q3);
        assertThat(q4.getId()).isNotNull();
        assertThat(q3).isNotSameAs(q4);
        assertThat(q4.getContents()).isEqualTo(q3.getContents());
        assertThat(q4.getTitle()).isEqualTo(q3.getTitle());
        assertThat(q4.getWriter()).isEqualTo(q3.getWriter());
    }

    @Test
    public void merge() {
        Question q1 = new Question("title1", "contents1");
        Question q2 = questionRepository.save(q1);

        assertThat(q1).isSameAs(q2);
        System.out.println("merge " + q1.getId());

        Question q3 = new Question("title2", "contents2");
        // id 가 존재하면 merge를 수행
        q3.setId(1L);

        // merge는 기존에 객체가 존재한다면 그 객체를 반환하고
        // 없다면 새로운객체를 반환한다.
        Question q4 = questionRepository.save(q3);
//        assertThat(q4.getId()).isEqualTo(4L);
        assertThat(q3).isNotSameAs(q4);
//        assertThat(q4).isSameAs(q1);
        //merge 는 현재 상태값을 업데이트한다.
        assertThat(q3.getContents()).isEqualTo(q4.getContents());
        assertThat(q3.getTitle()).isEqualTo(q4.getTitle());
        assertThat(q3.getWriter()).isEqualTo(q4.getWriter());
    }

    @Test
    public void test() {
        Question q1 = new Question("title1", "contents1");
        Question q2 = questionRepository.save(q1);
//        assertThat(q2.getId()).isEqualTo(5L);
//        System.out.println("test " + q1.getId());
        assertThat(q1).isSameAs(q2);
    }

    @Test
    @DisplayName("answers 조회 테스트")
    public void answer() {
        assertThat(q1.getAnswers()).hasSize(2).contains(a1,a2);
    }

    @Test
    @DisplayName("question 삭제시 deletehistory추가")
    public void delete() throws CannotDeleteException {
        q1.delete(u1);
        assertThat(u1.getDeleteHistories()).contains(new DeleteHistory(ContentType.QUESTION,q1.getId(),u1, LocalDateTime.now()));
        assertThat(u1.getQuestions()).contains(q1);
    }

    @Test
    @DisplayName("question 다른사람이 삭제시 에러")
    public void deleteByOther() {
        User u2 = new User("userid2","password","name","email2");
        userRepository.save(u2);
        assertThatThrownBy(()->q1.delete(u2))
                .isInstanceOf(CannotDeleteException.class);
    }
}