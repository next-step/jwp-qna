package qna.feedback.generator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.feedback.entity.eager.Class;
import qna.feedback.repository.ClassRepository;

@TestConstructor(autowireMode = AutowireMode.ALL)
public class ClassGenerator {

    private final ClassRepository classRepository;

    public ClassGenerator(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public static int COUNTER = 0;
    public static final String NAME = "class name";

    public Class generateClass() {
        COUNTER++;
        return new Class(NAME + COUNTER);
    }

    public Class savedClass() {
        return classRepository.saveAndFlush(generateClass());
    }

    public List<Class> savedClasses(int count) {
        List<Class> classes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            classes.add(generateClass());
        }
        return classRepository.saveAll(classes);
    }
}
