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
(defn degree [[x0 y0] [x1 y1]]
  (cond
    (and (= y0 y1) (< x0 x1)) 0.0
    (and (= y0 y1) (> x0 x1)) 180.0
    (and (= x0 x1) (< y0 y1)) 90.0
    (and (= x0 x1) (> y0 y1)) 270.0
    ;x inc, y inc - A
    (and (< x0 x1) (< y0 y1)) (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI)
    ;x dec, y dec - C
    (and (> x0 x1) (> y0 y1)) (+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180)
    ;x inc, y dec - D
    (and (< x0 x1) (> y0 y1)) (+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 360)
    ;x dec, y inc - B
    (and (> x0 x1) (< y0 y1)) (+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180)))

(degree [0 0] [1 0])
;=> 0.0
(degree [0 0] [1 1])
;=> 45.0
(degree [0 0] [0 1])
;=> 90.0
(degree [0 0] [-1 1])
;=> 135.0
(degree [0 0] [-1 0])
;=> 180.0
(degree [0 0] [-1 -1])
;=> 225.0
(degree [0 0] [0 -1])
;=> 270.0
(degree [0 0] [30000 -1])
;=> 359.9980901406836
