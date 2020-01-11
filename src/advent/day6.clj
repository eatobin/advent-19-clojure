(ns advent.day6
  (:require [clojure.string :as str]))

(def tester "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L")

;; (def tester "COM)C
;; B)D
;; C)F
;; B)E
;; COM)B")

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

(def my-parents (group-by first (for [[i1 o1] orbits
                                      [i2 o2] orbits
                                      :when (= o1 i2)]
                                  [o1 o2])))


(def branches (for [[k v] my-parents
                    :when (> (count v) 1)]
                [k v]))

(def branch-count (count branches))
