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
