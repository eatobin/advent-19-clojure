(ns advent.day3
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def both (with-open [reader (io/reader "paths-test-1.csv")]
            (doall
              (csv/read-csv reader))))

;(def both (with-open [reader (io/reader "paths.csv")]
;            (doall
;              (csv/read-csv reader))))

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
(def blue-v (make-paths blue [0 0]))
(def blue-v-i (map-indexed vector blue-v))
(def red-v (make-paths red [0 0]))
(def red-v-i (map-indexed vector red-v))

;(def answer-2
;  (apply min (into [] (rest (for [b blue-v-i
;                                  r red-v-i
;                                  :when (= (second b) (second r))
;                                  :let [ts (+ (first b) (first r))]]
;                              ts)))))
(rest (for [[k v] red-v-i
      hit red-blue-intersect
      :when (= v hit)]
  [k v]))
