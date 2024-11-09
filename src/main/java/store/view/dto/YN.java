package store.view.dto;

import java.util.Arrays;

public enum YN {

    Y, N;

    public static YN of(String yn) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(yn))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
    }

    public boolean yes() {
        return this == Y;
    }

    public boolean no() {
        return this == N;
    }

}
