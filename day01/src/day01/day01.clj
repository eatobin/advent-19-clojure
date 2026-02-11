(ns day01.day01
  (:require
   [malli.core :as m]))

; clojure -M:repl/rebel

(def modules-schema
  [:fn '(fn [coll] (and (list? coll) (= (count coll) 100) (every? integer? coll)))])

;; use a list - will iterate over all...
(def modules '(68958 82218 54333 59177 51874 100259 95468 124006 75078 113631 90315 147580 68233 81025 133084 90959 81196 92443 124832 65871 57704 140203 113053 76337 72195 115917 87843 131768 105816 131153 59714 94107 50933 139545 94969 149463 60042 66028 111190 63257 50020 88783 81428 73977 149240 137152 74738 55067 128829 56465 81962 67242 94121 92303 68477 88595 64324 82527 134717 140344 132026 137558 95643 79010 146346 86246 52341 147564 89159 66456 83190 128675 130658 122857 134538 122151 133900 117462 117791 139254 86366 66165 92897 121218 135962 143061 129220 114623 98257 76722 121014 77713 137858 133282 103595 118981 149137 101141 70765 141113))
(m/validate modules-schema modules)

;part a
(def gas
  (m/-instrument
   {:schema [:=> [:cat :int] :int]}
   (fn [module] (- (quot module 3) 2))))
#_(gas 77.7)

(defn answer-a []
  (->> modules
       (map gas)
       (reduce +)))

;part b
(def gas-plus
  (m/-instrument
   {:schema [:=> [:cat :int] :int]}
   (fn [module] (loop [m   module
                       acc 0]
                  (let [new-gas (gas m)]
                    (if (pos-int? new-gas)
                      (recur
                       new-gas
                       (+ acc new-gas))
                      acc))))))
#_(gas-plus 77.7)

(defn gas-plus-lazy [module]
  (->> module
       (iterate gas)
       (take-while pos?)
       (rest)
       (reduce +)))

(defn answer-b []
  (->> modules
       (map gas-plus)
       (reduce +)))

(defn answer-c []
  (->> modules
       (map gas-plus-lazy)
       (reduce +)))

(defn print-a
  "Invoke me with clojure -X day01.day01/print-a"
  [_]
  (println)
  (printf "Part A answer: %s, correct: 3337766%n" (answer-a))
  (flush))

(defn print-b
  "Invoke me with clojure -X day01.day01/print-b"
  [_]
  (println)
  (printf "Part B answer (strict): %s, correct: 5003788%n" (answer-b))
  (printf "Part B answer (lazy): %s, correct: 5003788%n" (answer-c))
  (flush))

(comment
  (print-a nil)
  (print-b nil))

(defn -main
  "Invoke me with clojure -M -m day01.day01"
  [& _]
  (println)
  (printf "Part A answer: %s, correct: 3337766%n" (answer-a))
  (flush)
  (printf "Part B answer (strict): %s, correct: 5003788%n" (answer-b))
  (flush)
  (printf "Part B answer (lazy): %s, correct: 5003788%n" (answer-c))
  (flush))

(comment
  (-main)
  *ns*)
