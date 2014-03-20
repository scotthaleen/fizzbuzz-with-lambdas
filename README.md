# FizzBuzz with Lambdas

<img align="right" width="150"
src="http://raw.github.com/scotthaleen/fizzbuzz-with-lambdas/master/lambda.png" />

Functional implementation of
[FizzBuzz](http://en.wikipedia.org/wiki/Fizz_buzz) using lambdas

This started as a project to make use of Java 8's new lambda
expressions but I decided to implement the same (similar) functional
logic in
[Java](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/java8),
[Scala](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/scala),
and 
[Clojure](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/clojure)
as comparisons.


The functional logic basically follows this

<hr />

Define a base function that takes a predicate and a **String**
and returns a function that takes an **Integer**.  <br />If the predicate is
met return the **String**, if not return **None/Empty/nil**
```
((Int) ⇒ Boolean, String) ⇒ (Int) ⇒ Optional[String]
```
```java
static final Function<Predicate<Integer>, Function<String, Function<Integer, Optional<String>>>>
   isaFizzBuzz = p -> sz -> i -> {
      if (p.test(i)) return Optional.of(sz);
         return Optional.empty();
      };
```
```scala
  def isaFizzBuzz(p:(Int) => Boolean, sz:String) =
    (i:Int) =>
      if (p(i)) Some(sz)
      else None
```
```clojure
(defn isaFizzBuzz [pred sz]
  (fn [i] (cond (pred i) sz)))
```
Define higher order functions from the base function by partially
applying the predicate and string

```
(Int) ⇒ Optional[String]
```
```java
Function<Integer, Optional<String>>
   isFizz = isaFizzBuzz.apply(i -> (i % 3) == 0).apply("Fizz");
```
```scala
def isFizz = isaFizzBuzz((x) => x % 3 == 0, "Fizz")
```
```clojure
(def isFizz
  (isaFizzBuzz
   (fn [x] (= 0 (mod x 3)))
   "Fizz"))
```
Create an ordered sequence of the functions to apply to each number.
(juxtaposition)

```java
List<Function<Integer, Optional<String>>>
   conditions = Arrays.asList(isFizz, isBuzz);
```
```scala
val conditions = List(isFizz, isBuzz)
```
```clojure
(def conditions (juxt isFizz isBuzz))
```

Define a combination function for joining **Optional[String]s** and
**None**. 
* **String** + **String** = **Combined Strings**
* **String** + **nil** = **String**
* **nil** + **nil** = **nil**. 

```java
static final BinaryOperator<Optional<String>>
   combiner = (Optional<String> a, Optional<String> b) -> {
      if (a.isPresent() && b.isPresent()) return Optional.of(a.get() + b.get());
         if (a.isPresent()) return a;
            return b;
      };
```
```scala
  def combine(a:Option[String], b:Option[String]):Option[String] =
    (a,b) match {
      case (Some(s1), Some(s2)) => Some(s1 + s2)
      case (Some(_), None) => a
      case (None, Some(_)) => b
      case (_,_) => None
    }
```
clojure handles this a bit differently because **str** will combine **nil**
and a **String** and **nil** + **nil** results in **""** we can just
use the **empty?** on **""** to achieve similar results to the
**Optinal** **None** 
```clojure
(reduce str (conditions i))
```

Reduce the results of applying each function and joining the strings
together with the combination function, to produce an optional
**String**. If the result is **None/nil** return the input number as a
**String**. 
```
(Int) ⇒ String
```
```java
i -> conditions.stream().sequential()
   .reduce(
      emptyString,
      (s, fn) -> combiner.apply(s, fn.apply(i)),
      (xs, x) -> combiner.apply(xs, x)
   ).orElse(String.valueOf(i))
```
```scala
(i) =>
   conditions.foldLeft(emptyString) {
      (xs, fn) => combine(xs, fn(i))
  }.orElse(Some(i.toString)).get)
```
```clojure
(fn [i]
   (let [sz (reduce str (conditions i))]
       (if (empty? sz)
           (str i)
           sz)))
```

Take the range from **1..N** and **map/apply** the function and print the
results to the console. 

```java
IntStream.iterate(1, inc)
   .limit(limit)
   .mapToObj(i ->
      conditions.stream().sequential()
         .reduce(
            emptyString,
            (s, fn) -> combiner.apply(s, fn.apply(i)),
            (xs, x) -> combiner.apply(xs, x)
         ).orElse(String.valueOf(i))
   ).forEach(System.out::println);
```
```scala
  Stream.from(1)
    .take(limit)
    .map((i) =>
        conditions.foldLeft(emptyString) {
          (xs, fn) => combine(xs, fn(i))
        }.orElse(Some(i.toString)).get)
    .foreach(println)
```
```clojure
(doseq [x (map fizzbuzz
               (range 1 (inc limit)))]
  (println x))
```

```
1
2
Fizz
4
Buzz
Fizz
7
8
Fizz
Buzz
11
Fizz
13
14
FizzBuzz
16
17
Fizz
19
Buzz
```

