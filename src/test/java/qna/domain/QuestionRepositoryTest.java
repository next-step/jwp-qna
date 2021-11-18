package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class QuestionRepositoryTest {

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @Test
    @DisplayName("데이터가 정상적으로 저장되는지 확인")
    void save() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        Question question = new Question("title", "contents1").writeBy(user);
        questionRepository.save(question);

        // when
        Question findQuestion = questionRepository.findById(question.getId()).get();

        // then
        assertThat(findQuestion.equalsTitleAndContentsAndNotDeleted(question)).isTrue();
    }

    @Test
    @DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
    void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue() {

        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        Question firstQuestion = new Question("title1", "contents1").writeBy(user);
        Question secondQuestion = new Question("title2", "contents2").writeBy(user);
        questionRepository.saveAll(asList(firstQuestion, secondQuestion));

        // when
        List<Question> questions = questionRepository.findAll();
        Question question = questions.get(0);
        question.setDeleted(true);

        // then
        List<Question> expectedQuestion = questionRepository.findByDeletedFalse();
        assertThat(expectedQuestion.size()).isEqualTo(1);
        assertThat(expectedQuestion.get(0)).isNotEqualTo(question);
    }

    @Test
    void 컨텐츠에서_답변_여러개_등록_확인() {

        // given
        User user = new User("seunghoona", "password", "username", "email");
        userRepository.save(user);

        Question question = new Question("title", "contents1").writeBy(user);
        question.addAnswer(new Answer(user, question, "content"));
        questionRepository.save(question);

        // when
        Question saveQuestion = questionRepository.save(question);

        // then
        assertThat(saveQuestion).isEqualTo(question);

    }

    @Test
    void 컨텐츠에서_답변_여러개_등록을_리스트로_확인() {

        // given
        User user = new User("seunghoona", "password", "username", "email");
        Question question = new Question("title", "contents1");
        question.addAnswers(asList(new Answer(user, question, "content1"), new Answer(user, question, "content2")));

        // when
        Question saveQuestion = questionRepository.save(question);

        // then
        assertThat(saveQuestion).isEqualTo(question);

    }

}
