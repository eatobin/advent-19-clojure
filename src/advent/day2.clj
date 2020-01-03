(ns advent.day2
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "memory.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(defn op-code [memory]
  (loop [pointer 0
         memory memory]
    (let [instruction (memory (+ 0 pointer))]
      (case instruction
        99 memory
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))))))

(def fix-op-code (first (op-code (updated-memory 12 2))))

;2890696

;part b
(def noun-verb
  (for [noun (range 0 100)
        verb (range 0 100)
        :let [candidate (first (op-code (updated-memory noun verb)))]
        :when (= candidate 19690720)]
    [candidate noun verb (+ (* 100 noun) verb)]))

;[19690720 82 26 8226]
