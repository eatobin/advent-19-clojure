(ns advent.day10
  (:require [clojure.string :as str]
            [clojure.algo.generic.math-functions :as trig]))

;part a
(def universe (->>
                "resources/day10.txt"
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
  (cond
    (and (= y0 y1) (< x0 x1)) [:h :out]
    (and (= y0 y1) (> x0 x1)) [:h :in]
    (and (= x0 x1) (< y0 y1)) [:v :out]
    (and (= x0 x1) (> y0 y1)) [:v :in]
    (and (< x0 x1) (< y0 y1)) [(/ (- y1 y0) (- x1 x0)) :out]
    (and (> x0 x1) (> y0 y1)) [(/ (- y1 y0) (- x1 x0)) :in]
    (and (< x0 x1) (> y0 y1)) [(/ (- y1 y0) (- x1 x0)) :out]
    (and (> x0 x1) (< y0 y1)) [(/ (- y1 y0) (- x1 x0)) :in]))

(defn slopes [source asteroid-points]
  (map (partial slope source) asteroid-points))

(defn distinct-slopes [asteroid-points-vec]
  (for [ast asteroid-points-vec
        :let [less-a (remove #(= ast %) asteroid-points-vec)
              slopes (count (distinct (slopes ast less-a)))]]
    [ast slopes]))

(def answer (last (sort-by second (distinct-slopes asteroid-points-vec))))

(println answer)

;[[17 22] 288]

;part b
(/ (* (trig/atan2 0 1) 180) Math/PI)
;=> 0.0
(/ (* (trig/atan2 1 1) 180) Math/PI)
;=> 45.0
(/ (* (trig/atan2 1 0) 180) Math/PI)
;=> 90.0
(/ (* (trig/atan2 1 -1) 180) Math/PI)
;=> 135.0
(/ (* (trig/atan2 0 -1) 180) Math/PI)
;=> 180.0
(/ (* (trig/atan2 -1 -1) 180) Math/PI)
;=> -135.0
(+ (- (/ (* (trig/atan2 -1 0) 180) Math/PI)) 180)
;=> -90.0
(/ (* (trig/atan2 -1 1) 180) Math/PI)
;=> -45.0
