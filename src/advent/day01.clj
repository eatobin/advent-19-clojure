(ns advent.day01
  (:require [advent.domain :as dom]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

;part a
(defn fuel [module]
  (max
    0
    (- (quot module 3) 2)))
(s/fdef fuel
        :args (s/cat :module ::dom/module)
        :ret ::dom/fuel)

(def modules-sum (->>
                   "resources/day01.txt"
                   (slurp)
                   (str/split-lines)
                   (map read-string)
                   (map fuel)
                   (reduce +)))

(println modules-sum)

;; 3337766

;part b
(defn fuel-plus [x]
  (apply + (take-while pos? (next (iterate fuel x)))))
(s/fdef fuel-plus
        :args (s/cat :module ::dom/module)
        :ret ::dom/fuel)

(def modules-fuel-sum (->>
                        "resources/day01.txt"
                        (slurp)
                        (str/split-lines)
                        (map read-string)
                        (map fuel-plus)
                        (reduce +)))

(println modules-fuel-sum)

;; 5003788

(ostest/instrument)
