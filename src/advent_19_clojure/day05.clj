(ns advent-19-clojure.day05
  (:require [advent-19-clojure.intcode :as ic]))

;part a
(def memory (ic/make-memory "resources/day05.csv"))

(def answer (last ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}) :output)))

(comment
  answer
  (println answer)
  :rcf)

;9025675

;part b
(def answer-2 (last ((ic/op-code {:input 5 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}) :output)))

(comment
  answer-2
  (println answer-2)
  :rcf)

;11981754
