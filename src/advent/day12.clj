(ns advent.day12
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as str]))

;part a
(def vcs (with-open [reader (io/reader "resources/day12a.csv")]
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
  {:i {:name :i,
       :pos  {:x 0, :y 0, :z 0},
       :vel  {:x 0, :y 0, :z 0}}
   :e {:name :e,
       :pos  {:x 0, :y 0, :z 0},
       :vel  {:x 0, :y 0, :z 0}}
   :g {:name :g,
       :pos  {:x 0, :y 0, :z 0},
       :vel  {:x 0, :y 0, :z 0}}
   :c {:name :c,
       :pos  {:x 0, :y 0, :z 0},
       :vel  {:x 0, :y 0, :z 0}}})

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
  [(moon 0) (moon 2) (get-in @moon-meld moon)])

(defn moons-pair [moons]
  (vec (map moon-getter moons)))

(def all-candidates
  (vec (map moons-pair candidates)))



(defn gravity-update [all-candidates]
  (for [[moon-vec-0 moon-vec-1] all-candidates
        :let [moon-pos-0 (get moon-vec-0 2)
              moon-pos-1 (get moon-vec-1 2)
              moon-0 (get moon-vec-0 0)
              moon-1 (get moon-vec-1 0)
              axis (get moon-vec-0 1)
              moon-0-velocity (cond (> moon-pos-0 moon-pos-1) -1
                                    (> moon-pos-1 moon-pos-0) 1
                                    :else 0)
              moon-1-velocity (cond (> moon-pos-1 moon-pos-0) -1
                                    (> moon-pos-0 moon-pos-1) 1
                                    :else 0)]]
    (do (swap! moon-meld update-in [moon-0 :vel axis] + moon-0-velocity)
        (swap! moon-meld update-in [moon-1 :vel axis] + moon-1-velocity))))

(gravity-update all-candidates)

;(doseq [moon-map @moon-meld
;        :let [[[_ _ _] [x-vel y-vel z-vel]] moon-map]]
;  (swap! moon-map update-in [0 0] + x-vel)
;  (swap! moon-map update-in [0 1] + y-vel)
;  (swap! moon-map update-in [0 2] + z-vel))

;(doseq [moon-map @moon-meld
;        :let [{name :name, {x-vel :x, y-vel :y, z-vel :z} :vel} moon-map]]
;  (swap! moon-meld update-in [name :pos :x] + x-vel)
;  (swap! moon-meld update-in [name :pos :y] + y-vel)
;  (swap! moon-meld update-in [name :pos :z] + z-vel))

(doseq [[_ {name :name {x-vel :x, y-vel :y, z-vel :z} :vel}] @moon-meld]
  (swap! moon-meld update-in [name :pos :x] + x-vel)
  (swap! moon-meld update-in [name :pos :y] + y-vel)
  (swap! moon-meld update-in [name :pos :z] + z-vel))
