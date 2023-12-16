(ns advent.day13
  (:require [advent.intcode :as ic]))

;; (x [from left] y [from top] id [below])
;; 0 is an empty tile. No game object appears in this tile.
;; 1 is a wall tile. Walls are indestructible barriers.
;; 2 is a block tile. Blocks can be broken by the ball.
;; 3 is a horizontal paddle tile. The paddle is indestructible.
;; 4 is a ball tile. The ball moves diagonally and bounces off objects.

;part a
(def memory (ic/make-memory "resources/day13.csv"))

(def raw-output (:output (ic/op-code {:input nil :output [] :phase nil :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true})))

(defn zip-it
  [instruction]
  (zipmap [:x :y :z] instruction))

(def tiles (map zip-it (partition 3 raw-output)))

(defn is-block? [tile] (if (= (:z tile) 2) 1 0))

(comment
  (reduce + (map is-block? tiles))
  (filter #(= (:z %) 1) tiles)
  (max-key val #:advent.day13{:x 43, :y 1, :z 1})
  (apply max-key val #:advent.day13{:x 43, :y 1, :z 1})
  :rcf)

;; 412

;part b

;; The arcade cabinet has a joystick that can move left and right. The software reads the position of the joystick with input instructions:

;; If the joystick is in the neutral position, provide 0.
;; If the joystick is tilted to the left, provide -1.
;; If the joystick is tilted to the right, provide 1.

;; (def memory-b (assoc memory 0 2))

;; (def raw-output-b-n ((ic/op-code {:input 0 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory-b :stopped? false :recur? true}) :output))

;; (def raw-output-b-l ((ic/op-code {:input -1 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory-b :stopped? false :recur? true}) :output))

;; (def raw-output-b-r ((ic/op-code {:input 1 :output [] :phase nil :pointer 0 :relative-base 0 :memory memory-b :stopped? false :recur? true}) :output))

;; (def tiles-n (vec (map vec (partition 3 raw-output-b-n))))

;; (def tiles-l (vec (map vec (partition 3 raw-output-b-l))))

;; (def tiles-r (vec (map vec (partition 3 raw-output-b-r))))
