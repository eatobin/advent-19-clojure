(ns advent.day10
  (:require [clojure.string :as str]))

;part a
(def universe (->>
                "resources/day10a.txt"
                (slurp)
                (str/split-lines)
                (into [])
                (map vec)
                (into [])))
