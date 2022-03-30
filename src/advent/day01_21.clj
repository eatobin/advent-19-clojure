(ns advent.day01-21
  (:require [clojure.string :as str]))

; part a
(def v1 (->>
          "resources/day01_21.txt"
          (slurp)
          (str/split-lines)
          (map #(Integer/parseInt %))
          (into [])))

(def v2 (into [] (rest v1)))

(reduce + (map #(if % 1 0) (map < v1 v2)))

;; 1529

;part b
(def gp1 (into [] (partition 3 1 v1)))

(def gp2 (into [] (rest gp1)))

(def t1 (map #(reduce + %) gp1))

(def t2 (map #(reduce + %) gp2))

(reduce + (map #(if % 1 0) (map < t1 t2)))

;; 1567
