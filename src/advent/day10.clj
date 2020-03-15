(ns advent.day10
  (:require [clojure.string :as str]
            [clojure.algo.generic.math-functions :as trig]))

;part a
(def universe (->>
                "resources/day10-3-2-21.txt"
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
(slope [0 0] [1 0])
;=> 0.0
(slope [0 0] [1 1])
;=> 45.0
(slope [0 0] [0 1])
;=> 90.0
(slope [0 0] [-1 1])
;=> 135.0
(slope [0 0] [-1 0])
;=> 180.0
(slope [0 0] [-1 -1])
;=> 225.0
(slope [0 0] [0 -1])
;=> 270.0
(slope [0 0] [30000 -1])
;=> 359.9980901406836

;(vec (distinct (slopes [17 22] asteroid-points-vec)))
;(count (vec (distinct (slopes [17 22] (remove #(= [17 22] %) asteroid-points-vec)))))
;=> 288
;(sort (zipmap (range) (sort (vec (distinct (slopes [0 0] (remove #(= [0 0] %) asteroid-points-vec)))))))

(defn slope-point [[x0 y0] [x1 y1]]
  (cond
    (and (= y0 y1) (< x0 x1)) [0.0 [x1 y1]]
    (and (= y0 y1) (> x0 x1)) [180.0 [x1 y1]]
    (and (= x0 x1) (< y0 y1)) [90.0 [x1 y1]]
    (and (= x0 x1) (> y0 y1)) [270.0 [x1 y1]]
    ;x inc, y inc - A
    (and (< x0 x1) (< y0 y1)) [(/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) [x1 y1]]
    ;x dec, y dec - C
    (and (> x0 x1) (> y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1]]
    ;x inc, y dec - D
    (and (< x0 x1) (> y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 360) [x1 y1]]
    ;x dec, y inc - B
    (and (> x0 x1) (< y0 y1)) [(+ (/ (* (trig/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1]]))

(defn slopes-point [source asteroid-points]
  (map (partial slope-point source) asteroid-points))

;(sort (group-by first (slopes-point [0 0] (remove #(= [0 0] %) asteroid-points-vec))))

(defn grouped-slopes [source asteroid-points-vec]
  (let [less-a (remove #(= source %) asteroid-points-vec)]
    (vec (sort (group-by first (slopes-point source less-a))))))

(defn convert-it [degrees]
  (cond
    (= degrees 270.0) 0.0
    (= degrees 0.0) 90.0
    (= degrees 90.0) 180.0
    (= degrees 180.0) 270.0
    ;x inc, y inc - old A
    (> degrees 270.0) (- degrees 270.0)
    ;x inc, y dec - old D
    (< degrees 90.0) (+ degrees 90.0)
    ;x dec, y dec - old C
    (and (> degrees 90.0) (< degrees 180.0)) (+ degrees 90.0)
    ;x dec, y inc - old B
    (and (> degrees 180.0) (< degrees 270.0)) (+ degrees 90.0)))
