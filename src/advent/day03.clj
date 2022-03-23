(ns advent.day03
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.math.numeric-tower :as math]
            [clojure.set :as s]))

;part a
;(def both (with-open [reader (io/reader "resources/day03b.csv")]
;            (doall
;              (csv/read-csv reader))))

(def both (with-open [reader (io/reader "resources/day03.csv")]
            (doall
              (csv/read-csv reader))))

(def red (first both))

(def blue (second both))

(defn direction [unit]
  (subs unit 0 1))

(defn distance [unit]
  (inc (Integer/parseInt (subs unit 1))))

(defn make-path [unit [a b]]
  (let [direction (direction unit)
        distance (distance unit)]
    (vec (case direction
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
        (vec (concat path (rest (make-path (first units) start))))))))

(defn abs-dist [[x y]]
  (+ (math/abs x) (math/abs y)))

(def answer
  (let [red-set (set (make-paths red [0 0]))
        blue-set (set (make-paths blue [0 0]))
        red-blue-intersect (s/intersection red-set blue-set)]
    (apply min (map abs-dist (disj red-blue-intersect [0 0])))))

(println answer)

;2193

;part b
(def blue-v (make-paths blue [0 0]))
(def red-v (make-paths red [0 0]))
(def blue-set (set blue-v))
(def red-set (set red-v))
(def blue-v-i (map-indexed vector blue-v))
(def red-v-i (map-indexed vector red-v))
(def red-blue-intersect (clojure.set/intersection red-set blue-set))

(def reds-k-v
  (rest (for [[k v] red-v-i
              hit red-blue-intersect
              :when (= v hit)]
          [k v])))

(def blues-k-v
  (rest (for [[k v] blue-v-i
              hit red-blue-intersect
              :when (= v hit)]
          [k v])))

(def answer-2 (apply min (for [[kr vr] reds-k-v
                               [kb vb] blues-k-v
                               :when (= vr vb)]
                           (+ kr kb))))

(println answer-2)

;63526
