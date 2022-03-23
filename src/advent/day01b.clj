(ns advent.day01b
  (:require [advent.day01a :as day01a]
            [clojure.string :as str]
            [malli.instrument :as mi]))

(def =>fuel-plus [:=> [:cat :int] :int])

;; part b
(defn fuel-plus
  "calculate the recursive fuel required for a module"
  {:malli/schema =>fuel-plus}
  [module]
  (loop [m module
         acc 0]
    (let [new-gas (day01a/fuel m)]
      (if (pos? new-gas)
        (recur
          new-gas
          (+ acc new-gas))
        acc))))

(defn -main
  "the main function/entry point"
  []
  (->>
    "resources/day01.txt"
    (slurp)
    (str/split-lines)
    (map #(Integer/parseInt %))
    (into [])
    (map fuel-plus)
    (reduce +)
    (println "advent.day01b:")))                           ;5003788

(mi/collect!)
(mi/instrument!)
;;[eric@linux-epth advent-clojure](dev)$ clojure -M -m advent.day01b
