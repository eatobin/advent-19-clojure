(ns advent.day3
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
;(def both (with-open [reader (io/reader "paths-test-1.csv")]
;            (doall
;              (csv/read-csv reader))))

(def both (with-open [reader (io/reader "paths.csv")]
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

(defn abs-dist [[x y]]
  (+ (Math/abs x) (Math/abs y)))

(def answer
  (let [red-set (into #{} (make-paths red [0 0]))
        blue-set (into #{} (make-paths blue [0 0]))
        red-blue-intersect (clojure.set/intersection red-set blue-set)]
    (apply min (map abs-dist (disj red-blue-intersect [0 0])))))

;2193

;part b
(defn count-make-path [unit [a b] step]
  (let [direction (direction unit)
        distance (distance unit)]
    (into [] (case direction
               "R" (for [x (range a (+ distance a))
                         :let [ns (inc step)]]
                     [x b ns])
               "U" (for [y (range b (+ distance b))
                         :let [ns (inc step)]]
                     [a y ns])
               "L" (for [x (range a (+ (- distance) a) -1)
                         :let [ns (inc step)]]
                     [x b ns])
               "D" (for [y (range b (+ (- distance) b) -1)
                         :let [ns (inc step)]]
                     [a y ns])))))
