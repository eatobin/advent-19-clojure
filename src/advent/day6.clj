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

(def tester "A)C
B)D
C)F
B)E
A)B")

(def orbits (->>
              tester
              (str/split-lines)
              (map #(str/split % #"\)"))
              (sort-by second)))

;; (def orbits (->>
;;               "day6.txt"
;;               (slurp)
;;               (str/split-lines)
;;               (map #(str/split % #"\)"))
;;               (sort-by second)))

(def parent-levels (sort-by first (group-by first (for [[p1 c1] orbits
                                                        [p2 c2] orbits
                                                        :when (= c1 p2)]
                                                    [c1 c2]))))


(def x (* (inc (first (first (map-indexed vector parent-levels)))) (count (second (second (first (map-indexed vector parent-levels)))))))
