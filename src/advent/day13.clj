(ns advent.day13
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day13.csv"))

(def oc (atom {:input nil :output nil :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? false}))

(swap! oc ic/op-code)
