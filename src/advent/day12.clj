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
    (into [])))

(def moon-maps (into [] (map make-moon-map vcs)))

(def moon-meld
  [(atom [[0 0 0] [0 0 0]])
   (atom [[0 0 0] [0 0 0]])
   (atom [[0 0 0] [0 0 0]])
   (atom [[0 0 0] [0 0 0]])])

(do
  (swap! (get moon-meld 0) assoc-in [0] (get moon-maps 0))
  (swap! (get moon-meld 1) assoc-in [0] (get moon-maps 1))
  (swap! (get moon-meld 2) assoc-in [0] (get moon-maps 2))
  (swap! (get moon-meld 3) assoc-in [0] (get moon-maps 3)))

(def candidates
  [[[0 0 0] [1 0 0]]
   [[0 0 0] [2 0 0]]
   [[0 0 0] [3 0 0]]
   [[1 0 0] [2 0 0]]
   [[1 0 0] [3 0 0]]
   [[2 0 0] [3 0 0]]

   [[0 0 1] [1 0 1]]
   [[0 0 1] [2 0 1]]
   [[0 0 1] [3 0 1]]
   [[1 0 1] [2 0 1]]
   [[1 0 1] [3 0 1]]
   [[2 0 1] [3 0 1]]

   [[0 0 2] [1 0 2]]
   [[0 0 2] [2 0 2]]
   [[0 0 2] [3 0 2]]
   [[1 0 2] [2 0 2]]
   [[1 0 2] [3 0 2]]
   [[2 0 2] [3 0 2]]])

(defn moon-getter [[moon _ axis]]
  [(get-in @(get moon-meld moon) [0 axis]) moon axis])

(defn moons-pair [moons]
  (vec (map moon-getter moons)))

(def all-candidates
  (vec (map moons-pair candidates)))

;(defn gravity-update [all-candidates]
;  (for [[moon-vec-0 moon-vec-1] all-candidates
;        :let [moon-pos-0 (get moon-vec-0 0)
;              moon-pos-1 (get moon-vec-1 0)
;              moon-0 (get moon-vec-0 1)
;              moon-1 (get moon-vec-1 1)
;              axis (get moon-vec-0 2)
;              moon-0-velocity (cond (> moon-pos-0 moon-pos-1) -1
;                                    (> moon-pos-1 moon-pos-0) 1
;                                    :else 0)
;              moon-1-velocity (cond (> moon-pos-1 moon-pos-0) -1
;                                    (> moon-pos-0 moon-pos-1) 1
;                                    :else 0)]]
;    (do (swap! moon-meld update-in [moon-0 1 axis] + moon-0-velocity)
;        (swap! moon-meld update-in [moon-1 1 axis] + moon-1-velocity))))
;
;(gravity-update all-candidates)
