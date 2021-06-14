(ns advent.day12
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as str]))

;part a
(def vcs (with-open [reader (io/reader "resources/day12.csv")]
           (doall
             (csv/read-csv reader))))

(defn make-moon-map [moon-pos]
  (->>
    moon-pos
    (map #(str/replace % ">" ""))
    (map #(str/split % #"="))
    (map #(get % 1))
    (map #(Integer/parseInt %))
    (zipmap [:x :y :z])))

(def moon-maps (into [] (map make-moon-map vcs)))

(def moon-template
  {0 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   1 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   2 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   3 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}})

(def moon-meld
  (atom (->
          moon-template
          (assoc-in [0 :pos] (get moon-maps 0))
          (assoc-in [1 :pos] (get moon-maps 1))
          (assoc-in [2 :pos] (get moon-maps 2))
          (assoc-in [3 :pos] (get moon-maps 3)))))

moon-meld
