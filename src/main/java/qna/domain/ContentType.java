package qna.domain;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ContentType {
    QUESTION, ANSWER;


    public static String valuesString(){
        return Arrays.stream(ContentType.values())
            .map(Enum::name)
            .collect(joining(","));

    }
}
