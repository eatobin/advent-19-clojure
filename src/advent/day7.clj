(ns advent.day7
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day7.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])
             (zipmap (range))
             (into (sorted-map-by <))))

(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))
; y4 y6
(defn param-c-ir-pw-iw [pointer memory]
  (memory (+ 1 pointer)))
; y3
(defn param-c-pr-rr-rw [pointer memory relative-base]
  (get memory (+ (memory (+ 1 pointer)) relative-base) 0))
; y2
(defn param-b-pr-rr [pointer memory relative-base]
  (get memory (+ (memory (+ 2 pointer)) relative-base) 0))
; y5
(defn param-b-ir [pointer memory]
  (memory (+ 2 pointer)))
; y1
(defn param-a-pw-iw [pointer memory]
  (memory (+ 3 pointer)))

(defn param-maker-c [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))
    2 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))
    3 (param-c-ir-pw-iw pointer memory)
    4 (param-c-pr-rr-rw pointer memory 0)
    5 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))
    6 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))
    7 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))
    8 (case (instruction :c)
        0 (param-c-pr-rr-rw pointer memory 0)
        1 (param-c-ir-pw-iw pointer memory))))

(defn param-maker-b [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))
    2 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))
    5 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))
    6 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))
    7 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))
    8 (case (instruction :b)
        0 (param-b-pr-rr pointer memory 0)
        1 (param-b-ir pointer memory))))

(defn param-maker-a [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :a)
        0 (param-a-pw-iw pointer memory)
        1 (param-a-pw-iw pointer memory))
    2 (case (instruction :a)
        0 (param-a-pw-iw pointer memory)
        1 (param-a-pw-iw pointer memory))
    7 (case (instruction :a)
        0 (param-a-pw-iw pointer memory)
        1 (param-a-pw-iw pointer memory))
    8 (case (instruction :a)
        0 (param-a-pw-iw pointer memory)
        1 (param-a-pw-iw pointer memory))))

(defn op-code [[input phase pointer memory]]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 (if (= (instruction :d) 9)
          [input phase pointer memory]
          "not used")
      1 (recur
          [input
           phase
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (+ (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      2 (recur
          [input
           phase
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (* (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      3 (recur
          [input
           phase
           (+ 2 pointer)
           (if (= 0 pointer)
             (assoc memory (param-maker-c instruction pointer memory) phase)
             (assoc memory (param-maker-c instruction pointer memory) input))])
      4 (recur
          [(param-maker-c instruction pointer memory)
           phase
           (+ 2 pointer)
           memory])
      5 (recur
          [input
           phase
           (if (= 0 (param-maker-c instruction pointer memory))
             (+ 3 pointer)
             (param-maker-b instruction pointer memory))
           memory])
      6 (recur
          [input
           phase
           (if (not= 0 (param-maker-c instruction pointer memory))
             (+ 3 pointer)
             (param-maker-b instruction pointer memory))
           memory])
      7 (recur
          [input
           phase
           (+ 4 pointer)
           (if (< (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
             (assoc memory (param-maker-a instruction pointer memory) 1)
             (assoc memory (param-maker-a instruction pointer memory) 0))])
      8 (recur
          [input
           phase
           (+ 4 pointer)
           (if (= (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
             (assoc memory (param-maker-a instruction pointer memory) 1)
             (assoc memory (param-maker-a instruction pointer memory) 0))]))))

(def possibles (for [a (range 0 5)
                     b (range 0 5)
                     c (range 0 5)
                     d (range 0 5)
                     e (range 0 5)
                     :when (distinct? a b c d e)]
                 [a b c d e]))

(defn pass [[a b c d e] i-code]
  (first (op-code [
                   (first (op-code [
                                    (first (op-code [
                                                     (first (op-code [
                                                                      (first (op-code [
                                                                                       0 a 0 i-code]))
                                                                      b 0 i-code]))
                                                     c 0 i-code]))
                                    d 0 i-code]))
                   e 0 i-code])))

(defn passes [i-code]
  (vec (map #(pass % i-code) possibles)))

(def answer (apply max (passes tv)))

(println answer)

;368584

;part b
(defn op-code-2 [[input phase pointer memory stopped?]]
  (if stopped?
    [input phase pointer memory stopped?]
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            [input phase pointer memory true]
            "not used")
        1 (recur
            [input
             phase
             (+ 4 pointer)
             (assoc memory (param-maker-a instruction pointer memory)
                           (+ (param-maker-c instruction pointer memory)
                              (param-maker-b instruction pointer memory)))
             stopped?])
        2 (recur
            [input
             phase
             (+ 4 pointer)
             (assoc memory (param-maker-a instruction pointer memory)
                           (* (param-maker-c instruction pointer memory)
                              (param-maker-b instruction pointer memory)))
             stopped?])
        3 (recur
            [input
             phase
             (+ 2 pointer)
             (if (= 0 pointer)
               (assoc memory (param-maker-c instruction pointer memory) phase)
               (assoc memory (param-maker-c instruction pointer memory) input))
             stopped?])
        4 [(param-maker-c instruction pointer memory) phase (+ 2 pointer) memory stopped?]
        5 (recur
            [input
             phase
             (if (= 0 (param-maker-c instruction pointer memory))
               (+ 3 pointer)
               (param-maker-b instruction pointer memory))
             memory
             stopped?])
        6 (recur
            [input
             phase
             (if (not= 0 (param-maker-c instruction pointer memory))
               (+ 3 pointer)
               (param-maker-b instruction pointer memory))
             memory
             stopped?])
        7 (recur
            [input
             phase
             (+ 4 pointer)
             (if (< (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
               (assoc memory (param-maker-a instruction pointer memory) 1)
               (assoc memory (param-maker-a instruction pointer memory) 0))
             stopped?])
        8 (recur
            [input
             phase
             (+ 4 pointer)
             (if (= (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
               (assoc memory (param-maker-a instruction pointer memory) 1)
               (assoc memory (param-maker-a instruction pointer memory) 0))
             stopped?])))))

(def possibles-2 (for [a (range 5 10)
                       b (range 5 10)
                       c (range 5 10)
                       d (range 5 10)
                       e (range 5 10)
                       :when (distinct? a b c d e)]
                   [a b c d e]))

(defn to-amps-vector [phases-vector memory]
  (letfn [(to-amps [phases]
            {1 (atom [0 (phases 0) 0 memory false])
             2 (atom [nil (phases 1) 0 memory false])
             3 (atom [nil (phases 2) 0 memory false])
             4 (atom [nil (phases 3) 0 memory false])
             5 (atom [nil (phases 4) 0 memory false])})]
    (map to-amps phases-vector)))

(defn runner [amps]
  (loop [amps amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (last @(amps current-amp-no)))
      (first @(amps current-amp-no))
      (let [op-this (atom (swap! (amps current-amp-no) op-code-2))
            op-next (atom (swap! (amps next-amp-no) assoc 0 (first @op-this)))]
        (recur
          (assoc amps current-amp-no op-this next-amp-no op-next)
          next-amp-no
          (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (vec (map runner (to-amps-vector possibles-2 tv)))))

(println answer-2)

;35993240
