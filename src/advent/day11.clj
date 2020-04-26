(ns advent.day11
  (:require [advent.intcode :as ic]))

;First, it will output a value indicating the color to paint the panel the robot is over:
;   0 means to paint the panel black, and 1 means to paint the panel white.
;Second, it will output a value indicating the direction the robot should turn:
;   0 means it should turn left 90 degrees, and 1 means it should turn right 90 degrees.

;part a
(def tv (ic/make-tv "resources/day11.csv"))

(ic/op-code {:input 0 :output nil :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true})

(defn new-heading [heading turn]
  (cond
    (and (= heading :n) (= turn 0)) :w
    (and (= heading :n) (= turn 1)) :e
    (and (= heading :e) (= turn 0)) :n
    (and (= heading :e) (= turn 1)) :s
    (and (= heading :s) (= turn 0)) :e
    (and (= heading :s) (= turn 1)) :w
    (and (= heading :w) (= turn 0)) :s
    (and (= heading :w) (= turn 1)) :n))

(defn new-point [{x :x y :y} h]
  (case h
    :n {:x x :y (inc y)}
    :e {:x (inc x) :y y}
    :s {:x x :y (dec y)}
    :w {:x (dec x) :y y}))

(defn repainted? [c p]
  (cond
    (and (= c 0) (= p 1)) 1
    (and (= c 1) (= p 0)) 0
    :else nil))

(def state {:pt {:x 0 :y 0} :h :n :c 0 :rp nil})

;(def robots-vector [(atom [{:ic {:input 0 :output nil :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? false?} :pt {:x 0, :y 0}, :h :n, :c 0 :rp nil}])])

;(def robot (atom {:ic {:input nil :output nil :phase nil :pointer 0 :relative-base 0 :memory {0 3, 1 0, 2 99} :stopped? false :recur? true} :visits [{:pt {:x 0 :y 0} :h :n :c 0 :rp nil}]}))

(def oc (atom {:input nil :output nil :phase nil :pointer 0 :relative-base 0 :memory (into (sorted-map) {0 3, 1 0, 2 4, 3 9, 4 3, 5 0, 6 4, 7 10, 8 99, 9 1, 10 0}) :stopped? false :recur? true}))

(def visits (atom [{:pt {:x 0 :y 0} :h :n :c 0 :rp nil}]))

;(def two-robots {1 robot 2 robot})

(def many-states [{:pt {:x 0, :y 0}, :h :n :c 0 :rp nil}
                  {:pt {:x 1, :y 1}, :h :n :c 0 :rp nil}
                  {:pt {:x 2, :y 2}, :h :n :c 0 :rp nil}
                  {:pt {:x 3, :y 3}, :h :n :c 0 :rp nil}])

(defn map-eq-pts
  "Takes a {:x 3 :y 3} as target point (tpt)"
  [tpt pts]
  (vec (map (fn [{pt :pt c :c}] (if (= tpt pt) c nil)) pts)))

(defn dups-check
  "Takes a {:x 3 :y 3} as target point (tpt)"
  [tpt pts]
  (let [c (->>
            pts
            (map-eq-pts tpt)
            (butlast)
            (remove nil?)
            (last))]
    (if (nil? c)
      0
      c)))

(defn paint-atom [coll p]
  (let [{:keys [pt h c]} (last coll)
        painted [{:pt pt :h h :c p :rp (repainted? c p)}]]
    (into (vec (butlast coll)) painted)))

(defn turn-atom [coll t]
  (let [{:keys [pt h]} (last coll)
        new-point (new-point pt (new-heading h t))
        turned [{:pt new-point :h (new-heading h t) :c (dups-check new-point coll) :rp nil}]]
    (into (vec coll) turned)))

