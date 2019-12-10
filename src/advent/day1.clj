(ns advent.day1
  (:require [clojure.string :as str]))

;part a
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
                       "modules.txt"
                       (slurp)
                       (str/split-lines)
                       (map #(Integer/parseInt %))
                       (into [])
                       (map gas-plus)
                       (reduce +)))

;; 5003788
