package qna.study.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import qna.study.domain.eager.Class;
import qna.study.domain.eager.Student;
import qna.study.repository.StudentRepository;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class StudentGenerator {

    private final StudentRepository studentRepository;

    public StudentGenerator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static int COUNTER = 0;
    public static final String NAME = "student name";

    public Student generateStudent(Class clazz) {
        COUNTER++;
        return new Student(NAME + COUNTER, clazz);
    }

    public Student savedStudent(Class savedClazz) {
        return studentRepository.saveAndFlush(generateStudent(savedClazz));
    }

    public List<Student> savedStudents(Class savedClazz, int count){
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(generateStudent(savedClazz));
        }
        return studentRepository.saveAll(students);
    }
}
