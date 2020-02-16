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

(defn pos-c [pointer memory]
  (memory (memory (+ 1 pointer))))

(defn pos-b [pointer memory]
  (memory (memory (+ 2 pointer))))

(defn imm-a [pointer memory]
  (memory (+ 3 pointer)))

(defn op-code [memory]
  (loop [memory memory
         pointer 0]
    (case (memory pointer)
      99 memory
      1 (recur
          (assoc memory (imm-a pointer memory) (+ (pos-c pointer memory) (pos-b pointer memory)))
          (+ 4 pointer))
      2 (recur
          (assoc memory (imm-a pointer memory) (* (pos-c pointer memory) (pos-b pointer memory)))
          (+ 4 pointer)))))

(def fix-op-code (first (op-code (updated-memory 12 2))))

(println fix-op-code)

;2890696

;part b
(def noun-verb
  (for [noun (range 0 100)
        verb (range 0 100)
        :let [candidate (first (op-code (updated-memory noun verb)))]
        :when (= candidate 19690720)]
    [candidate noun verb (+ (* 100 noun) verb)]))

(println noun-verb)

;[19690720 82 26 8226]
