package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BaseTimeEntityTest {

	@Test
	public void 생성_테스트(){
		BaseTimeEntity baseTimeEntity = new BaseTimeEntity();

	}

}