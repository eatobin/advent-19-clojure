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

(def orbits (->>
              tester
              (str/split-lines)
              (map #(str/split % #"\)"))))
