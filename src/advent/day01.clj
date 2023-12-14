;; $ clj -X:repl/socket-repl **Use this one for printing to the server REPL

(ns advent.day01
  (:require [clojure.string :as str]))

;part a
(defn gas [module]
  (- (quot module 3) 2))

(defn modules-sum [file]
  (->>
    file
    (slurp)
    (str/split-lines)
    (map #(Integer/parseInt %))
    (into [])
    (map gas)
    (reduce +)))

(comment
  (modules-sum "resources/day01.txt"))

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

(defn modules-gas-sum [file]
  (->>
    file
    (slurp)
    (str/split-lines)
    (map #(Integer/parseInt %))
    (into [])
    (map gas-plus)
    (reduce +)))

(comment
  (modules-gas-sum "resources/day01.txt"))

;; 5003788
