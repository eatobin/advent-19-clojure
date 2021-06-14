(ns advent.day12
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as str]
    [clojure.math.numeric-tower :as math]))

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
  {1 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   2 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   3 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}
   4 {:pos {:x 0, :y 0, :z 0},
      :vel {:x 0, :y 0, :z 0}}})

(def moon-meld
  (atom (->
          moon-template
          (assoc-in [1 :pos] (get moon-maps 0))
          (assoc-in [2 :pos] (get moon-maps 1))
          (assoc-in [3 :pos] (get moon-maps 2))
          (assoc-in [4 :pos] (get moon-maps 3)))))

(defn velocity-calc [moons-atom axis-keyword current-moon-number]
  (let [next-moon-number (+ 1 (mod current-moon-number 4))
        current-moon-pos (math/abs (get-in @moons-atom [current-moon-number :pos axis-keyword]))
        next-moon-pos (math/abs (get-in @moons-atom [next-moon-number :pos axis-keyword]))]
    (cond
      (< current-moon-pos next-moon-pos) (do (swap! moons-atom assoc-in [current-moon-number :vel axis-keyword] 1)
                                             (swap! moons-atom assoc-in [next-moon-number :vel axis-keyword] -1))
      (> current-moon-pos next-moon-pos) (do (swap! moons-atom assoc-in [current-moon-number :vel axis-keyword] -1)
                                             (swap! moons-atom assoc-in [next-moon-number :vel axis-keyword] 1))
      :else @moons-atom)))

moon-meld
