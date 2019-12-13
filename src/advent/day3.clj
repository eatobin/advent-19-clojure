(ns advent.day3
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def both (with-open [reader (io/reader "paths.csv")]
            (doall
              (csv/read-csv reader))))
(def red (->> (first both)
              (into [])))

(def blue (->> (second both)
               (into [])))


(defn direction [unit]
  (subs unit 0 1))

(defn distance [unit]
  (Integer/parseInt (subs unit 1)))
