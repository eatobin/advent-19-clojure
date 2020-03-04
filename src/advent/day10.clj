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

(defn asteroids [universe]
  (vec (for [y0 (range 10)
             x0 (range 10)
             :when (= (get-in universe [y0 x0]) \#)]
         [x0 y0])))

(defn slope [[x0 y0] [x1 y1]]
  (if (= 0 (- x1 x0))
    0
    (/ (- y1 y0) (- x1 x0))))

(defn slopes [source asteroids]
  (map (partial slope source) asteroids))

(def asteroids (asteroids universe))

(defn distinct-slopes [asteroids]
  (for [ast asteroids
        :let [less-a (remove #(= ast %) asteroids)
              slopes (count (distinct (slopes ast less-a)))]]
    [ast slopes]))

(def answer (last (sort-by second (distinct-slopes asteroids))))

(println answer)
