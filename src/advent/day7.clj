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
  (:output (ic/op-code {:input
                                (:output (ic/op-code {:input
                                                              (:output (ic/op-code {:input
                                                                                            (:output (ic/op-code {:input
                                                                                                                          (:output (ic/op-code {:input  0
                                                                                                                                                :output nil :phase a :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true}))
                                                                                                                  :output nil :phase b :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true}))
                                                                                    :output nil :phase c :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true}))
                                                      :output nil :phase d :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true}))
                        :output nil :phase e :pointer 0 :relative-base 0 :memory i-code :stopped? false :recur? true})))

(defn passes [i-code]
  (vec (map #(pass % i-code) possibles)))

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

(defn to-amps-vector [phases-vector memory]
  (vec (letfn [(to-amps [phases]
                 {1 (atom {:input 0 :output nil :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
                  2 (atom {:input nil :output nil :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
                  3 (atom {:input nil :output nil :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
                  4 (atom {:input nil :output nil :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
                  5 (atom {:input nil :output nil :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})})]
         (map to-amps phases-vector))))

(defn runner [five-amps]
  (loop [amps five-amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (:stopped? @(amps current-amp-no)))
      (:output @(amps current-amp-no))
      (let [op-this (atom (swap! (amps current-amp-no) ic/op-code))
            op-next (atom (swap! (amps next-amp-no) assoc :input (:output @op-this)))]
        (recur
          (assoc amps current-amp-no op-this next-amp-no op-next)
          next-amp-no
          (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (vec (map runner (to-amps-vector possibles-2 tv)))))

(println answer-2)

;35993240
