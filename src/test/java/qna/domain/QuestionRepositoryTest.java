package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    void save() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final Question actual = questionRepository.save(QuestionTest.Q3.writeBy(writer));
        //final Question actual = questionRepository.save(QuestionTest.Q1.writeBy(writer));
        assertThat(actual.getId()).isEqualTo(QuestionTest.Q3.getId());
    }// db에 이제 Q1이 지닌 상태

    @Test
    @DisplayName("save 학습테스트. 같은 객체를 중복으로 저장하였을 때 결과 확인")
    void duplicateSave() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final Question question1 = questionRepository.save(QuestionTest.Q1.writeBy(writer)); //db 에는 없고 persist 로 영속성에만 저장
        final Question question2 = questionRepository.save(QuestionTest.Q1.writeBy(writer)); //db 에는 없고 persist 로 영속성에만 저장
        assertThat(question1).isEqualTo(question2); //같은 객체인지 확인
        assertThat(question1.getId()).isEqualTo(question2.getId());
        assertThat(question1.getId()).isEqualTo(QuestionTest.Q1.getId());
    }// test 하나 끝나면 commit 되어 db에 반영

    @Test
    @DisplayName("삭제되지 않은 질문들을 찾는다")
    void findByDeletedFalse() {
        final List<User> writers = userRepository.saveAll(Arrays.asList(UserTest.JAVAJIGI, UserTest.SANJIGI));
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(
                QuestionTest.Q1.writeBy(writers.get(0)), QuestionTest.Q2.writeBy(writers.get(1))));
        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(2);
        for (Question question : findQuestions) {
            assertThat(question.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 id 를 통해서 찾는다")
    void findByIdAndDeletedFalse() {
        final User writer = userRepository.save(UserTest.TESTUSER);
        final List<Question> questions = questionRepository.saveAll(Arrays.asList(QuestionTest.Q4.writeBy(writer)));
        //questions.get(0).setDeleted(true);
        questionRepository.delete(questions.get(0));

        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q4.getId());
        assertThat(findQuestion.isPresent()).isFalse();
    }
}