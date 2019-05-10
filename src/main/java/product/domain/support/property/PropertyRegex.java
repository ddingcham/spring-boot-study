package product.domain.support.property;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PropertyRegex {

    SQUARE_BRACKET("\\[(.+?)\\]"),
    PARENTHESES("\\((.+?)\\)"),
    CARET("\\^(.+?)\\b");

    private static Pattern[] patterns;
    private final String regex;

    public static Pattern[] patterns() {
        if (patterns == null) {
            initPatterns();
        }
        return patterns;
    }

    private static void initPatterns() {
        PropertyRegex[] values = PropertyRegex.values();
        int length = values.length;
        patterns = new Pattern[length];
        for (int i = 0; i < length; i++) {
            patterns[i] = Pattern.compile(values[i].regex);
        }
    }
}
