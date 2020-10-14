(ns eatobin.advent.day12
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))

;part a
(def tv (let [vcs (with-open [reader (io/reader "resources/day12.csv")]
                    (doall
                      (csv/read-csv reader)))]
          (vec (map vec (map #(map str/trim %) vcs)))))
