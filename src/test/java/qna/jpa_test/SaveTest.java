package qna.jpa_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SaveTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final Question actual = questionRepository.save(QuestionTest.Q5.writeBy(writer));
        assertThat(actual.getId()).isEqualTo(QuestionTest.Q5.getId());

        //final Question actual = questionRepository.save(QuestionTest.Q6.writeBy(writer));
        //assertThat(actual.getId()).isNotEqualTo(QuestionTest.Q6.getId());
    }// db에 이제 Q6를 지닌 상태

    @Test
    @DisplayName("save 학습테스트. 같은 객체를 중복으로 저장하였을 때 결과 확인")
    void duplicateSave() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final Question question1 = questionRepository.save(QuestionTest.Q6.writeBy(writer)); //db 에는 없고 persist 로 영속성에만 저장
        final Question question2 = questionRepository.save(QuestionTest.Q6.writeBy(writer)); //db 에는 없고 persist 로 영속성에만 저장
        assertThat(question1).isEqualTo(question2); //같은 객체인지 확인
    }// test 하나 끝나면 commit 되어 db에 반영

}
