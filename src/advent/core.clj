(ns advent.core
  (:require [clojure.string :as str]))

(def modules-sum (->>
                   "modules.txt"
                   (slurp)
                   (str/split-lines)
                   (map #(Integer/parseInt %))
                   (into [])
                   (map #(- (quot % 3) 2))
                   (reduce +)))

(max (- (quot 5 3) 2) 0)

;; (def gas-plus
;;   (fn [m]
;;     (loop [m m]
;;        (if (pos? m)
;;           (recur (max (- (quot m 3) 2) 0))
;;           m))))

(defn gas-plus [module]
  (loop [m module
         acc module]
    (let [new-gas (max (- (quot m 3) 2) 0)]
      (if (pos? new-gas)
        (recur
          new-gas
          (+ acc new-gas))
        acc))))
