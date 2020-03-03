(ns advent.day10
  (:require [clojure.string :as str]))

;part a
(def universe (->>
                "resources/day10bERIC.txt"
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
(map-indexed #(if (= %2 \#) %1) [\. \# \. \. \#])
(vec (for [row universe]
       (vec (filter (complement nil?) (map-indexed #(if (= %2 \#) %1) row)))))
(defn add-both [y x]
  [x y])
(def add-y (partial add-both 0))
(add-both 0 6)
(add-y 6)
(map add-y [1 4])

universe
;=> [[\. \# \. \. \#] [\. \. \. \. \.] [\# \# \# \# \#] [\. \. \. \. \#] [\. \. \. \# \#]]
(get-in universe [0])
;=> [\. \# \. \. \#]
(get-in universe [0 0])
;=> \.
(get-in universe [0 4])
;=> \#
(get-in universe [4 4])
;=> \#
(get-in universe [4 0])
;=> \.
(for [i (range 1 10)
      :when (even? i)
      :let [inverse (/ 1 i)]]
  [i inverse])

(for [c [:2 :3 :4 :5 :6 :7 :8 :9 :10 :J :Q :K :A]
      s [:♠ :♥ :♣ :♦]]
  [c s])

(vec (for [y0 (range 10)
           x0 (range 10)
           y1 (range 9 -1 -1)
           x1 (range 9 -1 -1)
           :when (and (= (get-in universe [y0 x0]) \#) (= (get-in universe [y1 x1]) \#))]
       [[x0 y0] [x1 y1]]))
