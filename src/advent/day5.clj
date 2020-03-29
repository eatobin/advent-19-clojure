(ns advent.day5
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day5.csv"))

(def answer ((ic/op-code {:input 1 :phase 0 :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :input))

(println answer) answer

;9025675

;part b
(def answer-2 ((ic/op-code {:input 5 :phase 0 :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :input))

(println answer-2)

;11981754
