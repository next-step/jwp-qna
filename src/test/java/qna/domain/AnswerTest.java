package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AnswerTest {


    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager manager;

    private Question question;

    private User writer;

    private Answer answer;

    public  final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public  final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @BeforeEach
    void before(@Autowired QuestionRepository questionRepository,
                @Autowired UserRepository userRepository) {
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

    @Test
    @DisplayName("식별자로 답변을 조회한다.")
    void findById() {
        Answer expected = answerRepository.save(answer);
        Answer answer = answerRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestion()).isEqualTo(expected.getQuestion()),
                () -> assertThat(answer.getWriter()).isEqualTo(expected.getWriter()),
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
