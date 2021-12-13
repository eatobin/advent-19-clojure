(ns advent.day05
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
       (into [])))

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

(defn get-or-else [pointer OFFSET-X memory]
  (if (> (+ pointer OFFSET-X)
         (- (count memory) 1))
    0
    (memory (memory (+ pointer OFFSET-X)))))

(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    ; a-p-w
    0 (memory (+ pointer OFFSET-A))))

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    ; b-p-r
    0 (get-or-else pointer OFFSET-B memory)
    ; b-i-r
    1 (memory (+ pointer OFFSET-B))))

;(defn c-param [{:keys [instruction pointer memory]}]
;  (case (instruction :e)
;    3 (case (instruction :c)
;        ; c-p-w
;        0 (memory (+ pointer OFFSET-C)))
;    (case (instruction :c)
;      ; c-p-r
;      0 (get-or-else pointer OFFSET-C memory)
;      ; c-i-r
;      1 (memory (+ pointer OFFSET-C)))))

(defn c-param [{:keys [instruction pointer memory]}]
  (if (= 3 (instruction :e))
    (case (instruction :c)
      ; c-p-w
      0 (memory (+ pointer OFFSET-C)))
    (case (instruction :c)
      ; c-p-r
      0 (get-or-else pointer OFFSET-C memory)
      ; c-i-r
      1 (memory (+ pointer OFFSET-C)))))

(defn op-code [{:keys [input output pointer memory]}]
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      9 {:input input :output output :pointer pointer :memory memory}
      1 (recur
          {:input   input
           :output  output
           :pointer (+ 4 pointer)
           :memory  (assoc
                      memory
                      (a-param {:instruction instruction :pointer pointer :memory memory})
                      (+ (c-param {:instruction instruction :pointer pointer :memory memory})
                         (b-param {:instruction instruction :pointer pointer :memory memory})))})
      2 (recur
          {:input   input
           :output  output
           :pointer (+ 4 pointer)
           :memory  (assoc
                      memory
                      (a-param {:instruction instruction :pointer pointer :memory memory})
                      (* (c-param {:instruction instruction :pointer pointer :memory memory})
                         (b-param {:instruction instruction :pointer pointer :memory memory})))})
      3 (recur
          {:input   input
           :output  output
           :pointer (+ 2 pointer)
           :memory  (assoc
                      memory
                      (c-param {:instruction instruction :pointer pointer :memory memory})
                      input)})
      4 (recur
          {:input   input
           :output  (c-param {:instruction instruction :pointer pointer :memory memory})
           :pointer (+ 2 pointer)
           :memory  memory})
      5 (recur
          {:input   input
           :output  output
           :pointer (if (= 0 (c-param {:instruction instruction :pointer pointer :memory memory}))
                      (+ 3 pointer)
                      (b-param {:instruction instruction :pointer pointer :memory memory}))
           :memory  memory})
      6 (recur
          {:input   input
           :output  output
           :pointer (if (not= 0 (c-param {:instruction instruction :pointer pointer :memory memory}))
                      (+ 3 pointer)
                      (b-param {:instruction instruction :pointer pointer :memory memory}))
           :memory  memory})
      7 (recur
          {:input   input
           :output  output
           :pointer (+ 4 pointer)
           :memory  (if (< (c-param {:instruction instruction :pointer pointer :memory memory})
                           (b-param {:instruction instruction :pointer pointer :memory memory}))
                      (assoc
                        memory
                        (a-param {:instruction instruction :pointer pointer :memory memory})
                        1)
                      (assoc
                        memory
                        (a-param {:instruction instruction :pointer pointer :memory memory})
                        0))})
      8 (recur
          {:input   input
           :output  output
           :pointer (+ 4 pointer)
           :memory  (if (= (c-param {:instruction instruction :pointer pointer :memory memory})
                           (b-param {:instruction instruction :pointer pointer :memory memory}))
                      (assoc
                        memory
                        (a-param {:instruction instruction :pointer pointer :memory memory})
                        1)
                      (assoc
                        memory
                        (a-param {:instruction instruction :pointer pointer :memory memory})
                        0))})
      "Unknown opcode")))

;part a
(def tv (make-tv "resources/day05.csv"))

(def answer ((op-code {:input   1
                       :output  nil
                       :pointer 0
                       :memory  tv})
             :output))

(println answer) answer

;9025675

;part b
(def answer-2 ((op-code {:input   5
                         :output  nil
                         :pointer 0
                         :memory  tv})
               :output))

(println answer-2)

;11981754
