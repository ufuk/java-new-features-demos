package io.github.ufuk.java08;

import io.github.ufuk.java08.examples.MyInterface;
import io.github.ufuk.java08.examples.MyInterfaceImpl;
import io.github.ufuk.java08.examples.MyOtherInterfaceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
    void basics_of_local_date_in_new_date_time_api() {
        LocalDate today = LocalDate.now();
        System.out.println("Today's date: " + today);

        LocalDate specificDate = LocalDate.of(2024, 12, 21);
        System.out.println("Specific date: " + specificDate);

        LocalDate nextWeek = today.plusWeeks(1);
        System.out.println("One week later: " + nextWeek);

        LocalDate lastMonth = today.minusMonths(1);
        System.out.println("One month before: " + lastMonth);

        if (lastMonth.isBefore(today)) {
            System.out.println(lastMonth + " is in the past.");
        }

        if (nextWeek.isAfter(today)) {
            System.out.println(nextWeek + " is in the future.");
        }

        DayOfWeek dayOfWeek = today.getDayOfWeek();
        System.out.println("Day of the week: " + dayOfWeek);

        Month month = today.getMonth();
        System.out.println("Month: " + month);

        int dayOfMonth = today.getDayOfMonth();
        System.out.println("Day of the month: " + dayOfMonth);

        if (specificDate.isLeapYear()) {
            System.out.println(specificDate.getYear() + " is a leap year.");
        }

        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        System.out.println("Start of the week: " + startOfWeek);

        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        System.out.println("End of the week: " + endOfWeek);

        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("Last day of the month: " + lastDayOfMonth);
    }

    @Test
    void basics_of_local_time_in_new_date_time_api() {
        LocalTime now = LocalTime.now();
        System.out.println("Current time: " + now);

        LocalTime specificTime = LocalTime.of(14, 30);
        System.out.println("Specific time: " + specificTime);

        LocalTime oneHourLater = now.plusHours(1);
        System.out.println("One hour later: " + oneHourLater);

        LocalTime tenMinutesBefore = now.minusMinutes(10);
        System.out.println("Ten minutes before: " + tenMinutesBefore);
    }

    @Test
    void basics_of_local_date_time_in_new_date_time_api() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current date and time: " + now);

        LocalDateTime specificDateTime1 = LocalDateTime.of(2023, 12, 21, 14, 30);
        System.out.println("Specific date and time #1: " + specificDateTime1);

        LocalDateTime specificDateTime2 = LocalDateTime.of(LocalDate.of(2023, 12, 21), LocalTime.of(14, 30));
        System.out.println("Specific date and time #2: " + specificDateTime2);

        if (specificDateTime1.isEqual(specificDateTime2)) {
            System.out.println("Yes, they are same!");
        }

        LocalDateTime tomorrow = now.plusDays(1);
        System.out.println("Tomorrow: " + tomorrow);

        LocalDateTime lastYear = now.minusYears(1);
        System.out.println("One year before: " + lastYear);

        LocalDateTime truncatedToHour = now.truncatedTo(ChronoUnit.HOURS);
        System.out.println("Minutes truncated: " + truncatedToHour);
    }

    @Test
    void basics_of_zoned_date_time_in_new_date_time_api() {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("Current date and time with zone: " + now);

        ZonedDateTime specificZone = ZonedDateTime.now(ZoneId.of("Europe/Istanbul"));
        System.out.println("Istanbul time: " + specificZone);
    }

    @Test
    void basics_of_period_in_new_date_time_api() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        Period period = Period.between(startDate, endDate);
        System.out.println("Years: " + period.getYears());
        System.out.println("Months: " + period.getMonths());
        System.out.println("Days: " + period.getDays());
    }

    @Test
    void basics_of_duration_in_new_date_time_api() {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 30);

        Duration duration = Duration.between(startTime, endTime);
        System.out.println("Hours: " + duration.toHours());
        System.out.println("Minutes: " + duration.toMinutes());
    }

    @Test
    void basics_of_date_time_formatter_in_new_date_time_api() {
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Default format: " + now);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = now.format(formatter);
        System.out.println("Formatted date and time: " + formattedDateTime);

        String isoDateTime = now.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("ISO formatted date and time: " + isoDateTime);

        LocalDateTime dateTime = LocalDateTime.parse("21-12-2024 17:47", formatter);
        System.out.println("Parsed date: " + dateTime);
    }

}
