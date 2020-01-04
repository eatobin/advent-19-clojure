(ns advent.day5
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "day5.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def tester [3 7 1 7 6 6 1100 0])

(defn op-code [input memory]
  (loop [pointer 0
         memory memory
         exit-code 999]
    (let [instruction (memory (+ 0 pointer))]
      (case instruction
        1101 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))
        3 (recur
            (+ 2 pointer)
            (assoc memory (memory (inc pointer)) input))))))

(defn explode-2 [num]
  (for [n (format "%05d" num)]
    (- (byte n) 48)))
