(ns advent.day2
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "memory.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def updated-address
  (->
    tv
    (assoc 1 12)
    (assoc 2 2)))

(defn int-code [memory]
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

(def fix-int-code (first (int-code updated-address)))

;2890696

;part b
