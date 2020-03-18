(ns advent.day10
  (:require [clojure.string :as str]
            [clojure.algo.generic.math-functions :as trig])
  (:import (clojure.lang MapEntry)))

;part a
(def universe (->>
                "resources/day10-17-22-288.txt"
                (slurp)
                (str/split-lines)
                (into [])
                (map vec)
                (into [])))

(defn make-pad-line [universe]
  (map (fn [_] \.) (first universe)))

(defn pad [n coll val]
  (take n (concat coll (repeat val))))

(defn balance-universe [universe]
  (vec (pad (count (first universe)) universe (make-pad-line universe))))

(def balanced-universe (balance-universe universe))

(defn asteroid-points [balanced-universe]
  (vec (for [y0 (range (count balanced-universe))
             x0 (range (count balanced-universe))
             :when (= (get-in balanced-universe [y0 x0]) \#)]
         [x0 y0])))

(def asteroid-points-vec (asteroid-points balanced-universe))

(defn slope [[x0 y0] [x1 y1]]
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
;(slope [0 0] [1 0])
;;=> 0.0
;(slope [0 0] [1 1])
;;=> 45.0
;(slope [0 0] [0 1])
;;=> 90.0
;(slope [0 0] [-1 1])
;;=> 135.0
;(slope [0 0] [-1 0])
;;=> 180.0
;(slope [0 0] [-1 -1])
;;=> 225.0
;(slope [0 0] [0 -1])
;;=> 270.0
;(slope [0 0] [30000 -1])
;;=> 359.9980901406836

(defn cbd [[x0 y0] [x1 y1]]
  (+ (Math/abs (- y1 y0)) (Math/abs (- x1 x0))))

(defn slope-point [[x0 y0] [x1 y1]]
  (cond
    (and (= y0 y1) (< x0 x1)) [0.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= y0 y1) (> x0 x1)) [180.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= x0 x1) (< y0 y1)) [90.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= x0 x1) (> y0 y1)) [270.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x inc, y inc - A
    (and (< x0 x1) (< y0 y1)) [(/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x dec, y dec - C
    (and (> x0 x1) (> y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x inc, y dec - D
    (and (< x0 x1) (> y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 360) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x dec, y inc - B
    (and (> x0 x1) (< y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1] (cbd [x0 y0] [x1 y1])]))

(defn slopes-point [source asteroid-points]
  (map (partial slope-point source) asteroid-points))

;(sort (group-by first (slopes-point [0 0] (remove #(= [0 0] %) asteroid-points-vec))))

(defn grouped-slopes [source asteroid-points-vec]
  (let [less-a (remove #(= source %) asteroid-points-vec)]
    (vec (sort (group-by first (slopes-point source less-a))))))

(defn convert-it [degrees]
  (cond
    (= degrees 270.0) 0.0
    ;x inc, y inc - old A
    (> degrees 270.0) (- degrees 270.0)
    ;all else
    :else (+ degrees 90.0)))

(defn convert-key [[k v]]
  [(convert-it k) (vec (sort-by last (vec (map #(vec (rest %)) v))))])

(defn convert-keys [grouped-slopes-map]
  (vec (sort (map convert-key grouped-slopes-map))))

(def ss (grouped-slopes [17 22] asteroid-points-vec))
;=> #'advent.day10/ss
ss
;=>
;[[0.0 [[0.0 [6 2]]]]
; [9.46232220802562 [[9.46232220802562 [9 3]]]]
; [33.69006752597979 [[33.69006752597979 [6 4]] [33.69006752597979 [9 6]]]]
; [38.659808254090095 [[38.659808254090095 [8 6]]]]
; [45.0 [[45.0 [4 3]]]]
; [49.39870535499554 [[49.39870535499554 [9 9]]]]
; [50.19442890773481 [[50.19442890773481 [8 8]]]]
; [51.34019174590991 [[51.34019174590991 [7 7]]]]
; [53.13010235415597 [[53.13010235415597 [6 6]]]]
; [56.309932474020215 [[56.309932474020215 [5 5]]]]
; [63.43494882292201 [[63.43494882292201 [4 4]]]]
; [66.80140948635182 [[66.80140948635182 [6 9]]]]
; [75.96375653207353 [[75.96375653207353 [4 6]]]]
; [80.53767779197439 [[80.53767779197439 [4 8]]]]
; [90.0 [[90.0 [3 3]] [90.0 [3 6]] [90.0 [3 9]]]]
; [104.03624346792647 [[104.03624346792647 [2 6]]]]
; [116.56505117707799 [[116.56505117707799 [2 4]]]]
; [135.0 [[135.0 [2 3]]]]
; [153.43494882292202 [[153.43494882292202 [1 3]]]]
; [213.6900675259798 [[213.6900675259798 [0 0]]]]
; [270.0 [[270.0 [3 1]]]]]
(def cc (convert-keys ss))
;=>
;[[0.0 [[3 1]]]
; [90.0 [[6 2]]]
; [99.46232220802563 [[9 3]]]
; [123.6900675259798 [[6 4] [9 6]]]
; [128.65980825409008 [[8 6]]]
; [135.0 [[4 3]]]
; [139.39870535499554 [[9 9]]]
; [140.19442890773482 [[8 8]]]
; [141.34019174590992 [[7 7]]]
; [143.13010235415598 [[6 6]]]
; [146.30993247402023 [[5 5]]]
; [153.43494882292202 [[4 4]]]
; [156.80140948635182 [[6 9]]]
; [165.96375653207355 [[4 6]]]
; [170.53767779197437 [[4 8]]]
; [180.0 [[3 3] [3 6] [3 9]]]
; [194.03624346792645 [[2 6]]]
; [206.56505117707798 [[2 4]]]
; [225.0 [[2 3]]]
; [243.43494882292202 [[1 3]]]
; [303.69006752597977 [[0 0]]]]

(defn pad-by [points]
  (apply max (map #(count (second %)) points)))

(defn pad-points [points]
  (vec (for [[_ v] points]
         (vec (pad (pad-by points) v nil)))))

(def vv (pad-points cc))

(defn interleaved-points [points]
  (vec (filter (complement nil?) (apply interleave (pad-points points)))))

(def nn (interleaved-points cc))

(defn answer-2 [[[x y] _]]
  (+ (* x 100) y))

(println (answer-2 (nn 199)))
