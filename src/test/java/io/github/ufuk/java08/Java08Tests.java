package io.github.ufuk.java08;

import io.github.ufuk.java08.examples.MyInterface;
import io.github.ufuk.java08.examples.MyInterfaceImpl;
import io.github.ufuk.java08.examples.MyOtherInterfaceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Further readings:
 * - https://www.baeldung.com/java-8-new-features
 * - https://www.baeldung.com/java-8-lambda-expressions-tips
 * - https://www.baeldung.com/java-8-streams-introduction
 */
class Java08Tests {

    @Test
    void interface_static_methods() {
        MyInterface.doSomethingStatically();
    }

    @Test
    void interface_default_method_when_not_implemented() {
        MyInterface anImplementation = new MyInterfaceImpl();
        anImplementation.doSomethingEvenIfNotImplemented();

        MyInterface anAnotherImplementation = new MyOtherInterfaceImpl();
        anAnotherImplementation.doSomethingEvenIfNotImplemented();
    }

    @Test
    void lambda_expression_is_just_syntactic_sugar_for_interfaces_with_one_method() {
        // alternative 1
        Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello Runnable");
            }
        };

        aRunnable.run();

        // alternative 2
        Runnable anAnotherRunnable = () -> System.out.println("Hello Lambda");

        anAnotherRunnable.run();
    }

    @Test
    void lambda_expressions_which_has_parameters_and_returns() {
        // Supplier
        Supplier<Long> aSupplier = () -> 1L;

        System.out.println(aSupplier.get());

        // Consumer
        Consumer<String> aConsumer = (aParameter) -> System.out.println(aParameter);

        aConsumer.accept("Hello Consumer");

        // Function
        Function<String, Long> aConverter = (aParameter) -> Long.parseLong(aParameter);

        Long convertedValue = aConverter.apply("13");
        System.out.println(convertedValue);
    }

    @Test
    void static_methods_as_lambda() {
        Runnable doSomethingStatically = MyInterface::doSomethingStatically;

        doSomethingStatically.run();
    }

    @Test
    void optional() {
        Optional<Long> anOptionalNumber = Optional.of(1L);

        // alternative 1
        if (anOptionalNumber.isPresent()) {
            System.out.println(anOptionalNumber.get());
        }

        // alternative 2
        anOptionalNumber.ifPresent(aNumber -> System.out.println(aNumber));

        // alternative 3 - if a default value is works for you, no need to check present or not
        Long actualValueOrDefault = anOptionalNumber.orElse(13L);
        System.out.println(actualValueOrDefault);

        // alternative 4 - if null is works for you too
        Long actualValueOrNull = anOptionalNumber.orElse(null);
        System.out.println(actualValueOrNull);

        // before
        Long aNumber = 1L;
        if (aNumber != null) {
            System.out.println(aNumber);
        }
    }

    @Test
    void optional_map() {
        Optional<String> anOptionalString = Optional.of("1923");
        Optional<Long> anOptionalLong = anOptionalString.map(aString -> Long.parseLong(aString));
        System.out.println(anOptionalLong.get());

        // before
        String aString = "1923";
        Long aLong = null;
        if (aString != null) {
            aLong = Long.parseLong(aString);
        }
        System.out.println(aLong);
    }

    @Test
    void optional_filter_then_map() {
        Optional<String> anOptionalString = Optional.of("not numeric text");
        Optional<Long> anOptionalLong = anOptionalString.filter(aString -> StringUtils.isNumeric(aString)).map(aString -> Long.parseLong(aString));
        System.out.println(anOptionalLong.orElse(null));

        // before
        String aString = "not numeric text";
        Long aLong = null;
        if (aString != null && StringUtils.isNumeric(aString)) {
            aLong = Long.parseLong(aString);
        }
        System.out.println(aLong);
    }

    @Test
    void optional_flat_map() {
        Optional<String> anOptionalString = Optional.of("a text");
        Optional<Long> anOptionalLong = anOptionalString.flatMap(aString -> StringUtils.isNumeric(aString) ? Optional.ofNullable(Long.parseLong(aString)) : Optional.empty());
        assertThat(anOptionalLong).isEmpty();
    }

    @Test
    void optional_or_else_throw() {
        Optional<String> anOptionalString = Optional.empty();

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> {
                    anOptionalString.orElseThrow(() -> new RuntimeException("No value presents, then throw...")); // throws exception
                }
        );

        assertThat(e).hasMessage("No value presents, then throw...");
    }

    @Test
    void use_stream_instead_of_loops() {
        // alternative 1 - create from an array
        Stream.of("1", "2", "3", "text")
                .filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .forEach(System.out::println);

        // alternative 2 - create from a Collection
        List<String> aStringList = Arrays.asList("1", "2", "3", "text");
        aStringList.stream()
                .filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .forEach(System.out::println);

        // if you need count, then count
        long count1 = aStringList.stream()
                .filter(StringUtils::isNumeric)
                .count();
        System.out.println(count1);

        // collect items into a collection instead of printing each
        List<Long> collectedItems = aStringList.stream()
                .filter(StringUtils::isNumeric)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        collectedItems.forEach(System.out::println);

        // count in parallel
        long count2 = aStringList.parallelStream()
                .filter(StringUtils::isNumeric)
                .count();
        System.out.println(count2);

        // ...
    }

    @Test
    void new_utilities_for_null_checks() {
        Long aLong = null;

        if (Objects.isNull(aLong)) {
            System.out.println("Yes, it's null");
        }

        Long anAnotherLong = 13L;
        if (Objects.nonNull(anAnotherLong)) {
            System.out.println("Yes, it's not null");
        }

        // you can use them as predicates
        List<String> aList = Arrays.asList("1", "2", null, "3");

        aList.stream()
                .filter(Objects::nonNull)
                .forEach(System.out::println);

        long nullItemCount = aList.stream()
                .filter(Objects::isNull)
                .count();
        System.out.println("Null item count: " + nullItemCount);
    }

    @Test
    void new_date_time_api() {
        Assertions.fail("No example presents"); // TODO: add example(s)
    }

}
