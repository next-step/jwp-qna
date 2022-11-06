package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import qna.config.EntityConfig;

@DataJpaTest
@Import(EntityConfig.class)
public abstract class BaseDomainTest<T extends BaseEntity> {

	void 도메인_생성_검증(List<T> 생성된_도메인, List<T> 비교_도메인) {
		assertThat(비교_도메인).containsAnyElementsOf(생성된_도메인);
	}

	List<LocalDateTime> 최종_수정_일자(List<T> 수정할_도메인) {
		return 수정할_도메인.stream()
			.map(BaseEntity::getUpdatedAt)
			.collect(Collectors.toList());
	}

	void 생성날짜_수정날짜_검증(List<T> entity) {
		entity.forEach(this::생성날짜_수정날짜_검증);
	}

	void 생성날짜_수정날짜_검증(T entity) {
		LocalDateTime createdAt = entity.getCreatedAt();
		LocalDateTime updatedAt = entity.getUpdatedAt();

		assertThat(createdAt).isNotNull();
		assertThat(updatedAt).isNotNull();
	}

	void 수정된_도메인_검증(List<T> 수정된_도메인, List<LocalDateTime> 최종_수정_일자) {
		boolean 수정_일자가_변경됨 = 수정된_도메인.stream()
			.map(BaseEntity::getUpdatedAt)
			.noneMatch(최종_수정_일자::contains);

		assertThat(수정_일자가_변경됨).isTrue();
	}

}
