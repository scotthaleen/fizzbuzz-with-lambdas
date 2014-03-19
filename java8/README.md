# Java 8 Lambda FizzBuzz

Functional implementation of
[FizzBuzz](http://en.wikipedia.org/wiki/Fizz_buzz) using JDK8 Lambdas


### To run 

```
$ gradle build
$ ./run.sh 42
```

Or

```
$ gradle runMain
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

### Break down

Basically we want a Range of numbers from 1..N and for each number
apply some function that will return the String to print or the number
itself if no conditions are met. 

The **isaFizzBuzz** function is a way to
[curry](http://en.wikipedia.org/wiki/Curry_(programming_language)) the
logic being applied to each number.  It allows you to create a partial
function with the Predicate and String to return if the predicate is
met. 

```java
Function<Predicate<Integer>, Function<String, Function<Integer, Optional<String>>>> isaFizzBuzz =
 p -> sz -> i -> {
    if (p.test(i)) return Optional.of(sz);
    return Optional.empty(); 
 };
```

With this function we can now create higher order functions defining
**isFizz** and **isBuzz** 

this is pseudo equivalent to this
```
isFizz = isaFizzBuzz(λ i -> (i % 3) == 0, "Fizz")
```


The next chunk of logic to tackle is applying each function to a
number.  This could be done with if/else or case/switch or possibly
[Optional.orElse](http://download.java.net/jdk8/docs/api/java/util/Optional.html#orElse-T-).
I preferred to create a List of the function
that I want them to be applied. By using an ordered List I can
just apply the functions and combine the results.  This is the reason
for implementing the `BinaryOperator<Optional<String>>` **combiner**.
This could alternatively be achieved if there was some kind of
juxtaposition function like in [clojure](http://clojuredocs.org/clojure_core/clojure.core/juxt)

This logic follows, apply the function on *i* combine the results with
the previous result.  If the predicate does not pass the function just
returns *empty*.  This means if all of the functions applied and none of
the predicates are matched I can make use of the
[Optional.orElse](http://download.java.net/jdk8/docs/api/java/util/Optional.html#orElse-T-)
to get the string value of the number and display it. And if multiple
conditions are met like with **15**, Fizz and Buzz will be combined to
print out **FizzBuzz**

That covers what is going on in this chunk of code
```java
  .mapToObj(i ->
     conditions.stream().sequential()
     .reduce(
        emptyString,
        (s, fn) -> combiner.apply(s, fn.apply(i)),
        (xs, x) -> combiner.apply(xs, x))
     .orElse(String.valueOf(i))
```
                                                                                
What I like about this solution is the composability of this solution.
It actually follows the
[open/closed principle](http://en.wikipedia.org/wiki/Open/closed_principle)
although obviously not OO.

If we want to extend this solution and say we want to add "Woof" for
every multiple of 7. It only requires 2 changes

The new partial function composed from the **isaFizzBuzz** function
```java
Function<Integer, Optional<String>>
   isWoof = isaFizzBuzz.apply(i -> (i % 7) == 0).apply("Woof");
```

And then add the new function to the end of the conditions list

```java
List<Function<Integer, Optional<String>>>
   conditions = Arrays.asList(isFizz, isBuzz, isWoof);
```

```
$ ./run.sh 25
1
2
Fizz
4
Buzz
Fizz
Woof
8
Fizz
Buzz
11
Fizz
13
Woof
FizzBuzz
16
17
Fizz
19
Buzz
FizzWoof
22
23
Fizz
Buzz
```

And we can very easily keep going 

```java
Function<Integer, Optional<String>>
   isMeow = isaFizzBuzz.apply(i -> (i % 9) == 0).apply("Meow");

List<Function<Integer, Optional<String>>>
   conditions = Arrays.asList(isFizz, isBuzz, isWoof, isMeow);
```

```
$ ./run.sh 25
1
2
Fizz
4
Buzz
Fizz
Woof
8
FizzMeow
Buzz
11
Fizz
13
Woof
FizzBuzz
16
17
FizzMeow
19
Buzz
FizzWoof
22
23
Fizz
```
