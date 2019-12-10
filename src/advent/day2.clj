(ns advent.day2
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "ic.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def update-pos
  (->
    tv
    (assoc 1 12)
    (assoc 2 2)))

(defn int-code [ic]
  (loop [offset 0
         ic ic]
    (let [ops (ic (+ 0 offset))]
      (case ops
        99 ic
        1 (recur
            (+ 4 offset)
            (assoc ic (ic (+ 3 offset)) (+ (ic (ic (+ 1 offset))) (ic (ic (+ 2 offset))))))
        2 (recur
            (+ 4 offset)
            (assoc ic (ic (+ 3 offset)) (* (ic (ic (+ 1 offset))) (ic (ic (+ 2 offset))))))))))

(def fix-int-code (first (int-code update-pos)))

;2890696

;part b
