package qna;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestStringGenerate {
    public static String generateByLength(int length){
        return IntStream.range(0, length).mapToObj(i -> "a").collect(Collectors.joining(""));
    }

    private TestStringGenerate(){
    }
}
