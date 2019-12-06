(ns advent.core
  (:require [clojure.string :as str]))

(def modules-sum (->>
                   "modules.txt"
                   (slurp)
                   (str/split-lines)
                   (map #(Integer/parseInt %))
                   (into [])
                   (map #(- (quot % 3) 2))
                   (reduce +)))
