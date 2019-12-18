(ns advent.day3
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def both (with-open [reader (io/reader "paths-test-2.csv")]
            (doall
              (csv/read-csv reader))))
(def red (->> (first both)
              (into [])))

(def blue (->> (second both)
               (into [])))


(defn direction [unit]
  (subs unit 0 1))

(defn distance [unit]
  (inc (Integer/parseInt (subs unit 1))))

(defn make-path [unit [a b]]
  (let [direction (direction unit)
        distance (distance unit)]
    (into [] (case direction
               "R" (for [x (range a (+ distance a))]
                     [x b])
               "U" (for [y (range b (+ distance b))]
                     [a y])
               "L" (for [x (range a (+ (- distance) a) -1)]
                     [x b])
               "D" (for [y (range b (+ (- distance) b) -1)]
                     [a y])))))

(defn make-paths [units start]
  (loop [units units
         start start
         path [start]]
    (if (empty? units)
      path
      (recur
        (rest units)
        (last (make-path (first units) start))
        (into [] (concat path (rest (make-path (first units) start))))))))

(def rs (into #{} (make-paths red [0 0])))
(def bs (into #{} (make-paths blue [0 0])))
(def urb (clojure.set/intersection bs rs))

(defn abs-dist [[x y]]
  (+ (Math/abs x) (Math/abs y)))
(def abs-vals (map abs-dist (disj urb [0 0])))
(def answer (apply min abs-vals))
