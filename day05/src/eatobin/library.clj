(ns eatobin.library
  (:require
   [clojure.string :as str]))

(defn char-to-int [char-as-byte]
  (- char-as-byte 48))

(defn make-instruction [integer]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" integer)]
            (char-to-int (byte character)))))

(defn make-memory [memory-as-csv-string]
  (->>
   (str/split memory-as-csv-string #",")
   (map #(Long/parseLong %))
   (into [])
   (zipmap (range))
   (into (sorted-map))))

(comment
  (make-memory "10,11,1"))
