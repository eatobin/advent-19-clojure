(ns advent.day02b
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
       (zipmap (range))
       (into (sorted-map-by <))))

(def OFFSET-C 1)
(def OFFSET-B 2)
(def OFFSET-A 3)

(defn char-to-int [char-as-byte]
  (if (or (< char-as-byte 48)
          (> char-as-byte 57))
    "Char is not an integer"
    (- char-as-byte 48)))

(defn pad-5 [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (char-to-int (byte character)))))

(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    0 (memory (+ pointer OFFSET-A)))) ; a-p-w

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    0 (memory (memory (+ pointer OFFSET-B))))) ; b-p-r

(defn c-param [{:keys [instruction pointer memory]}]
  (case (instruction :c)
    0 (memory (memory (+ pointer OFFSET-C))))) ; c-p-r

(defn op-code [{:keys [pointer memory]}]
  (let [instruction (pad-5 (memory pointer))]
    (if (= 9 (instruction :d))
      {:pointer pointer :memory memory}
      (case (instruction :e)
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
        "Unknown opcode"))))

(def tv (make-tv "resources/day02.csv"))

(defn updated-memory [noun verb]
  (->
    tv
    (assoc 1 noun)
    (assoc 2 verb)))

(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate (((op-code {:pointer 0
                                         :memory  (updated-memory noun verb)})
                               :memory)
                              0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(defn -main
  "the main function/entry point"
  []
  (println "advent.day02a:" (last (first noun-verb))))

;8226

;;[eric@linux-epth advent-clojure](dev)$ clojure -M -m advent.day02b
