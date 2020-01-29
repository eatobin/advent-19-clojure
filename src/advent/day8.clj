(ns advent.day8
  (:require [clojure.core.matrix :as m]))

(def data "123456789012")

(defn to-ints [char-v]
  (vec (for [c char-v]
         (- (int c) 48))))

(defn input-data [layer-size num-rows]
  (->>
    data
    (partition layer-size)
    (m/matrix)
    (map to-ints)
    (map #(partition num-rows %))
    (m/matrix)))

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

(def smallest (first (first (zeros-in-layers (input 150 25)))))

(defn small-layer [layer-size num-rows]
  ((input layer-size num-rows) smallest))
