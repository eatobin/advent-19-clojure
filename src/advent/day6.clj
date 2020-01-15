(ns advent.day6
  (:require [clojure.string :as str]))

; Parses the input format into a map of object -> orbit center
(defn parse-orbit-map [raw]
  (apply hash-map (flatten (map (comp reverse #(str/split % #"\)")) (str/split-lines raw)))))

(def input (->>
             "resources/day6-tester-1.txt"
             (slurp)
             (parse-orbit-map)))

; Returns the number of orbits the given object is contained in (i.e. direct orbit + indirect orbits)
(defn orbit-centers [orbit-map object]
  (count (take-while some? (drop 1 (iterate orbit-map object)))))
