package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 저장 성공")
    void save() {
        // when
        final Question question = questionRepository.save(new Question("제목", "내용"));

        // then
        assertThat(question).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("saveExceptionArgs")
    @DisplayName("Question 저장시 제약조건 으로 인한 저장 실패 케이스")
    void save_exception(Question expected) {
        // when & then
        assertThatThrownBy(() -> questionRepository.save(expected))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    static Stream<Arguments> saveExceptionArgs() {
        return Stream.of(
                Arguments.of(new Question("htvxfmpqhocuxcbnzcjvsuaiugucnfmmstrnaqvqbcipkvnrmfwzpyemyfgsxqqqsiinbmivuvrqpzvnwdilfnhnohuwcrewigxyzzhtjbrdcywdvsnuvmjspwycmogvdmbkfuamczyoncupyixmivoyefvpwwvgfzmdhvmrdydkvyeuttwqqkpkylniakpxuxnajfzuxawqhspwwpijcskfoynjabpcrjntdgigduasptxssjppojqdnuhtvxfmpqhocuxcbnzcjvsuaiugucnfmmstrnaqvqbcipkvnrmfwzpyemyfgsxqqqsiinbmivuvrqpzvnwdilfnhnohuwcrewigxyzzhtjbrdcywdvsnuvmjspwycmogvdmbkfuamczyoncupyixmivoyefvpwwvgfzmdhvmrdydkvyeuttwqqkpkylniakpxuxnajfzuxawqhspwwpijcskfoynjabpcrjntdgigduasptxssjppojqdnu", "테스트"))
        );
    }

    @Test
    @DisplayName("deleted = false 인 question 리스트 조회")
    void findByDeletedFalse() {
        // given
        final Question question = questionRepository.save(new Question("제목", "내용"));

        // when
        final List<Question> questionList = questionRepository.findByDeletedFalse();
        questionRepository.flush();

        // then
        assertThat(questionList).hasSize(1);
        assertThat(questionList).contains(question);
    }

    @Test
    @DisplayName("id 와 deleted = false 인 question 조회")
    void findByIdAndDeletedFalse() {
        // given
        final Question question = questionRepository.save(new Question("제목", "내용"));
        questionRepository.flush();

        // when & then
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotEmpty();
    }

    @Test
    @DisplayName("writer_id 변경")
    void update() {
        // given
        final Question question = questionRepository.save(new Question("제목", "내용"));

        // when
        question.writeBy(new User(1L, "userId", "1234", "name", "email@email.com"));
        questionRepository.flush();

        // then
        assertThat(question.getWriterId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Question 삭제")
    void delete() {
        // given
        final Question question = questionRepository.save(new Question("제목", "내용"));

        // when
        questionRepository.delete(question);
        questionRepository.flush();

        // then
        assertThat(questionRepository.findById(question.getId())).isEmpty();
    }
}