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

(def tester "COM)B
COM)C
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

(defn parent?
  [v coll]
  (some #(= % v) coll))

(loop [orbits [[:com :a] [:com :b] [:a :y] [:b :me] [:com :j] [:me :you]]
       parents [:com]
       kids []]
  (if (empty? orbits)
    parents
    (recur
      (rest orbits)
      (if (parent? (first (first orbits)) parents)
        (conj parents (second (first orbits)))
        parents))))

(loop [orbits []
       generation 0
       kids []
       parents [:com]]
  (if (empty? orbits)
    [[generation parents]]
    (let [new-kids (if (parent? (first (first orbits)) parents)
                     (conj parents (second (first orbits)))
                     parents)])))
