(ns lambdas.main
  (:gen-class))

(defn isaFizzBuzz [pred sz]
  "Take a predicate and a string,
   if the predicate is met returns the string
   else returns nil."
  (fn [i] (cond (pred i) sz)))

(def isFizz
  (isaFizzBuzz
   (fn [x] (= 0 (mod x 3)))
   "Fizz"))

(def isBuzz
  (isaFizzBuzz
   (fn [x] (= 0 (mod x 5)))
   "Buzz"))

;;juxtaposition of conditions
(def conditions (juxt isFizz isBuzz))

(defn -main [& args]
  (let [
        ;;arg parsing
        arg0 (first args)
        ;;convert to int or default 
        limit (if (nil? arg0) 30 (read-string arg0))
        ;; fizz buzz function
        fizzbuzz (fn [i]
                    (let [sz (apply str (conditions i))]
                      (if (empty? sz)
                        (str i)
                        sz)))]
    ;; run fizz buzzb "foreach" println the results
    (doseq [x (map fizzbuzz
                   (range 1 (inc limit)))]
      (println x))))
