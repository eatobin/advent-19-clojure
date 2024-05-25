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
  (modules-sum "resources/day01.txt")
  :rcf)

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
  (modules-gas-sum "resources/day01.txt")
  :rcf)

;; 5003788

(defn gas-plus-lazy [module]
  (reduce + (rest (take-while pos? (iterate gas module)))))

(defn my-fn [_] 8)

(defn modules-gas-sum-lazy [file]
  (->>
   file
   (slurp)
   (str/split-lines)
   (map #(Integer/parseInt %))
   (map gas-plus-lazy)
   (reduce +)))

(comment
  (modules-gas-sum-lazy "resources/day01.txt")
  :rcf)

;; 5003788

(println "Let's test the terminal!")
