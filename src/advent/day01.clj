;; $ clj -X:repl/socket-repl **Use this one for printing to the server REPL

(ns advent.day01
  (:require [clojure.string :as str]))

;part a
(defn gas [module]
  (- (quot module 3) 2))

(def modules-sum (->>
                  "resources/day01.txt"
                  (slurp)
                  (str/split-lines)
                  (map #(Integer/parseInt %))
                  (into [])
                  (map gas)
                  (reduce +)))

(comment modules-sum)

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

(def modules-gas-sum (->>
                      "resources/day01.txt"
                      (slurp)
                      (str/split-lines)
                      (map #(Integer/parseInt %))
                      (into [])
                      (map gas-plus)
                      (reduce +)))

(comment modules-gas-sum)

;; 5003788
