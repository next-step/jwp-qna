package learningtest;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

@Entity
class TestEntity implements Persistable<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String column;

	protected TestEntity() {
	}

	public TestEntity(Long id, String column) {
		this.id = id;
		this.column = column;
	}

	public TestEntity(String column) {
		this(null, column);
	}

	public TestEntity(Long id) {
		this(id, null);
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(id);
	}

	public String getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "TestEntity{" +
			"id=" + id +
			", column='" + column + '\'' +
			'}';
	}

	public void update(String modifiedColumn) {
		this.column = modifiedColumn;
	}
}
