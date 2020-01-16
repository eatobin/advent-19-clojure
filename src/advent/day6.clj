(ns advent.day6
  (:require [clojure.string :as str]))

;part 1

;From:
;https://github.com/jkoenig134/AdventOfCode-2019

(defn parse-orbit-map
  "Parses the input format into a map of object -> orbit center"
  [raw]
  (apply hash-map (flatten (map (comp reverse #(str/split % #"\)")) (str/split-lines raw)))))

(def input (->>
             "resources/day6.txt"
             (slurp)
             (parse-orbit-map)))

(defn orbit-centers
  "Returns the number of orbits the given object is contained in (i.e. direct orbit + indirect orbits)"
  [orbit-map object]
  (count (take-while some? (drop 1 (iterate orbit-map object)))))

(defn direct-and-indirect-orbits
  "Returns the sum of the orbit centers for each object in the orbit map."
  [orbit-map]
  (apply + (map (partial orbit-centers orbit-map) (keys orbit-map))))

(println "Total number of (in)direct orbits:" (direct-and-indirect-orbits input))

;241064

;part 2

(def input-2 (->>
               "resources/santa-advent.txt"
               (slurp)
               (parse-orbit-map)))

(loop [you-path (take-while some? (drop 2 (iterate input-2 "YOU")))
       santa-path (take-while some? (drop 1 (iterate input-2 "SAN")))]
  (if (some #(= (first you-path) %) santa-path)
    true
    (recur
      (rest you-path)
      santa-path)))
