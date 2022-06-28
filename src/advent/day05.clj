(ns advent.day05
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day05.csv"))

(def answer (last ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output)))

(println answer) answer

;9025675

;part b
(def answer-2 (last ((ic/op-code {:input 5 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output)))

(println answer-2)

;11981754
