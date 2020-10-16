(ns advent.day01
  (:require [clojure.string :as str]))

;part a
(defn gas [m]
  (- (quot m 3) 2))

(def modules-sum (->>
                   "resources/day01.txt"
                   (slurp)
                   (str/split-lines)
                   (map #(Integer/parseInt %))
                   (into [])
                   (map gas)
                   (reduce +)))

(println modules-sum)

;; 3337766

;part b
(defn gas-plus [module]
  (loop [m module
         acc 0]
    (let [new-gas (max (- (quot m 3) 2) 0)]
      (if (pos? new-gas)
        (recur
          new-gas
          (+ acc new-gas))
        acc))))

(def modules-gas-sum (->>
                       "resources/day01.txt"
                       (slurp)
                       (str/split-lines)
                       (map #(Integer/parseInt %))
                       (into [])
                       (map gas-plus)
                       (reduce +)))

(println modules-gas-sum)

;; 5003788
