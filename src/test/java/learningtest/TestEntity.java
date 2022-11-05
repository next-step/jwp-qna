package learningtest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
class TestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String column;

	protected TestEntity() {
	}

	public TestEntity(String column) {
		this.column = column;
	}

	public Long getId() {
		return id;
	}

	public String getColumn() {
		return column;
	}
}
