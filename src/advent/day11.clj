(ns advent.day11
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day11.csv"))

(defn new-heading [heading turn]
  (cond
    (and (= heading 0) (= turn 0)) 3
    (and (= heading 3) (= turn 0)) 2
    (and (= heading 2) (= turn 0)) 1
    (and (= heading 1) (= turn 0)) 0
    (and (= heading 3) (= turn 1)) 0
    :else (+ heading turn)))

(defn new-point [{:keys [x y]} h]
  (case h
    0 {:x x :y (+ y 1)}
    1 {:x (+ x 1) :y y}
    2 {:x x :y (+ y -1)}
    3 {:x (+ x -1) :y y}))

(def state {:pt {:x 0 :y 0} :h 0 :c nil})

(defn revise-last [{:keys [pt h p]}]
  {:pt pt :h h :c p})

(defn new-last [{:keys [pt h t]}]
  {:pt (new-point pt h) :h (new-heading h t) :c nil})

(def states (atom [{:pt {:x 0, :y 0}, :h 0, :c nil}]))

(defn update-atom [coll p]
  (let [{:keys [pt h c]} (last coll)]
    {:pt pt :h h :c p}))
