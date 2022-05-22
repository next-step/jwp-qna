package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfig;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @ParameterizedTest
    @MethodSource
    void id를_넘겨주어_deleted가_false인_answer를_찾는다(Answer answer, boolean expected) {
        // given
        Answer saved = answerRepository.save(answer);
        // when
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(saved.getId());
        // then
        assertThat(result.isPresent()).isEqualTo(expected);
    }

    static Stream<Arguments> id를_넘겨주어_deleted가_false인_answer를_찾는다() {
        A2.delete();

        return Stream.of(
                Arguments.of(
                        A1,
                        true
                ),
                Arguments.of(
                        A2,
                        false
                )
        );
    }

    @Test
    void 변경감지() {
        // given
        Answer saved = answerRepository.save(A1);
        saved.writeContents("update");
        // when
        Optional<Answer> result = answerRepository.findById(saved.getId());
        // then
        assertThat(result)
                .map(Answer::getContents)
                .hasValue("update");
    }

}