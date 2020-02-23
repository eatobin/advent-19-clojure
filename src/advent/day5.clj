(ns advent.day5
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day5.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))

(defn param-c-ir-pw-iw [pointer memory]
  (memory (+ 1 pointer)))

(defn param-c-pr-rr-rw [pointer memory relative-base]
  (get memory (+ (memory (+ 1 pointer)) relative-base) 0))

(defn param-b-pr-rr [pointer memory relative-base]
  (get memory (+ (memory (+ 2 pointer)) relative-base) 0))

(defn param-b-ir [pointer memory]
  (memory (+ 2 pointer)))

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

(defn op-code [[input pointer memory]]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 (if (= (instruction :d) 9)
          [input pointer memory]
          "not used")
      1 (recur
          [input
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (+ (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      2 (recur
          [input
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (* (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      3 (recur
          [input
           (+ 2 pointer)
           (assoc memory (param-maker-c instruction pointer memory) input)])
      4 (recur
          [(param-maker-c instruction pointer memory)
           (+ 2 pointer)
           memory]))))

(def answer (first (op-code [1 0 tv])))

(println answer) answer

;9025675

;part b

(defn op-code-2 [[input pointer memory]]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 (if (= (instruction :d) 9)
          [input pointer memory]
          "not used")
      1 (recur
          [input
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (+ (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      2 (recur
          [input
           (+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (* (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      3 (recur
          [input
           (+ 2 pointer)
           (assoc memory (param-maker-c instruction pointer memory) input)])
      4 (recur
          [(param-maker-c instruction pointer memory)
           (+ 2 pointer)
           memory])
      5 (recur
          [input
           (if (= 0 (param-maker-c instruction pointer memory))
             (+ 3 pointer)
             (param-maker-b instruction pointer memory))
           memory])
      6 (recur
          [input
           (if (not= 0 (param-maker-c instruction pointer memory))
             (+ 3 pointer)
             (param-maker-b instruction pointer memory))
           memory])
      7 (recur
          [input
           (+ 4 pointer)
           (if (< (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
             (assoc memory (param-maker-a instruction pointer memory) 1)
             (assoc memory (param-maker-a instruction pointer memory) 0))])
      8 (recur
          [input
           (+ 4 pointer)
           (if (= (param-maker-c instruction pointer memory) (param-maker-b instruction pointer memory))
             (assoc memory (param-maker-a instruction pointer memory) 1)
             (assoc memory (param-maker-a instruction pointer memory) 0))]))))

(def answer-2 (first (op-code-2 [5 0 tv])))

(println answer-2)

;11981754
