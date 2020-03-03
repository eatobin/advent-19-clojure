(ns advent.day10
  (:require [clojure.string :as str]))

;part a
(def universe (->>
                "resources/day10a.txt"
                (slurp)
                (str/split-lines)
                (into [])
                (map vec)
                (into [])))

(defn plus-x [x i] (+ x i))

(map (partial plus-x 5) [1 2 3])

(defn map-row [row] (vec (map-indexed (fn [idx itm] [idx itm]) row)))
(defn map-rows [rows] (vec (map-indexed (fn [idx itm] [idx itm]) rows)))
;(defn map-rows [rows] (map-indexed (fn [idx itm] [idx [(map-row (itm 1))]]) rows))
;(vec (map-row [\. \# \. \. \#]))
;(map-indexed (fn [idx itm] [idx itm]) (map-row [\. \# \. \. \#]))
(map-indexed #(if (= %2 \#) %1)  [\. \# \. \. \#])
(vec (for [row universe]
       (vec (filter (complement nil?) (map-indexed #(if (= %2 \#) %1)  row)))))
(defn add-both [y x]
  [x y])
(def add-y (partial add-both 0))
(add-both 0 6)
(add-y 6)
(map add-y [1 4])
