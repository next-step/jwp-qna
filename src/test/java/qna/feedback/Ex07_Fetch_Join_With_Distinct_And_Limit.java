package qna.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.domain.Question;
import qna.domain.User;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({QuestionGenerator.class, AnswerGenerator.class, UserGenerator.class})
@DisplayName("코드 리뷰 피드백 : fetch join 사용 시, `distinct`와 `limit` 적용 여부에 따른 동작 방식과 주의 사항 살펴보기")
public class Ex07_Fetch_Join_With_Distinct_And_Limit {

    private final QuestionGenerator questionGenerator;
    private final AnswerGenerator answerGenerator;
    private final UserGenerator userGenerator;
    private final EntityManager entityManager;

    public Ex07_Fetch_Join_With_Distinct_And_Limit(
        QuestionGenerator questionGenerator,
        AnswerGenerator answerGenerator,
        UserGenerator userGenerator,
        EntityManager entityManager
    ) {
        this.questionGenerator = questionGenerator;
        this.answerGenerator = answerGenerator;
        this.userGenerator = userGenerator;
        this.entityManager = entityManager;
    }

    /**
     * fetch join 페이징 처리에 대한 실습을 위한 dummy data 생성
     * - 10개의 질문과, 각 질문에 15개의 답변 생성
     */
    @BeforeEach
    void setUp() {
        User questionWriter = userGenerator.savedUser();
        for (int i = 0; i < 10; i++) {
            Question question = questionGenerator.savedQuestion(questionWriter);
            for (int j = 0; j < 15; j++) {
                answerGenerator.savedAnswer(questionWriter, question);
            }
        }
        entityManager.clear();
    }

    @Test
    @DisplayName("직접 표현이 불가능한 MySQL의 limit")
    public void throwException_CauseSpringDataJpaDialectIsNotAllowed() {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> entityManager.createQuery("select t from Team as t join fetch t.members as m limit 3"))
            .withMessageContaining("unexpected token: limit")
            .as("JPA에서는 `dialect`를 이용하여 연동된 DB에 적절한 쿼리를 생성하므로, 특정 DB에 종속되는 `limit`와 같은 문법을 직접 사용할 수 없음");
    }

    @Test
    @DisplayName("`Fetch Join`의 카테이션 곱 연산으로 인한 데이터 중복 문제")
    public void duplicatedData_jpqlFetchJoin_CauseByCartesian() {
        // When
        List<Question> actual = entityManager.createQuery("select q from Question as q join fetch q.answers as a", Question.class)
            .getResultList();

        // Then
        assertThat(actual)
            .as("1:N 연관관계의 fetch join 수행 시, 카테이션 곱 연산으로 인해 각 질문은 답변 수 만큼의 데이터 중복 발생")
            .hasSize(10 * 15)
            .allSatisfy(question -> {
                assertThat(Hibernate.isInitialized(question.getAnswers()))
                    .as("`fetch join`으로 인해 프록시 할당이 아닌 연관 객체의 로딩 여부").isTrue();
                assertThat(question.getAnswers()).hasSize(15);
                });
    }

    @Test
    @DisplayName("`Distinct`를 이용한 `Fetch Join`의 데이터 중복 해결")
    public void solveDataDuplicationProblem_ByDistinct() {
        // When
        List<Question> actual = entityManager.createQuery("select distinct q from Question as q join fetch q.answers as a", Question.class)
            .getResultList();

        // Then
        assertThat(actual)
            .as("`distinct`를 이용하여 일다대 관계의 `fetch join` 수행 시, DB 전송 쿼리에 `distinct`를 적용")
            .as("조회된 결과에 대해 Application 에서 id가 동일한 Entity의 중복을 추가로 제거하여 데이터 중복 문제를 해결")
            .hasSize(10)
            .allSatisfy(question -> assertThat(question.getAnswers()).hasSize(15));
    }

    @Test
    @DisplayName("#1 코드 리뷰 피드백 테스트 : 질문 5개와 각 질문의 모든 답변을 `fetch join + limit`으로 조회 하는 경우")
    public void fetchJoinPaginationTest(){
        // Given
        final int offset = 5;
        final int limit = 5;

        // When
        List<Question> actual = entityManager.createQuery("select q from Question as q join fetch q.answers as a", Question.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();

        /**
         * 원인 :
         * 1:N 연관관계의 경우 카테이션 곱 연산으로 인해 연관된 객체가 조회된 수 만큼의 중복이 발생하므로 쿼리 실행 결과로는 페이징 처리 할 수없음
         * 따라서 JPA는 limite와 offset이 적용되지 않은 풀 스캔 쿼리를 실행하고 그 결과를 Application 메모리에 적재
         * JAP는 메모리 상에서 중복을 제거하고, 그 결과 값을 이용하여 페이징 처리를 진행하며, 그 과정에서 경고 문구를 출력
         * `firstResult/maxResults specified with collection fetch; applying in memory!`
         * 이를 인지하지 못하고 사용할 경우, 많은 양의 데이터를 메모리에 로드하는 과정에서 OOME 발생에 주의
         *
         * 해결 :
         * *One으로 매핑된 연관관계를 fetch join으로 조회하여 불필요한 쿼리 발생을 줄이고,
         * *Many로 매핑된 연관관계는 LazyLoading과 batch size 옵션을 이용해여 In절을 이용하여 프록시 객체를 초기화
         */
        assertThat(actual)
            .as("From 절의 Question에 limit 적용 확인")
            .hasSize(limit)
            .allSatisfy(question -> {
                assertThat(Hibernate.isInitialized(question.getAnswers())).isTrue();
                assertThat(question.getAnswers()).hasSize(15);
            });
    }

    @Test
    @DisplayName("#2 코드 리뷰 피드백 테스트 : 질문 5개와 각 질문의 모든 답변을 `fetch join + limit + distinct`으로 조회 하는 경우")
    public void fetchJoinPaginationWithDistinctTest(){
        // Given
        final int offset = 5;
        final int limit = 5;

        // When
        List<Question> actual = entityManager.createQuery("select distinct q from Question as q join fetch q.answers as a", Question.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();

        // Then
        assertThat(actual)
            .as("`fetch join + limit + distinct`쿼리 실행 시, distinct 유무에 대한 차이점을 발견하지 못함")
            .hasSize(5)
            .allSatisfy(question -> {
                assertThat(Hibernate.isInitialized(question.getAnswers())).isTrue();
                assertThat(question.getAnswers())
                    .hasSize(15);
            });
    }
}
