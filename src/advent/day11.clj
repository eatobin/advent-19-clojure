(ns advent.day11
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day11.csv"))

(ic/op-code {:input 1 :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? false})

(defn new-heading [heading turn]
  (cond
    (and (= heading :n) (= turn 0)) :w
    (and (= heading :n) (= turn 1)) :e
    (and (= heading :e) (= turn 0)) :n
    (and (= heading :e) (= turn 1)) :s
    (and (= heading :s) (= turn 0)) :e
    (and (= heading :s) (= turn 1)) :w
    (and (= heading :w) (= turn 0)) :s
    (and (= heading :w) (= turn 1)) :n))

(defn new-point [{:keys [x y]} h]
  (case h
    0 {:x x :y (+ y 1)}
    1 {:x (+ x 1) :y y}
    2 {:x x :y (+ y -1)}
    3 {:x (+ x -1) :y y}))

(def state {:pt {:x 0 :y 0} :c 0})

(def states (atom [{:pt {:x 0, :y 0}, :h :n, :c 0}]))

(def many-states [{:pt {:x 0, :y 0}, :c 1}
                  {:pt {:x 1, :y 1}, :c 1}
                  {:pt {:x 2, :y 2}, :c 0}
                  {:pt {:x 3, :y 3}, :h :s, :c 0}])

;(defn map-eq-pts [tpt pts]
;  (vec (map (fn [{pt :pt c :c}] (if (= tpt pt) c nil)) pts)))
;
;(defn dups-check [tpt pts]
;  (let [c (->>
;            pts
;            (map-eq-pts tpt)
;            (butlast)
;            (remove nil?)
;            (last))]
;    (if (nil? c)
;      0
;      c)))

;First, it will output a value indicating the color to paint the panel the robot is over:
;   0 means to paint the panel black, and 1 means to paint the panel white.
;Second, it will output a value indicating the direction the robot should turn:
;   0 means it should turn left 90 degrees, and 1 means it should turn right 90 degrees.

;(defn update-atom [coll p t]
;  (let [{:keys [pt h]} (last coll)
;        npt (new-point pt (new-heading h t))
;        new-2 [{:pt pt :c p} {:pt npt :h (new-heading h t) :c (dups-check npt coll)}]]
;    (into (vec (butlast coll)) new-2)))
;
;(swap! states update-atom 1 0)
;(swap! states update-atom 0 0)
;(swap! states update-atom 1 0)
;(swap! states update-atom 1 0)

;(swap! states update-atom 0 1)
;(swap! states update-atom 1 0)
;(swap! states update-atom 1 0)
