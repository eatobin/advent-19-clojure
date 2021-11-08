(ns advent.make-tv
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn make-tv [file]
  (->> (first (with-open [reader (io/reader file)]
                (doall
                  (csv/read-csv reader))))
       (map #(Long/parseLong %))
       (into [])
       (zipmap (range))
       (into (sorted-map-by <))))
