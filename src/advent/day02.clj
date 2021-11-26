(ns advent.day02
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

; ABCDE
;  1002

; a- b- or c- = left-to-right position after 2 digit opcode
; -p- -i- or -r- = position, immediate or relative mode
; -r or -w = read or write

(defn make-tv [file]
  (->> (first (with-open [reader (io/reader file)]
                (doall
                  (csv/read-csv reader))))
       (map #(Long/parseLong %))
       (into [])
       (zipmap (range))))

(def OFFSET-C 1)
(def OFFSET-B 2)
(def OFFSET-A 3)

(defn pad-5 [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (- (byte character) 48))))

(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    ; a-p-w
    0 (memory (+ pointer OFFSET-A))))

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    ; b-p-r
    0 (get memory (memory (+ pointer OFFSET-B)) 0)))

(defn c-param [{:keys [instruction pointer memory]}]
  (case (instruction :c)
    ; c-p-r
    0 (get memory (memory (+ pointer OFFSET-C)) 0)))

(defn op-code [{:keys [pointer memory]}]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 {:pointer pointer :memory memory}
      1 (recur
          {:pointer (+ 4 pointer)
           :memory  (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory})
                                  (+ (c-param {:instruction instruction :pointer pointer :memory memory})
                                     (b-param {:instruction instruction :pointer pointer :memory memory})))})
      2 (recur
          {:pointer (+ 4 pointer)
           :memory  (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory})
                                  (* (c-param {:instruction instruction :pointer pointer :memory memory})
                                     (b-param {:instruction instruction :pointer pointer :memory memory})))})
      "Unknown opcode")))

;part a
(def tv (make-tv "resources/day02.csv"))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(def answer
  (((op-code {:pointer 0
              :memory  (updated-memory 12 2)})
    :memory)
   0))

(println answer)

;2890696

;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate (((op-code {:pointer 0
                                         :memory  (updated-memory noun verb)})
                               :memory)
                              0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(println (last (first noun-verb)))

;8226
