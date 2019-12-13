(ns advent.day3
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
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

(def red-path [[0 0]])

(def blue-path [[0 0]])

(into [] (concat red-path [[6 6] [9 6] 7]))

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
