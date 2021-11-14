package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Contents {
    @Lob
    private String contents;

    protected Contents() {
    }

    private Contents(String contents) {
        this.contents = contents;
    }

    public static Contents of(String contents) {
        checkValidation(contents);
        return new Contents(contents);
    }
    
    private static void checkValidation(String contents) {
        if (isEmpty(contents)) {
            throw new IllegalArgumentException("내용을 입력하지 않았습니다.");
        }
    }
    
    private static boolean isEmpty(String contents) {
        return contents.isEmpty();
    }
}
