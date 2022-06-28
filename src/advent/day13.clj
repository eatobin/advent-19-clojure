(ns advent.day13
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day13.csv"))

(ic/op-code {:input 0 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true})
