(ns advent.day07int
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day07.csv"))

(def possibles (for [a (range 0 5)
                     b (range 0 5)
                     c (range 0 5)
                     d (range 0 5)
                     e (range 0 5)
                     :when (distinct? a b c d e)]
                 [a b c d e]))

(defn pass [[a b c d e] i-code]
  ((ic/op-code {:input    ((ic/op-code {:input    ((ic/op-code {:input    ((ic/op-code {:input    ((ic/op-code {:input    0
                                                                                                                :output   0
                                                                                                                :phase    a
                                                                                                                :pointer  0
                                                                                                                :memory   i-code
                                                                                                                :stopped? false
                                                                                                                :recur?   true}) :output)
                                                                                        :output   0
                                                                                        :phase    b
                                                                                        :pointer  0
                                                                                        :memory   i-code
                                                                                        :stopped? false
                                                                                        :recur?   true}) :output)
                                                                :output   0
                                                                :phase    c
                                                                :pointer  0
                                                                :memory   i-code
                                                                :stopped? false
                                                                :recur?   true}) :output)
                                        :output   0
                                        :phase    d
                                        :pointer  0
                                        :memory   i-code
                                        :stopped? false
                                        :recur?   true}) :output)
                :output   0
                :phase    e
                :pointer  0
                :memory   i-code
                :stopped? false
                :recur?   true}) :output))

(defn passes [i-code]
  (map #(pass % i-code) possibles))

(def answer (apply max (passes tv)))

(println answer)

;368584

;part b
(def possibles-2 (for [a (range 5 10)
                       b (range 5 10)
                       c (range 5 10)
                       d (range 5 10)
                       e (range 5 10)
                       :when (distinct? a b c d e)]
                   [a b c d e]))

(defn to-amps-list [phases-vector memory]
  (letfn [(to-amps [phases]
            {1 (atom {:input 0 :output 0 :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             2 (atom {:input 0 :output 0 :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             3 (atom {:input 0 :output 0 :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             4 (atom {:input 0 :output 0 :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             5 (atom {:input 0 :output 0 :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})})]
    (map to-amps phases-vector)))

(defn runner [five-amps]
  (loop [amps five-amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (:stopped? @(amps current-amp-no)))
      (:output @(amps current-amp-no))
      (do (swap! (amps current-amp-no) ic/op-code)
          (swap! (amps next-amp-no) assoc :input (:output @(amps current-amp-no)))
          (recur
            (assoc amps current-amp-no (amps current-amp-no) next-amp-no (amps next-amp-no))
            next-amp-no
            (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (map runner (to-amps-list possibles-2 tv))))

(println answer-2)

;35993240
