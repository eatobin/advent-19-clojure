(ns advent.day7
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a

(def tv (->> (first (with-open [reader (io/reader "resources/day7.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))

(defn param-mode-c [instruction pointer memory]
  (case (instruction :c)
    0 (memory (memory (+ 1 pointer)))
    1 (memory (+ 1 pointer))))

(defn param-mode-b [instruction pointer memory]
  (case (instruction :b)
    0 (memory (memory (+ 2 pointer)))
    1 (memory (+ 2 pointer))))

(defn param-mode-a [instruction pointer memory]
  (case (instruction :a)
    0 (memory (+ 3 pointer))))

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
           (assoc memory (param-mode-a instruction pointer memory)
                         (+ (param-mode-c instruction pointer memory)
                            (param-mode-b instruction pointer memory)))])
      2 (recur
          [input
           phase
           (+ 4 pointer)
           (assoc memory (param-mode-a instruction pointer memory)
                         (* (param-mode-c instruction pointer memory)
                            (param-mode-b instruction pointer memory)))])
      3 (recur
          [input
           phase
           (+ 2 pointer)
           (if (= 0 pointer)
             (assoc memory (memory (+ 1 pointer)) phase)
             (assoc memory (memory (+ 1 pointer)) input))])
      4 (recur
          [(memory (memory (+ 1 pointer)))
           phase
           (+ 2 pointer)
           memory])
      5 (recur
          [input
           phase
           (if (= 0 (param-mode-c instruction pointer memory))
             (+ 3 pointer)
             (param-mode-b instruction pointer memory))
           memory])
      6 (recur
          [input
           phase
           (if (not= 0 (param-mode-c instruction pointer memory))
             (+ 3 pointer)
             (param-mode-b instruction pointer memory))
           memory])
      7 (recur
          [input
           phase
           (+ 4 pointer)
           (if (< (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
             (assoc memory (memory (+ 3 pointer)) 1)
             (assoc memory (memory (+ 3 pointer)) 0))])
      8 (recur
          [input
           phase
           (+ 4 pointer)
           (if (= (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
             (assoc memory (memory (+ 3 pointer)) 1)
             (assoc memory (memory (+ 3 pointer)) 0))]))))

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
    [input phase pointer memory true]
    (loop [pointer pointer
           memory memory]
      (let [instruction (pad-5 (memory pointer))]
        (case (instruction :e)
          9 (if (= (instruction :d) 9)
              [input phase pointer memory true]
              memory)
          1 (recur
              (+ 4 pointer)
              (assoc memory (param-mode-a instruction pointer memory)
                            (+ (param-mode-c instruction pointer memory)
                               (param-mode-b instruction pointer memory))))
          2 (recur
              (+ 4 pointer)
              (assoc memory (param-mode-a instruction pointer memory)
                            (* (param-mode-c instruction pointer memory)
                               (param-mode-b instruction pointer memory))))
          3 (recur
              (+ 2 pointer)
              (if (= 0 pointer)
                (assoc memory (memory (+ 1 pointer)) phase)
                (assoc memory (memory (+ 1 pointer)) input)))
          4 [(param-mode-c instruction pointer memory) phase (+ 2 pointer) memory false]
          5 (recur
              (if (= 0 (param-mode-c instruction pointer memory))
                (+ 3 pointer)
                (param-mode-b instruction pointer memory))
              memory)
          6 (recur
              (if (not= 0 (param-mode-c instruction pointer memory))
                (+ 3 pointer)
                (param-mode-b instruction pointer memory))
              memory)
          7 (recur
              (+ 4 pointer)
              (if (< (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          8 (recur
              (+ 4 pointer)
              (if (= (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))))))))

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
