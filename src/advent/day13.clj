(ns advent.day13
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day13.csv"))

(def raw-output ((ic/op-code {:input nil :output [] :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true}) :output))

(def tiles (vec (map vec (partition 3 raw-output))))

(defn is-block? [[_ _ id]] (if (= id 2) 1 0))

(reduce + (map is-block? tiles))

;; 412

;part b
;; (def tv-b (assoc tv 0 2))

;; (def raw-output-b-n ((ic/op-code {:input 0 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv-b :stopped? false :recur? true}) :output))

;; (def raw-output-b-l ((ic/op-code {:input -1 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv-b :stopped? false :recur? true}) :output))

;; (def raw-output-b-r ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory tv-b :stopped? false :recur? true}) :output))

;; (def tiles-n (vec (map vec (partition 3 raw-output-b-n))))

;; (def tiles-l (vec (map vec (partition 3 raw-output-b-l))))

;; (def tiles-r (vec (map vec (partition 3 raw-output-b-r))))
