package qna.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.eager.Class;
import qna.feedback.entity.eager.Student;
import qna.feedback.generator.ClassGenerator;
import qna.feedback.generator.StudentGenerator;
import qna.feedback.repository.ClassRepository;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({ClassGenerator.class, StudentGenerator.class})
@DisplayName("코드 리뷰 피드백 : Fetch Join VS @EntityGraph 동작 비교하기(inner join vs left join)")
public class Ex04_Fetch_Join_And_Entity_Graph_Test {

    private final EntityManager entityManager;
    private final ClassRepository classRepository;
    private final ClassGenerator classGenerator;
    private final StudentGenerator studentGenerator;

    public Ex04_Fetch_Join_And_Entity_Graph_Test(
        EntityManager entityManager,
        ClassRepository classRepository,
        ClassGenerator classGenerator,
        StudentGenerator studentGenerator
    ) {
        this.entityManager = entityManager;
        this.classRepository = classRepository;
        this.classGenerator = classGenerator;
        this.studentGenerator = studentGenerator;
    }

    @Test
    @DisplayName("JPQL-Fetch Join : Inner Join 동작 검증")
    public void jpqlFetchJoinIsInnerJoinTest2() {
        // Given
        final Class firstClass = classGenerator.savedClass();
        final Class secondClass = classGenerator.savedClass();
        final Class thirdClass = classGenerator.savedClass();
        List<Student> firstClassStudents = studentGenerator.savedStudents(firstClass, 3);
        entityManager.clear();

        // When
        List<Class> actual = classRepository.findAllWithJpqlFetchJoin();
        assertAll(
            () -> assertThat(actual.size())
                .as("Inner Join으로 조건에 부합하는 `firstClass`만 조회")
                .as("카타시안 곱 연산으로 인해 `Class`와 연관관계를 가지는 `Students` 객체의 수만큼 `firstClass` 객체의 중복이 발생")
                .isEqualTo(firstClassStudents.size()),
            () -> assertThat(actual).containsOnly(firstClass)
        );
    }

    @Test
    @DisplayName("Spring Data JPA-@EntityGraph : Left Outer Join 동작 검증")
    public void springDataJpaEntityGraphIsLeftOuterJoinTest() {
        // Given
        final Class firstClass = classGenerator.savedClass();
        final Class secondClass = classGenerator.savedClass();
        final Class thirdClass = classGenerator.savedClass();
        studentGenerator.savedStudents(firstClass, 1);
        entityManager.clear();

        // When
        List<Class> actual = classRepository.findAllByUpdatedAtIsNull();

        // Then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(3),
            () -> assertThat(actual)
                .as("Outer Join으로 조건에 부합하는 `firstClass`와 left table의 `secondClass`, `thirdClass`가 같이 조회")
                .containsOnlyOnce(firstClass, secondClass, thirdClass)
        );
    }

    @Test
    @DisplayName("JPQL-Fetch Join : `distinct`를 이용하여 카테시안 곱 연산으로 인해 발생하는 중복 문제 해결")
    public void jpqlFetchJoinIsDuplicatedWithCartesian() {
        // Given
        final Class firstClass = classGenerator.savedClass();
        final Class secondClass = classGenerator.savedClass();
        final Class thirdClass = classGenerator.savedClass();
        List<Student> firstClassStudents = studentGenerator.savedStudents(firstClass, 1);
        List<Student> secondClassStudents = studentGenerator.savedStudents(secondClass, 2);
        entityManager.clear();

        // When
        List<Class> duplicatedResultByCartesian = classRepository.findAllWithJpqlFetchJoin();
        List<Class> actual = classRepository.findAllWithJpqlDistinctFetchJoin();
        assertAll(
            () -> assertThat(duplicatedResultByCartesian.size())
                .as("카테시안 곱 연산으로 인해 연관관계를 가지는 객체가 조회된 수만큼 중복이 발생")
                .isEqualTo(firstClassStudents.size() + secondClassStudents.size()),
            () -> assertThat(actual.size())
                .as("distinct를 이용한 중복 제거")
                .isEqualTo(2),
            () -> assertThat(actual)
                .as("inner join이므로 조건에 부합하지 않는 `thirdClass`는 조회되지 않음")
                .containsOnlyOnce(firstClass, secondClass)
        );
    }
}
