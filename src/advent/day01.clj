(ns advent.day01
  (:require [advent.domain :as dom]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [orchestra.spec.test :as ostest]))

;part a
(defn gas [module]
  (- (quot module 3) 2))
(s/fdef gas
        :args (s/cat :module ::dom/module)
        :ret ::dom/gas)

(def modules-sum (->>
                   "resources/day01.txt"
                   (slurp)
                   (str/split-lines)
                   (map #(Integer/parseInt %))
                   (into [])
                   (map gas)
                   (reduce +)))

(println modules-sum)

(defn fuel [x]
  (max 0
       (- (Math/floor (/ x 3)) 2)))

(def modules-sum-je (->>
                      "resources/day01.txt"
                      (slurp)
                      (str/split-lines)
                      (map #(Integer/parseInt %))
                      (into [])
                      (map fuel)
                      (reduce +)
                      (int)))

(println modules-sum-je)

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
        :args (s/cat :module ::dom/module)
        :ret ::dom/gas)

(def modules-gas-sum (->>
                       "resources/day01.txt"
                       (slurp)
                       (str/split-lines)
                       (map #(Integer/parseInt %))
                       (into [])
                       (map gas-plus)
                       (reduce +)))

(println modules-gas-sum)

(defn total-fuel [x]
  (apply + (take-while pos? (next (iterate fuel x)))))

(def modules-gas-sum-je (->>
                          "resources/day01.txt"
                          (slurp)
                          (str/split-lines)
                          (map #(Integer/parseInt %))
                          (into [])
                          (map total-fuel)
                          (reduce +)
                          (int)))

(println modules-gas-sum-je)

;; 5003788

(int (reduce + (map total-fuel (map read-string (clojure.string/split-lines (slurp "resources/day01.txt"))))))

(ostest/instrument)
