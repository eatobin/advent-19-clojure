(ns advent.day11
  (:require [advent.intcode :as ic]
            [clojure.math.numeric-tower :as math]))

;part a
(def tv (ic/make-tv "resources/day11.csv"))
(def tv-test (ic/make-tv "resources/day11-test.csv"))

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
    (and (= c 1) (= p 1)) 1
    (and (= c 0) (= p 0)) 0))

(def visits (atom [{:pt {:x 0 :y 0} :h :n :c 0 :rp nil}]))

(def oc (atom {:input nil :output nil :phase nil :pointer 0 :relative-base 0 :memory tv-test :stopped? false :recur? false}))

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

(defn runner [visits oc]
  (loop [c ((last @visits) :c)]
    (atom (reset! oc (ic/op-code (assoc @oc :input c))))
    (if (@oc :stopped?)
      (count (distinct (map :pt (vec (remove #(nil? (% :rp)) @visits)))))
      (do
        (atom (reset! visits (paint-atom @visits (@oc :output))))
        (atom (swap! oc ic/op-code))
        (atom (reset! visits (turn-atom @visits (@oc :output))))
        (recur
          ((last @visits) :c))))))

(def answer (runner visits oc))

(println answer)

;1771

;part b
(defn runner-2 [visits-2]
  (vec (map #(select-keys % [:pt :c]) visits-2)))

(def raw-visits (runner-2 (swap! visits butlast)))

(defn corrector [{{x :x, y :y} :pt c :c}]
  (let [col-adj (math/abs (apply min (map #(get-in % [:pt :x]) raw-visits)))
        row-adj (math/abs (apply max (map #(get-in % [:pt :y]) raw-visits)))]
    {:row (if (= y 0) row-adj (+ (- y) row-adj))
     :col (if (= x 0) col-adj (+ x col-adj))
     :c   c}))

;(def corrected (into (sorted-map) (zipmap (range) (map corrector raw-visits))))
(def corrected (vec (map corrector raw-visits)))

;(def num-rows (inc (apply max (map #(get-in % [1 :row]) corrected))))
;(def num-cols (inc (apply max (map #(get-in % [1 :col]) corrected))))
(def num-rows (inc (apply max (map #(get % :row) corrected))))
(def num-cols (inc (apply max (map #(get % :col) corrected))))

(def blank-row (into (sorted-map) (zipmap (range num-cols) (repeat 0))))
(def my-grid (atom (vec (vals (zipmap (range num-rows) (repeat blank-row))))))

;(def my-grid-w-atom (into (sorted-map) (map #(assoc-in % [1 :grid] my-grid) corrected)))
(def my-grid-w-atom (vec (map #(assoc % :grid my-grid) corrected)))

;(def my-grid-w-atom (into (sorted-map) (map #(assoc % :grid my-grid) corrected)))

(defn update-grid [{:keys [row col c grid]}]
  (vec (reset! grid (assoc-in @grid [row col] c))))

(def updated-grid (vec (map update-grid my-grid-w-atom)))

(clojure.pprint/print-table @my-grid)
;
;(assoc-in {0 {:row 1, :col 1, :c 1}} [0 :grid] my-grid)
;
;(map #(assoc-in % [1 :grid] my-grid) corrected)
;
;(reset! my-grid (assoc-in @my-grid [0 0] \u25A0))
;(reset! my-grid (assoc-in @my-grid [0 1] \u25A0))
;(reset! my-grid (assoc-in @my-grid [0 2] \u25A0))

;(println \u25A0)
;;■
;;=> nil
;(println \u25A1)
;;□
;;=> nil
;(assoc-in [] [0] \h)
;;=> [\h]
;(assoc-in [] [0 0] \h)
;;=> [{0 \h}]
;(assoc-in [[] []] [1 0] \h)
;;=> [[] [\h]]
;
;(map #(get-in % [0]) {19 {:row 1, :col 0, :c ""}})
;(map #(get-in % [1 :c]) {19 {:row 1, :col 0, :c ""}})
;
;(def scrambled {0 {:row 1, :col 0, :c ""},
;                1 {:row 0, :col 0, :c \u25A0},
;                2 {:row 1, :col 2, :c ""},
;                3 {:row 0, :col 2, :c \u25A0},
;                4 {:row 0, :col 1, :c ""},
;                5 {:row 1, :col 1, :c "X"}
;                6 {:row 1, :col 1, :c \u25A0}})
;
;(def table [{0 \u25A0 1 "" 2 \u25A0} {0 "" 1 \u25A0 2 ""}])
;
;(clojure.pprint/print-table [{0 \u25A0 1 \u25A1 2 \u25A0} {0 \u25A1 1 \u25A0 2 \u25A1} {0 \u25A0 1 \u25A1 2 \u25A0}])


;(def blank-row (into (sorted-map) (zipmap (range num-cols) (repeat 0))))
;(def my-grid (atom (vec (vals (zipmap (range num-rows) (repeat blank-row))))))
;; my-grid
;;=> [{0 "", 1 "", 2 ""} {0 "", 1 "", 2 ""}]
;(assoc-in @my-grid [(get-in scrambled [0 :row]) (get-in scrambled [0 :col])] 1)

;(reset! my-grid (assoc-in @my-grid [(get-in scrambled [6 :row]) (get-in scrambled [6 :col])] \u25A1))
;(reset! my-grid (assoc-in @my-grid [(get-in scrambled [2 :row]) (get-in scrambled [2 :col])] \u25A0))
;
;(def me {0 {:row 0 :col 0 :c 0 :grid my-grid}})
;@(get-in me [0 :grid])
;;=> [{0 0, 1 0, 2 0} {0 0, 1 0, 2 0}]
;(get-in me [0 :c])
