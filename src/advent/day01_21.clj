(ns advent.day01-21
  (:require [clojure.string :as str]))

(def v1 (->>
          "resources/day01_21.txt"
          (slurp)
          (str/split-lines)
          (map #(Integer/parseInt %))
          (into [])))

(def v2 (into [] (rest v1)))

(reduce + (map #(if % 1 0) (map < v1 v2)))

;; 1529
