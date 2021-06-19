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
  {:i {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :e {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :g {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :c {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}})

(def moon-meld
  (atom (->
          moon-template
          (assoc-in [:i :pos] (get moon-maps 0))
          (assoc-in [:e :pos] (get moon-maps 1))
          (assoc-in [:g :pos] (get moon-maps 2))
          (assoc-in [:c :pos] (get moon-maps 3)))))

(def candidates
  [[[:i :pos :x] [:e :pos :x]]
   [[:i :pos :x] [:g :pos :x]]
   [[:i :pos :x] [:c :pos :x]]
   [[:e :pos :x] [:g :pos :x]]
   [[:e :pos :x] [:c :pos :x]]
   [[:g :pos :x] [:c :pos :x]]

   [[:i :pos :y] [:e :pos :y]]
   [[:i :pos :y] [:g :pos :y]]
   [[:i :pos :y] [:c :pos :y]]
   [[:e :pos :y] [:g :pos :y]]
   [[:e :pos :y] [:c :pos :y]]
   [[:g :pos :y] [:c :pos :y]]

   [[:i :pos :z] [:e :pos :z]]
   [[:i :pos :z] [:g :pos :z]]
   [[:i :pos :z] [:c :pos :z]]
   [[:e :pos :z] [:g :pos :z]]
   [[:e :pos :z] [:c :pos :z]]
   [[:g :pos :z] [:c :pos :z]]])

(defn moon-getter [moon]
  (get-in @moon-meld moon))

(defn moons-pair [moons]
  (vec (map moon-getter moons)))

(def all-candidates
  (vec (map moons-pair candidates)))

(defn velocity-calc [moons-atom current-moon-number axis-keyword]
  (let [next-moon-number (+ 1 (mod current-moon-number 4))
        current-moon-pos (math/abs (get-in @moons-atom [current-moon-number :pos axis-keyword]))
        next-moon-pos (math/abs (get-in @moons-atom [next-moon-number :pos axis-keyword]))]
    (cond
      (and (< next-moon-pos current-moon-pos)
           (pos-int? (get-in @moons-atom [current-moon-number :pos axis-keyword]))) (swap! moons-atom update-in [current-moon-number :vel axis-keyword] dec)
      (and (< next-moon-pos current-moon-pos)
           (not (pos-int? (get-in @moons-atom [current-moon-number :pos axis-keyword])))) (swap! moons-atom update-in [current-moon-number :vel axis-keyword] inc)
      :else [current-moon-number next-moon-number current-moon-pos next-moon-pos (pos-int? (get-in @moons-atom [current-moon-number :pos axis-keyword])) (< current-moon-pos next-moon-pos)])))

moon-meld
