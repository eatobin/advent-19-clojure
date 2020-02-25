(ns advent.day2
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day2.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))

; y1
(defn a-p-w [pointer memory]
  (memory (+ 3 pointer)))

; y2
(defn b-p-r [pointer memory relative-base]
  (get memory (+ (memory (+ 2 pointer)) relative-base) 0))

; y3
(defn c-p-r [pointer memory relative-base]
  (get memory (+ (memory (+ 1 pointer)) relative-base) 0))

(defn param-maker-c [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :c)
        0 (c-p-r pointer memory 0))
    2 (case (instruction :c)
        0 (c-p-r pointer memory 0))))

(defn param-maker-b [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :b)
        0 (b-p-r pointer memory 0))
    2 (case (instruction :b)
        0 (b-p-r pointer memory 0))))

(defn param-maker-a [instruction pointer memory]
  (case (instruction :e)
    1 (case (instruction :a)
        0 (a-p-w pointer memory))
    2 (case (instruction :a)
        0 (a-p-w pointer memory))))

(defn op-code [[pointer memory]]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 (if (= (instruction :d) 9)
          [pointer memory]
          "not used")
      1 (recur
          [(+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (+ (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))])
      2 (recur
          [(+ 4 pointer)
           (assoc memory (param-maker-a instruction pointer memory)
                         (* (param-maker-c instruction pointer memory)
                            (param-maker-b instruction pointer memory)))]))))

(def fix-op-code (first (last (op-code [0 (updated-memory 12 2)]))))

(println fix-op-code)

;2890696

;part b
(def noun-verb
  (for [noun (range 0 100)
        verb (range 0 100)
        :let [candidate (first (last (op-code [0 (updated-memory noun verb)])))]
        :when (= candidate 19690720)]
    [candidate noun verb (+ (* 100 noun) verb)]))

(println noun-verb)

;[19690720 82 26 8226]
