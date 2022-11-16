package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.ContentType.ANSWER;

@DataJpaTest
@Import(JpaAuditingConfiguration.class)
public class AnswersTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    User writer;
    Question question;
    Answers answers = new Answers();
    List<Answer> answerEntityList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        writer = userRepository.save(new User("userId", "password", "name", "email"));
        question = questionRepository.save(new Question("Hello", "Why??"));
        answerEntityList.add(answerRepository.save(new Answer(writer, question, "answer1")));
        answerEntityList.add(answerRepository.save(new Answer(writer, question, "answer2")));
        answerEntityList.add(answerRepository.save(new Answer(writer, question, "answer3")));
        answerEntityList.add(answerRepository.save(new Answer(writer, question, "answer4")));

        for (Answer answer : answerEntityList) {
            answers.addAnswer(answer);
        }
    }

    @Test
    void 답변_모두가_답변작성자인지_아닌지_확인한다() {
        User other = userRepository.save(new User("crawal", "password", "name", "esesmail"));
        assertThat(answers.allOwner(writer)).isTrue();
        assertThat(answers.allOwner(other)).isFalse();
        Answer otherAnswer = new Answer(other, question, "Other Answers");
        answers.addAnswer(otherAnswer);
        assertThat(answers.allOwner(writer)).isFalse();
    }

    @Test
    void 답변들을_모두_삭제한다() {
        List<DeleteHistory> deleteHistories = answers.delete();
        assertThat(deleteHistories).hasSize(4);
        assertThat(deleteHistories).isEqualTo(Arrays.asList(
            new DeleteHistory(ANSWER, answerEntityList.get(0).getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ANSWER, answerEntityList.get(1).getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ANSWER, answerEntityList.get(2).getId(), writer, LocalDateTime.now()),
            new DeleteHistory(ANSWER, answerEntityList.get(3).getId(), writer, LocalDateTime.now())
        ));
    }
}
