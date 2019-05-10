package product.domain.support;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import product.domain.support.property.PropertyRegex;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ProductProperties {

    @NonNull
    private final Set<String> properties;

    public static PropertiesBuilder builder(ProductOrigin origin) {
        return new PropertiesBuilder(origin);
    }

    public boolean contains(String property) {
        return properties.contains(property);
    }

    public static class PropertiesBuilder {

        @NonNull
        private final ProductOrigin origin;

        private final Set<Pattern> regexps;

        private PropertiesBuilder(ProductOrigin origin) {
            this.origin = origin;
            regexps = Stream
                    .of(PropertyRegex.patterns())
                    .collect(Collectors.toSet());
        }

        public PropertiesBuilder with(String regex) {
            if (regex != null) {
                regexps.add(Pattern.compile(regex));
            }
            return this;
        }

        public ProductProperties build() {
            Set<String> tempProperties = new HashSet<>();
            String source = origin.getOrigin();
            for (Pattern regex : regexps) {
                Matcher matcher = regex.matcher(source);
                while (matcher.find()) {
                    tempProperties.add(matcher.group(1));
                }
            }
            return new ProductProperties(tempProperties);
        }
    }
}
