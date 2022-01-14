(ns advent.day02int
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day02.csv"))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(def answer
  (((ic/op-code {:pointer 0
                 :memory  (updated-memory 12 2)})
    :memory)
   0))

(println answer)

;2890696

;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate (((ic/op-code {:pointer 0
                                            :memory  (updated-memory noun verb)})
                               :memory)
                              0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(println (last (first noun-verb)))

;8226