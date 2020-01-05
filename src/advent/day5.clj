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
(def tester-2 [3 0 4 0 1101])

(defn op-code [input memory]
  (loop [pointer 0
         memory memory
         exit-code 999]
    (let [instruction (memory (+ 0 pointer))]
      (case instruction
        1101 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            999)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            999)
        3 (recur
            (+ 2 pointer)
            (assoc memory (memory (inc pointer)) input)
            999)
        4 (recur
            (+ 2 pointer)
            memory
            (memory (inc pointer)))))))

(defn explode [num]
  (vec (for [n (format "%05d" num)]
         (- (byte n) 48))))

(defn op [number]
  (->
    number
    explode
    (last)))
