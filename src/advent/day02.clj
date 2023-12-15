(ns advent.day02
  (:require [advent.intcode :as ic]))

;part a
(def memory (ic/make-tv "resources/day02.csv"))

(comment
  memory
  (memory 0)
  (memory 100)
  (count memory)
  )

(defn updated-memory [noun verb]
  (->
    memory
    (assoc 1 noun)
    (assoc 2 verb)))

(def answer ((:memory (ic/op-code {:input 0 :output [] :phase nil :pointer 0 :relative-base 0 :memory (updated-memory 12 2) :stopped? false :recur? true})) 0))

(comment answer)

;2890696

;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate ((:memory (ic/op-code {:input 0 :output [] :phase nil :pointer 0 :relative-base 0 :memory (updated-memory noun verb) :stopped? false :recur? true})) 0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(comment (last (first noun-verb)))

;8226
