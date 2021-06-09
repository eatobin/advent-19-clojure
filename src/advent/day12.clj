(ns advent.day12
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as str]))

;part a

(def moon-template
  {:i {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :e {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :g {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}
   :c {:pos {:x 0, :y 0, :z 0},
       :vel {:x 0, :y 0, :z 0}}})

(def tv (let [vcs (with-open [reader (io/reader "resources/day12.csv")]
                    (doall
                      (csv/read-csv reader)))]
          (vec (map vec (map #(map str/trim %) vcs)))))



(def vcs (with-open [reader (io/reader "resources/day12.csv")]
           (doall
             (csv/read-csv reader))))

(def i (first vcs))

(zipmap [:x :y :z] (map #(Integer/parseInt %)(map #(get % 1) (map #(str/split % #"=") (map #(str/replace % ">" "") i)))))

(println tv)
