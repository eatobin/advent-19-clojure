(ns day03.day03
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.math.numeric-tower :as math]
    [clojure.set :as s]))

;part a
(def both (with-open [reader (io/reader "resources/day03.csv")]
            (doall
              (csv/read-csv reader))))

(comment
  both)

(def red (first both))

(def blue (second both))

(defn direction [movement]
  (subs movement 0 1))

(defn distance [movement]
  (inc (Integer/parseInt (subs movement 1))))

(comment
  (def one (first red))
  one
  (direction one)
  (distance one)
  :rcf)

(defn make-path [movement [start-x start-y]]
  (let [direction (direction movement)
        distance (distance movement)]
    (vec (case direction
           "R" (for [x (range start-x (+ distance start-x))]
                 [x start-y])
           "U" (for [y (range start-y (+ distance start-y))]
                 [start-x y])
           "L" (for [x (range start-x (+ (- distance) start-x) -1)]
                 [x start-y])
           "D" (for [y (range start-y (+ (- distance) start-y) -1)]
                 [start-x y])))))

(comment
  (type (first (second (make-path "D5" [0 0]))))
  (type (second (second (make-path "D5" [0 0]))))
  :rcf)

(defn make-paths [movements start]
  (loop [movements movements
         start start
         path [start]]
    (if (empty? movements)
      path
      (recur
        (rest movements)
        (last (make-path (first movements) start))
        (vec (concat path (rest (make-path (first movements) start))))))))

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
