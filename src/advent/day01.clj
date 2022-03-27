(ns advent.day01
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

(s/def ::module int?)
(s/def ::gas int?)

;part a
(defn gas [module]
  (- (quot module 3) 2))
(s/fdef gas
        :args (s/cat :module ::module)
        :ret ::gas)

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
    (let [new-gas (gas m)]
      (if (pos? new-gas)
        (recur
          new-gas
          (+ acc new-gas))
        acc))))
(s/fdef gas-plus
        :args (s/cat :module ::module)
        :ret ::gas)

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

(ostest/instrument)
