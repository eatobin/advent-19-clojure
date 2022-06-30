(ns advent.day13
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day13.csv"))

(def raw-output ((ic/op-code {:input nil :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output))

(def tiles (vec (map vec (partition 3 raw-output))))

(defn is-block? [[_ _ id]] (if (= id 2) 1 0))

(reduce + (map is-block? tiles))

;; 412
