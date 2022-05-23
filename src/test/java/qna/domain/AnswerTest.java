package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    private Long javajigi_id;
    private Long sanjigi_id;

    @BeforeEach
    void setUp() {
        javajigi_id = UserTest.JAVAJIGI.getId();
        UserTest.JAVAJIGI.setId(null);
        User save = userRepository.save(UserTest.JAVAJIGI);
        sanjigi_id = UserTest.SANJIGI.getId();
        UserTest.SANJIGI.setId(null);
        User save1 = userRepository.save(UserTest.SANJIGI);
    }

    @AfterEach
    void end() {
        UserTest.JAVAJIGI.setId(javajigi_id);
        UserTest.SANJIGI.setId(sanjigi_id);
    }

    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answerRepository.save(A1);
        assertAll(
                () -> assertThat(expected.getCreatedAt()).isNotNull(),
                () -> assertThat(expected.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("저장값 비교하기")
    @Test
    void identityTest() {
        Answer expected = answerRepository.save(A1);
        Answer answer = answerRepository.findById(expected.getId()).get();
        assertThat(expected).isSameAs(answer);
    }


    @DisplayName("변경하기 ")
    @Test
    void updateTest(){
        Answer savedAnswer = answerRepository.save(A1);
        savedAnswer.setContents("Answers change");
        Optional<Answer> isSavedAnswer = answerRepository.findById(savedAnswer.getId());
        assertThat(isSavedAnswer.isPresent()).isTrue();
        assertThat(isSavedAnswer.get().getContents()).isEqualTo(savedAnswer.getContents());
    }

    @DisplayName("답변과 사용자 간의 다대일 단방향 테스트")
    @Test
    void manyToOneTest() {
        Answer savedAnswer = answerRepository.save(A1);
        assertThat(savedAnswer.getWriter()).isSameAs(UserTest.JAVAJIGI);
    }

}
