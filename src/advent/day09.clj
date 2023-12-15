(ns advent.day09
  (:require [advent.intcode :as ic]))

;part a
(def memory (ic/make-tv "resources/day09.csv"))


(def answer (last ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}) :output)))

(comment
  answer
  )

; 3780860499

; part b
(def answer-2 (last ((ic/op-code {:input 2 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}) :output)))

(comment
  answer-2
  )

; 33343
