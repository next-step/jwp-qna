package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.JAVAJIGI;

class NoAnswerRuleTest {
    @Test
    @DisplayName("답변이 없으면 삭제할 수 있음")
    void test1() {
        boolean deletable = new NoAnswerRule().deletable(JAVAJIGI, EMPTY_LIST);

        assertThat(deletable).isTrue();
    }
}