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

(max (- (quot 5 3) 2) 0)

(def gas-plus
  (fn [m]
    (loop [m m]
       (if (pos? m)
          (recur (max (- (quot m 3) 2) 0))
          m))))