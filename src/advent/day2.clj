(ns advent.day2
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day2.csv"))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(ic/op-code [0 0 0 0 (updated-memory 12 2) false true])

;(def fix-op-code (first (last (op-code [0 (updated-memory 12 2)]))))
;
;(println fix-op-code)

;2890696

;part b
;(def noun-verb
;  (for [noun (range 0 100)
;        verb (range 0 100)
;        :let [candidate (first (last (op-code [0 (updated-memory noun verb)])))]
;        :when (= candidate 19690720)]
;    [candidate noun verb (+ (* 100 noun) verb)]))
;
;(println noun-verb)

;[19690720 82 26 8226]
