(ns eatobin.library
  (:require
   [clojure.data.int-map :as i]
   [clojure.string :as str]))

;; {:input input :output output :pointer pointer :memory memory}

(def pointer-offset-c 1)
(def pointer-offset-b 2)
(def pointer-offset-a 3)

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
   (into (i/int-map))))

(defn lookup-abc [intcode pointer-offset-x]
  (get (:memory intcode)
       (+ (:pointer intcode) pointer-offset-x)))

(defn p-w [intcode pointer-offset-x]
  (lookup-abc intcode pointer-offset-x))

(defn p-r [intcode pointer-offset-x]
  (get (:memory intcode)
       (lookup-abc intcode pointer-offset-x)))
