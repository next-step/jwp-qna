package qna.domain;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("save merge비교")
    public void saveAndMerge() {
        Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question q2 = questionRepository.save(q1);

        assertThat(q1).isSameAs(q2);

        Question q3 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
        // id 가 존재하면 merge를 수행
        q3.setId(100L);

        // 왜 persist에는 No default constructor for entity가 발생하지 않고 merge에서는 발생할까?
        // => merge를 수행하면 새로운 객체를 리턴해줘야하므로 빈 생성자가 필요하다
        // https://stackoverflow.com/questions/1069992/jpa-entitymanager-why-use-persist-over-merge
        // merge의 경우에 새로 객체를 생성해서 리턴하므로 같은 객체는 아니나 내용은 같다.
        Question q4 = questionRepository.save(q3);
        assertThat(q4.getId()).isEqualTo(2L);
        assertThat(q3).isNotSameAs(q4);
        assertThat(q4.getContents()).isEqualTo(q3.getContents());
        assertThat(q4.getTitle()).isEqualTo(q3.getTitle());
        assertThat(q4.getWriterId()).isEqualTo(q3.getWriterId());
    }

    @Test
    public void merge() {
        Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question q2 = questionRepository.save(q1);

        assertThat(q1).isSameAs(q2);
        System.out.println("merge " + q1.getId());

        Question q3 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
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
        assertThat(q3.getWriterId()).isEqualTo(q4.getWriterId());
    }

    @Test
    public void test() {
        Question q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question q2 = questionRepository.save(q1);
//        assertThat(q2.getId()).isEqualTo(5L);
//        System.out.println("test " + q1.getId());
        assertThat(q1).isSameAs(q2);
    }


}