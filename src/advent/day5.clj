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

(defn op-code [[input memory]]
  (loop [input input
         memory memory
         pointer 0]
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            input
            memory)
        1 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (+ (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        2 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (* (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        3 (recur
            input
            (assoc memory (memory (+ 1 pointer)) input)
            (+ 2 pointer))
        4 (recur
            (memory (memory (+ 1 pointer)))
            memory
            (+ 2 pointer))))))

(def answer (op-code [1 tv]))

(println answer) answer

;9025675

;part b

(defn op-code-2 [[input memory]]
  (loop [input input
         memory memory
         pointer 0]
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            input
            memory)
        1 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (+ (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        2 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (* (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        3 (recur
            input
            (assoc memory (memory (+ 1 pointer)) input)
            (+ 2 pointer))
        4 (recur
            (memory (memory (+ 1 pointer)))
            memory
            (+ 2 pointer))
        5 (recur
            input
            memory
            (if (= 0 (param-mode-c instruction pointer memory))
              (+ 3 pointer)
              (param-mode-b instruction pointer memory)))
        6 (recur
            input
            memory
            (if (not= 0 (param-mode-c instruction pointer memory))
              (+ 3 pointer)
              (param-mode-b instruction pointer memory)))
        7 (recur
            input
            (if (< (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            (+ 4 pointer))
        8 (recur
            input
            (if (= (param-mode-c instruction pointer memory) (param-mode-b instruction pointer memory))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            (+ 4 pointer))))))

(def answer-2 (op-code-2 [5 tv]))

(println answer-2)

;11981754
