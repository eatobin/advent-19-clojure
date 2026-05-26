(ns eatobin.day07
  (:require
   [clojure.data.int-map :as i]
   [clojure.string :as str]
   [eatobin.intcode :as ic]))

(defn make-memory [memory-as-csv-string]
  (->>
   (str/split memory-as-csv-string #",")
   (map #(Long/parseLong %))
   (into [])
   (zipmap (range))
   (into (i/int-map))))

(def memory-as-csv-string "3,8,1001,8,10,8,105,1,0,0,21,38,55,72,93,118,199,280,361,442,99999,3,9,1001,9,2,9,1002,9,5,9,101,4,9,9,4,9,99,3,9,1002,9,3,9,1001,9,5,9,1002,9,4,9,4,9,99,3,9,101,4,9,9,1002,9,3,9,1001,9,4,9,4,9,99,3,9,1002,9,4,9,1001,9,4,9,102,5,9,9,1001,9,4,9,4,9,99,3,9,101,3,9,9,1002,9,3,9,1001,9,3,9,102,5,9,9,101,4,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,99,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,99,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,99")

;part a
(def memory (make-memory memory-as-csv-string))

(def possibles (into ()
                     (reverse
                      (for [a (range 0 5)
                            b (range 0 5)
                            c (range 0 5)
                            d (range 0 5)
                            e (range 0 5)
                            :when (distinct? a b c d e)]
                        [a b c d e]))))

(defn grab-my-input-from-prior-output [index this-pass-map]
  (if (= index 1)
    (assoc-in this-pass-map [index :input] 0)
    (assoc-in this-pass-map [index :input] (last (get-in this-pass-map [(dec index) :output])))))

(defn run-my-output-from-my-input [index this-pass-map]
  (assoc
   this-pass-map
   index
   (ic/op-code (get this-pass-map index))))

(defn grab-and-run [index this-pass-map]
  (->>
   this-pass-map
   (grab-my-input-from-prior-output index)
   (run-my-output-from-my-input index)))

(defn pass [[a b c d e]]
  (loop [index         1
         this-pass-map {1 {:input 0 :output [] :phase a :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}
                        2 {:input 0 :output [] :phase b :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}
                        3 {:input 0 :output [] :phase c :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}
                        4 {:input 0 :output [] :phase d :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}
                        5 {:input 0 :output [] :phase e :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? true}}]
    (if (get-in this-pass-map [5 :stopped?])
      (first (get-in this-pass-map [5 :output]))
      (recur
       (inc (mod index 5))
       (grab-and-run index this-pass-map)))))

(defn passes []
  (map #(pass %) possibles))

(def answer (apply max (passes)))

(comment
  answer
  368584
  :rcf)

