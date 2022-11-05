package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    private List<Question> expectList;

    private User writer1;
    private User writer2;
    Question expected1;
    Question expected2;



    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
//        userRepository.deleteAll();
        writer1 = userRepository.save(UserTest.JAVAJIGI);
        writer2 = userRepository.save(UserTest.SANJIGI);
        Q1.writeBy(writer1);
        Q2.writeBy(writer2);

        expected1 = questionRepository.save(Q1);
        expected2 = questionRepository.save(Q2);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(expected1.getId()).isNotNull(),
                () -> assertThat(expected2.getWriter()).isNotNull()
        );
    }

    @Test
    void 조회() {
        expectList = questionRepository.findAll();
        assertAll(
            () -> assertThat(expectList).isNotNull(),
            () -> assertThat(expectList.size()).isEqualTo(2),
            () -> assertThat(expected1.getWriter()).isEqualTo(writer1),
            () -> assertThat(expected2.getWriter()).isEqualTo(writer2)
        );
    }

    @Test
    @DisplayName("질문1의 상태를 삭제로 바꾼 후 해당 질문 고유아이디이면서 삭제되지 않은 것을 조회")
    void 수정() {
        expected1.setDeleted(true);
        assertThat(questionRepository.findByIdAndDeletedFalse(expected1.getId()).orElse(null)).isNull();
    }

    @Test
    void 삭제() {
        questionRepository.deleteAll();

        assertThat(questionRepository.findAll().size()).isEqualTo(0);
    }
}
