;From:
;https://github.com/jkoenig134/AdventOfCode-2019

;part 1

(ns advent.day6
  (:require [clojure.string :as str]))

; Parses the input format into a map of object -> orbit center
(defn parse-orbit-map [raw]
  (apply hash-map (flatten (map (comp reverse #(str/split % #"\)")) (str/split-lines raw)))))

(def input (->>
             "resources/day6.txt"
             (slurp)
             (parse-orbit-map)))

; Returns the number of orbits the given object is contained in (i.e. direct orbit + indirect orbits)
(defn orbit-centers [orbit-map object]
  (count (take-while some? (drop 1 (iterate orbit-map object)))))

; Returns the sum of the orbit centers for each object in the orbit map.
(defn direct-and-indirect-orbits [orbit-map]
  (apply + (map (partial orbit-centers orbit-map) (keys orbit-map))))

(println "Total number of (in)direct orbits:" (direct-and-indirect-orbits input))

;241064
