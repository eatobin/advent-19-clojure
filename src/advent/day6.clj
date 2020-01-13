(ns advent.day6
  (:require [clojure.string :as str]))

;(def tester "COM)B
;B)C
;C)D
;D)E
;E)F
;B)G
;G)H
;D)I
;E)J
;J)K
;K)L")

(def tester "A)B
A)C
B)D
B)E
C)F")

(def orbits (->>
              tester
              (str/split-lines)
              (map #(str/split % #"\)"))
              (vec)))

;; (def orbits (->>
;;               "day6.txt"
;;               (slurp)
;;               (str/split-lines)
;;               (map #(str/split % #"\)"))
;;               (sort-by second)))

(loop [orbits [["A" "B"] ["A" "C"]]
       generation 1
       sibling "A"
       kids []]
  (if (empty? orbits)
    [generation kids]
    (recur
      (rest orbits)
      (if (= (first (first orbits)) sibling)
        generation
        (inc generation))
      (first (first orbits))
      (if (= (first (first orbits)) sibling)
        (conj kids (second (first orbits)))
        kids))))
