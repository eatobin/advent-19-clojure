(ns advent.day10
  (:require [clojure.string :as str]))

;part a
(def universe (->>
                "resources/day10-3-4-8.txt"
                (slurp)
                (str/split-lines)
                (into [])
                (map vec)
                (into [])))

(defn asteroid-points [universe]
  (vec (for [y0 (range (count universe))
             x0 (range (count universe))
             :when (= (get-in universe [y0 x0]) \#)]
         [x0 y0])))

(def asteroid-points-vec (asteroid-points universe))

(defn slope [[x0 y0] [x1 y1]]
  (if (= 0 (- x1 x0))
    0
    (/ (- y1 y0) (- x1 x0))))

(defn slopes [source asteroid-points]
  (map (partial slope source) asteroid-points))

(defn distinct-slopes [asteroid-points-vec]
  (for [ast asteroid-points-vec
        :let [less-a (remove #(= ast %) asteroid-points-vec)
              slopes (count (distinct (slopes ast less-a)))]]
    [ast slopes]))

(def answer (last (sort-by second (distinct-slopes asteroid-points-vec))))

(println answer)
