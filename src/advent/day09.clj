(ns advent.day09
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day09.csv"))


(def answer (last ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output)))

(println answer)

; 3780860499

; part b
(def answer-2 (last ((ic/op-code {:input 2 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output)))

(println answer-2)

; 33343
