package qna.study.domain.eager;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class_id")
	private Class clazz;

	public Student() {
	}

	public Student(String name, Class clazz) {
		this.name = name;
		this.clazz = clazz;
		addClass();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void addClass() {
		this.clazz.getStudents().add(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Student student = (Student) o;
		return Objects.equals(id, student.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
