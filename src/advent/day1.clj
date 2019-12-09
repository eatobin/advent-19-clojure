(ns advent.day1
  (:require [clojure.string :as str]))

(defn gas [m]
  (- (quot m 3) 2))

(def modules-sum (->>
                  "modules.txt"
                  (slurp)
                  (str/split-lines)
                  (map #(Integer/parseInt %))
                  (into [])
                  (map gas)
                  (reduce +)))
