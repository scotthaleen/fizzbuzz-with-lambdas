package lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

class Main {

    static final Optional<String> emptyString = Optional.empty();
    static final IntUnaryOperator inc = i -> i + 1;

    //because java does not allow Optional<String> + Optional<String>
    //we have to define it to basically mirror String + String
    static final BinaryOperator<Optional<String>>
            combiner = (Optional<String> a, Optional<String> b) -> {
                if (a.isPresent() && b.isPresent()) return Optional.of(a.get() + b.get());
                if (a.isPresent()) return a;
                return b;
            };

    //curry the function (Predicate)(DefaultString)(Integer) -> Optional<String>
    static final Function<Predicate<Integer>, Function<String, Function<Integer, Optional<String>>>>
            isaFizzBuzz = p -> sz -> i -> {
                if (p.test(i)) return Optional.of(sz);
                return Optional.empty();
            };

    public static void main(String[] args) {

        //parse command line args or default to 30 for integer range;
        final Integer limit = Integer.valueOf(
                Arrays.asList(args).stream().findFirst().orElse("30")
        );

        //partial apply the fizzbuzzes
        Function<Integer, Optional<String>>
                isFizz = isaFizzBuzz.apply(i -> (i % 3) == 0).apply("Fizz");

        Function<Integer, Optional<String>>
                isBuzz = isaFizzBuzz.apply(i -> (i % 5) == 0).apply("Buzz");

        //Sequential Condition Tests
        List<Function<Integer, Optional<String>>>
                conditions = Arrays.asList(isFizz, isBuzz);

        //Go
        IntStream.iterate(1, inc)
                .limit(limit)
                .mapToObj(i ->
                        conditions.stream().sequential()
                                .reduce(
                                        emptyString,
                                        (s, fn) -> combiner.apply(s, fn.apply(i)),
                                        (xs, x) -> combiner.apply(xs, x)
                                )
                                .orElse(String.valueOf(i))
                      ).forEach(System.out::println);
    }
}
