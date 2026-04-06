(ns eatobin.library
  (:require
   [clojure.string :as str]))

(defn make-instruction [integer]
  (into (sorted-map)
        (zipmap [:a :b :c :d :e]
                (for [character (format "%05d" integer)]
                  (- (byte character) 48)))))

(defn make-memory [memory-as-csv-string]
  (->>
   (str/split memory-as-csv-string #",")
   (map #(Long/parseLong %))
   (into [])
   (zipmap (range))
   (into (sorted-map))))

(comment
  (make-memory "10,11,1"))
