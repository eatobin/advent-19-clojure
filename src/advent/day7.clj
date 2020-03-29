(ns advent.day7
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day7.csv"))

(def possibles (for [a (range 0 5)
                     b (range 0 5)
                     c (range 0 5)
                     d (range 0 5)
                     e (range 0 5)
                     :when (distinct? a b c d e)]
                 [a b c d e]))

(defn pass [[a b c d e] i-code]
  (:input (ic/op-code {:input (:input (ic/op-code {:input (:input (ic/op-code {:input (:input (ic/op-code {:input (:input (ic/op-code {:input 0 :phase a :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})) :phase b :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})) :phase c :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})) :phase d :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})) :phase e :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})))

(defn passes [i-code]
  (vec (map #(pass % i-code) possibles)))

(def answer (apply max (passes tv)))

(println answer)

;368584

;part b
;(defn op-code-2 [[input phase pointer memory stopped?]]
;  (if stopped?
;    [input phase pointer memory stopped?]
;    (let [instruction (pad-5 (memory pointer))]
;      (case (instruction :e)
;        9 (if (= (instruction :d) 9)
;            [input phase pointer memory true]
;            "not used")
;        1 (recur
;            [input
;             phase
;             (+ 4 pointer)
;             (assoc memory (param-maker-a instruction pointer memory)
;                           (+ (param-maker-c instruction pointer memory)
;                              (param-maker-b instruction pointer memory)))
;             stopped?])
;        2 (recur
;            [input
;             phase
;             (+ 4 pointer)
;             (assoc memory (param-maker-a instruction pointer memory)
;                           (* (param-maker-c instruction pointer memory)
;                              (param-maker-b instruction pointer memory)))
;             stopped?])
;        3 (recur
;            [input
;             phase
;             (+ 2 pointer)
;             (if (= 0 pointer)
;               (assoc memory (param-maker-c instruction pointer memory) phase)
;               (assoc memory (param-maker-c instruction pointer memory) input))
;             stopped?])
;        4 [(param-maker-c instruction pointer memory) phase (+ 2 pointer) memory stopped?]
;        5 (recur
;            [input
;             phase
;             (if (= 0 (param-maker-c instruction pointer memory))
;               (+ 3 pointer)
;               (param-maker-b instruction pointer memory))
;             memory
;             stopped?])
;        6 (recur
;            [input
;             phase
;             (if (not= 0 (param-maker-c instruction pointer memory))
;               (+ 3 pointer)
;               (param-maker-b instruction pointer memory))
;             memory
;             stopped?])
;        7 (recur
;            [input
;             phase
;             (+ 4 pointer)
;             (if (< (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
;               (assoc memory (param-maker-a instruction pointer memory) 1)
;               (assoc memory (param-maker-a instruction pointer memory) 0))
;             stopped?])
;        8 (recur
;            [input
;             phase
;             (+ 4 pointer)
;             (if (= (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
;               (assoc memory (param-maker-a instruction pointer memory) 1)
;               (assoc memory (param-maker-a instruction pointer memory) 0))
;             stopped?])))))
;
;(def possibles-2 (for [a (range 5 10)
;                       b (range 5 10)
;                       c (range 5 10)
;                       d (range 5 10)
;                       e (range 5 10)
;                       :when (distinct? a b c d e)]
;                   [a b c d e]))
;
;(defn to-amps-vector [phases-vector memory]
;  (letfn [(to-amps [phases]
;            {1 (atom [0 (phases 0) 0 memory false])
;             2 (atom [nil (phases 1) 0 memory false])
;             3 (atom [nil (phases 2) 0 memory false])
;             4 (atom [nil (phases 3) 0 memory false])
;             5 (atom [nil (phases 4) 0 memory false])})]
;    (map to-amps phases-vector)))
;
;(defn runner [amps]
;  (loop [amps amps
;         current-amp-no 1
;         next-amp-no (+ 1 (mod current-amp-no 5))]
;    (if (and (= 5 current-amp-no) (last @(amps current-amp-no)))
;      (first @(amps current-amp-no))
;      (let [op-this (atom (swap! (amps current-amp-no) op-code-2))
;            op-next (atom (swap! (amps next-amp-no) assoc 0 (first @op-this)))]
;        (recur
;          (assoc amps current-amp-no op-this next-amp-no op-next)
;          next-amp-no
;          (+ 1 (mod next-amp-no 5)))))))
;
;(def answer-2 (apply max (vec (map runner (to-amps-vector possibles-2 tv)))))
;
;(println answer-2)

;35993240
