(ns advent.day10
  (:require [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

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
    (and (< x0 x1) (< y0 y1)) (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI)
    ;x dec, y dec - C
    (and (> x0 x1) (> y0 y1)) (+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180)
    ;x inc, y dec - D
    (and (< x0 x1) (> y0 y1)) (+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 360)
    ;x dec, y inc - B
    (and (> x0 x1) (< y0 y1)) (+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180)))

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
(defn cbd [[x0 y0] [x1 y1]]
  (+ (math/abs (- y1 y0)) (math/abs (- x1 x0))))

(defn slope-point [[x0 y0] [x1 y1]]
  (cond
    (and (= y0 y1) (< x0 x1)) [0.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= y0 y1) (> x0 x1)) [180.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= x0 x1) (< y0 y1)) [90.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    (and (= x0 x1) (> y0 y1)) [270.0 [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x inc, y inc - A
    (and (< x0 x1) (< y0 y1)) [(/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x dec, y dec - C
    (and (> x0 x1) (> y0 y1)) [(+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x inc, y dec - D
    (and (< x0 x1) (> y0 y1)) [(+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 360) [x1 y1] (cbd [x0 y0] [x1 y1])]
    ;x dec, y inc - B
    (and (> x0 x1) (< y0 y1)) [(+ (/ (* (Math/atan (/ (- y1 y0) (- x1 x0))) 180) Math/PI) 180) [x1 y1] (cbd [x0 y0] [x1 y1])]))

(defn slopes-point [source asteroid-points]
  (map (partial slope-point source) asteroid-points))

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

(def ss (grouped-slopes (first answer) asteroid-points-vec))

(def cc (convert-keys ss))

(defn pad-by [points]
  (apply max (map #(count (second %)) points)))

(defn pad-points [points]
  (vec (for [[_ v] points]
         (vec (pad (pad-by points) v nil)))))

(defn interleaved-points [points]
  (vec (filter (complement nil?) (apply interleave (pad-points points)))))

(def nn (interleaved-points cc))

(defn answer-2 [[[x y] _]]
  (+ (* x 100) y))

(println (answer-2 (nn 199)))

;616