(defn count-repaints [coll]
  (count (filter #(some? (% :rp)) coll)))

;;[1 0]
;(swap! states paint-atom 1)
;(swap! states turn-atom 0)
;
;;[0 0]
;(swap! states paint-atom 0)
;(swap! states turn-atom 0)
;
;;[1 0]
;(swap! states paint-atom 1)
;(swap! states turn-atom 0)
;
;;[1 0]
;(swap! states paint-atom 1)
;(swap! states turn-atom 0)
;
;;[0 1]
;(swap! states paint-atom 0)
;(swap! states turn-atom 1)
;
;;[1 0]
;(swap! states paint-atom 1)
;(swap! states turn-atom 0)
;
;;[1 0]
;(swap! states paint-atom 1)
;(swap! states turn-atom 0)
;
;(count-repaints @states)

;.....
;..<#.
;...#.
;.##..
;.....

;(defn runnerX [oc visits]
;  (let [c ((last @visits) :c)]
;    (do
;      (atom (reset! oc (ic/op-code (assoc @oc :input c))))
;      (atom (reset! visits (paint-atom @visits (@oc :output))))
;      (atom (reset! oc (ic/op-code @oc)))
;      ;turn-visits (atom (reset! visits (turn-atom @visits (@painter-visits :output))))]
;      [c @oc @visits])))



(defn runnerX [oc visits]
  (if (@oc :stopped?)
    [@oc @visits]
    (loop [oc oc
           visits visits
           on-paint? true
           c ((last @visits) :c)]
      (if on-paint?
        (do
          (atom (reset! oc (ic/op-code (assoc @oc :input c))))
          (atom (reset! visits (paint-atom @visits (@oc :output)))))
        (do
          (atom (reset! oc (ic/op-code @oc)))
          (atom (reset! visits (turn-atom @visits (@oc :output))))))
      (recur
        oc
        visits
        false
        ((last @visits) :c)))))




;(defn runner [robot]
;    (if (get-in @robot [:ic :stopped?])
;      (get-in @robot [:visits])
;      (let [on-paint true
;            {:keys [ic visits]} @robot
;            c (visits :c)
;            paint-answer ()
;            robot-init (atom (swap! (robots current-robot-no) assoc-in [:ic :input] over-color))
;            visits-this (get-in @robot-init [:visits])
;            paint-this (paint-atom visits-this (robot-init :output))
;            op-next (atom (swap! (robots next-robot-no) assoc :input (:output @op-this)))]
;        (recur
;          (assoc robots current-robot-no op-this next-robot-no op-next)
;          next-robot-no
;          (+ 1 (mod next-robot-no 5))))))



;(defn runner [states]
;  (loop [states states
;         over-color ((last @states) :c)
;         op-code-1 (ic/op-code {:input over-color :output nil :phase nil :pointer 0 :relative-base 0 :memory tv :stopped? false :recur? true})
;         answer-1 (op-code-1 :output)
;         atom-update-1 (atom (swap! states paint-atom answer-1))
;         op-code-2 (ic/op-code (assoc op-code-1 :input over-color))
;         answer-2 (op-code-2 :output)
;         atom-update-2 (atom (swap! states turn-atom answer-2))]
;    (if (or (:stopped? op-code-1) (:stopped? op-code-2))
;      [over-color op-code-1 answer-1 @atom-update-1 over-color op-code-2 answer-2 @atom-update-2]
;      (recur
;        states
;        ((last @states) :c)
;        (ic/op-code (assoc op-code-2 :input over-color))
;        (op-code-1 :output)
;        (atom (swap! states paint-atom answer-1))
;        (ic/op-code (assoc op-code-1 :input over-color))
;        (op-code-2 :output)
;        (atom (swap! states turn-atom answer-2))))))


;(get-in @(two-robots 1) [:ic :memory 677])
;;=> -5
;(get-in @(two-robots 1) [:ic :input])
;;=> 0
;(get-in @(two-robots 1) [:visits 0 :pt])
;;=> {:x 0, :y 0}
;(get-in @(two-robots 2) [:visits 0 :pt])
;=> {:x 0, :y 0}
