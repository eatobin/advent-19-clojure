(ns advent.day9
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day9.csv")]
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

; y1
(defn a-p-w [pointer memory]
  (memory (+ 3 pointer)))

; y2, y9
(defn b-p-r-b-r-r [pointer memory relative-base]
  (get memory (+ (memory (+ 2 pointer)) relative-base) 0))

; y3, y10
(defn c-p-r-c-r-r [pointer memory relative-base]
  (get memory (+ (memory (+ 1 pointer)) relative-base) 0))

; y4, y6
(defn c-p-w-c-i-r [pointer memory]
  (memory (+ 1 pointer)))

; y5
(defn b-i-r [pointer memory]
  (memory (+ 2 pointer)))

; y7
(defn a-r-w [pointer memory relative-base]
  (+ (memory (+ 3 pointer)) relative-base))

; y8
(defn c-r-w [pointer memory relative-base]
  (+ (memory (+ 1 pointer)) relative-base))

(defn param-maker-c [instruction pointer memory relative-base]
  (case (instruction :e)
    1 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    2 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    3 (case (instruction :c)
        0 (c-p-w-c-i-r pointer memory)
        2 (c-r-w pointer memory relative-base))
    4 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    5 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    6 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    7 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    8 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))
    9 (case (instruction :c)
        0 (c-p-r-c-r-r pointer memory 0)
        1 (c-p-w-c-i-r pointer memory)
        2 (c-p-r-c-r-r pointer memory relative-base))))

(defn param-maker-b [instruction pointer memory relative-base]
  (case (instruction :e)
    1 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))
    2 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))
    5 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))
    6 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))
    7 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))
    8 (case (instruction :b)
        0 (b-p-r-b-r-r pointer memory 0)
        1 (b-i-r pointer memory)
        2 (b-p-r-b-r-r pointer memory relative-base))))

(defn param-maker-a [instruction pointer memory relative-base]
  (case (instruction :e)
    1 (case (instruction :a)
        0 (a-p-w pointer memory)
        2 (a-r-w pointer memory relative-base))
    2 (case (instruction :a)
        0 (a-p-w pointer memory)
        2 (a-r-w pointer memory relative-base))
    7 (case (instruction :a)
        0 (a-p-w pointer memory)
        2 (a-r-w pointer memory relative-base))
    8 (case (instruction :a)
        0 (a-p-w pointer memory)
        2 (a-r-w pointer memory relative-base))))

(defn op-code [[input phase pointer relative-base memory stopped? recur?]]
  (if stopped?
    [input phase pointer relative-base memory stopped? recur?]
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            [input phase pointer relative-base memory true recur?]
            (recur
              [input
               phase
               (+ 2 pointer)
               (+ (param-maker-c instruction pointer memory relative-base) relative-base)
               memory
               stopped?
               recur?]))
        1 (recur
            [input
             phase
             (+ 4 pointer)
             relative-base
             (assoc memory (param-maker-a instruction pointer memory relative-base)
                           (+ (param-maker-c instruction pointer memory relative-base)
                              (param-maker-b instruction pointer memory relative-base)))
             stopped?
             recur?])
        2 (recur
            [input
             phase
             (+ 4 pointer)
             relative-base
             (assoc memory (param-maker-a instruction pointer memory relative-base)
                           (* (param-maker-c instruction pointer memory relative-base)
                              (param-maker-b instruction pointer memory relative-base)))
             stopped?
             recur?])
        3 (recur
            [input
             phase
             (+ 2 pointer)
             relative-base
             (if (= 0 pointer)
               (assoc memory (param-maker-c instruction pointer memory relative-base) phase)
               (assoc memory (param-maker-c instruction pointer memory relative-base) input))
             stopped?
             recur?])
        4 (if recur?
            (recur
              [(param-maker-c instruction pointer memory relative-base)
               phase
               (+ 2 pointer)
               relative-base
               memory
               stopped?
               recur?])
            [(param-maker-c instruction pointer memory relative-base) phase (+ 2 pointer) relative-base memory stopped? recur?])
        5 (recur
            [input
             phase
             (if (= 0 (param-maker-c instruction pointer memory relative-base))
               (+ 3 pointer)
               (param-maker-b instruction pointer memory relative-base))
             relative-base
             memory
             stopped?
             recur?])
        6 (recur
            [input
             phase
             (if (not= 0 (param-maker-c instruction pointer memory relative-base))
               (+ 3 pointer)
               (param-maker-b instruction pointer memory relative-base))
             relative-base
             memory
             stopped?
             recur?])
        7 (recur
            [input
             phase
             (+ 4 pointer)
             relative-base
             (if (< (param-maker-c instruction pointer memory relative-base) (param-maker-b instruction pointer memory relative-base))
               (assoc memory (param-maker-a instruction pointer memory relative-base) 1)
               (assoc memory (param-maker-a instruction pointer memory relative-base) 0))
             stopped?
             recur?])
        8 (recur
            [input
             phase
             (+ 4 pointer)
             relative-base
             (if (= (param-maker-c instruction pointer memory relative-base) (param-maker-b instruction pointer memory relative-base))
               (assoc memory (param-maker-a instruction pointer memory relative-base) 1)
               (assoc memory (param-maker-a instruction pointer memory relative-base) 0))
             stopped?
             recur?])))))

(def answer (first (op-code [1 0 0 0 tv false true])))

(println answer)

; 3780860499

; part b
(def answer-2 (first (op-code [2 0 0 0 tv false true])))

(println answer-2)

; 33343
