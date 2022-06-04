package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    User testWriter;
    Question question;

    @BeforeEach
    void setup(){
        testWriter = userRepository.save(UserTest.ROCKPRO87);
        question = questionRepository.save(new Question("질문 제목1", "질문 내용1").writeBy(testWriter));
    }

    @Test
    @DisplayName("질문이 생성되고 연관관계가 맺어졌는지 검증")
    void saveWithRelation() {
        Question expected =new Question("질문 제목이에요", "질문 내용이에요.").writeBy(testWriter);
        Question actual = questionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriter()).isEqualTo(testWriter)
        );
    }

    @Test
    @DisplayName("질문의 연관관계 데이터가 정상적으로 조회되는지 검증")
    void findValidRelation() {
        Question actual = questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(actual.getWriter()).isEqualTo(testWriter)
        );
    }

    @Test
    @DisplayName("삭제되지 않은 모든 질문이 검색되는지 검증")
    void findByDeletedFalse() {
        Question anotherQuestion
                = questionRepository.save(new Question("질문 제목2", "질문 내용2").writeBy(testWriter));
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).contains(question, anotherQuestion);
    }

    @Test
    @DisplayName("특정 ID의 질문이 검색되는지 검증")
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).contains(question);
    }


}
