(ns advent.day8
  (:require [clojure.core.matrix :as m]))

(defn to-ints [char-v]
  (vec (for [c char-v]
         (- (int c) 48))))

(defn input [layer-size num-rows]
  (->>
    "resources/day8.txt"
    (slurp)
    (partition layer-size)
    (m/matrix)
    (map to-ints)
    (map #(partition num-rows %))
    (m/matrix)))

(defn zeros-in-row [row]
  (->>
    row
    (filter #(= % 0))
    (count)))

(defn zeros-in-layer [layer]
  (reduce + (map zeros-in-row layer)))

(defn zeros-in-layers [layers]
  (vec (sort-by second < (map-indexed (fn [i l] (vector i (zeros-in-layer l)))
                                      layers))))

(defn smallest [layer-size num-rows] (first (first (zeros-in-layers (input layer-size num-rows)))))

(defn small-layer [layer-size num-rows]
  ((input layer-size num-rows) (smallest layer-size num-rows)))

(defn ones-in-row [row]
  (->>
    row
    (filter #(= % 1))
    (count)))

(defn ones-in-layer [layer]
  (reduce + (map ones-in-row layer)))

(defn twos-in-row [row]
  (->>
    row
    (filter #(= % 2))
    (count)))

(defn twos-in-layer [layer]
  (reduce + (map twos-in-row layer)))

(def answer (* (ones-in-layer (small-layer 150 25)) (twos-in-layer (small-layer 150 25))))

(println answer)

;1485
