(ns advent.day5
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "day5.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def ex1 [3 0 4 0 99])
(def ex2 [3 0 104 63 99])
(def ex3 [1002 4 3 4 33])

(defn op-code [input memory]
  (loop [pointer 0
         memory memory
         exit-code nil]
    (let [instruction (memory (+ 0 pointer))]
      (case instruction
        99 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            0)
        101 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              0)
        1001 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               0)
        1101 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               0)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            0)
        102 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              0)
        1002 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               0)
        1102 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               0)
        3 (recur
            (+ 2 pointer)
            (assoc memory (memory (+ 1 pointer)) input)
            0)
        4 (recur
            (+ 2 pointer)
            memory
            (memory (memory (+ 1 pointer))))
        104 (recur
              (+ 2 pointer)
              memory
              (memory (+ 1 pointer)))))))

(def answer (op-code 1 tv))

;9025675
