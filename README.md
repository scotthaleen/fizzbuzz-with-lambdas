# FizzBuzz with Lambdas

<img align="right" width="150"
src="http://raw.github.com/scotthaleen/fizzbuzz-with-lambdas/master/lambda.png" />

Functional implementation of
[FizzBuzz](http://en.wikipedia.org/wiki/Fizz_buzz) using lambdas

This started of with as a project to make use of Java 8's new lambda
expressions but I decided to implement the same (similar) functional
logic in
[Java](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/java8),
[Scala](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/scala),
and 
[Clojure](https://github.com/scotthaleen/fizzbuzz-with-lambdas/tree/master/clojure)
as comparisons.


The functional logic basically follows this

Define a base function that takes a predicate and a string
and returns a function that takes an Integer.  If the predicate is
met return the string, if not return None/Empty/nil

((Int) ⇒ Boolean, String) ⇒ (Int) ⇒ Optional[String]

Define higher order functions from the base function by partially
applying the predicate and string

(Int) ⇒ Optional[String]

Create an ordered sequence of the functions to apply to each number.
(Juxtaposition)

Define a combination function for joining Optional[String]s and
None. String + String = CombinedString, String + nil = String, nil +
nil = nil. 

Reduce the results of applying each function and joining the strings
together with the combination function, to produce an optional
String. If the result is None/nil return the input number as a
string. 

(Int) ⇒ String

Take the range from 1..N and map/apply the function and print the
results to the console. 


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

