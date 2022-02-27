(ns advent.day01a
  (:require [clojure.string :as str]
            [malli.instrument :as mi]))

;; part a
(defn fuel
  "calculate the fuel required for a module"
  {:malli/schema [:=> [:cat :int] :int]}
  [module]
  (- (quot module 3) 2))

(defn -main
  "the main function/entry point"
  []
  (->>
    "resources/day01.txt"
    (slurp)
    (str/split-lines)
    (map #(Integer/parseInt %))
    (into [])
    (map fuel)
    (reduce +)
    (println "advent.day01a: ")))                           ;3337766

(mi/collect!)
(mi/instrument!)
;;[eric@linux-epth advent-clojure](dev)$ clojure -M -m advent.day01a
