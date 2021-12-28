(ns advent.day09
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

(defn get-or-else [pointer OFFSET-X relative-base memory]
  (if (> (+ (memory (+ pointer OFFSET-X)) relative-base)
         (- (count memory) 1))
    0
    (memory (+ (memory (+ pointer OFFSET-X)) relative-base))))

(defn a-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :a)
    ; a-p-w
    0 (memory (+ pointer OFFSET-A))
    ; a-r-w
    2 (+ (memory (+ pointer OFFSET-A)) relative-base)))

(defn b-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :b)
    ; b-p-r
    0 (get-or-else pointer OFFSET-B 0 memory)
    ; b-i-r
    1 (memory (+ pointer OFFSET-B))
    ; b-r-r
    2 (get-or-else pointer OFFSET-B relative-base memory)))

(defn c-param [{:keys [instruction pointer memory relative-base]}]
  (if (= 3 (instruction :e))
    (case (instruction :c)
      ; c-p-w
      0 (memory (+ pointer OFFSET-C))
      ; c-r-w
      2 (+ (memory (+ pointer OFFSET-C)) relative-base))
    (case (instruction :c)
      ; c-p-r
      0 (get-or-else pointer OFFSET-C 0 memory)
      ; c-i-r
      1 (memory (+ pointer OFFSET-C))
      ; c-r-r
      2 (get-or-else pointer OFFSET-C relative-base memory))))

(defn op-code [{:keys [input output phase pointer relative-base memory stopped? recur?]}]
  (if stopped?
    {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? stopped? :recur? recur?}
    (let [instruction (pad-5 (memory pointer))]
      (if (= 9 (instruction :d))
        {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? true :recur? recur?}
        (case (instruction :e)
          1 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 4 pointer)
               :relative-base relative-base
               :memory        (assoc
                                memory
                                (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                (+ (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                   (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})))
               :stopped?      stopped?
               :recur?        recur?})
          2 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 4 pointer)
               :relative-base relative-base
               :memory        (assoc
                                memory
                                (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                (* (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                   (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})))
               :stopped?      stopped?
               :recur?        recur?})
          3 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base relative-base
               :memory        (if (not= phase -1)
                                (if (= 0 pointer)
                                  (assoc
                                    memory
                                    (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                    phase)
                                  (assoc
                                    memory
                                    (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                    input))
                                (assoc
                                  memory
                                  (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                  input))
               :stopped?      stopped?
               :recur?        recur?})
          4 (if recur?
              (recur
                {:input         input
                 :output        (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                 :phase         phase
                 :pointer       (+ 2 pointer)
                 :relative-base relative-base
                 :memory        memory
                 :stopped?      stopped?
                 :recur?        recur?})
              {:input         input
               :output        (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base relative-base
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?})
          5 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (if (= 0 (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
                                (+ 3 pointer)
                                (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
               :relative-base relative-base
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?})
          6 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (if (not= 0 (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
                                (+ 3 pointer)
                                (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
               :relative-base relative-base
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?})
          7 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 4 pointer)
               :relative-base relative-base
               :memory        (if (< (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                     (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
                                (assoc
                                  memory
                                  (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                  1)
                                (assoc
                                  memory
                                  (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                  0))
               :stopped?      stopped?
               :recur?        recur?})
          8 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 4 pointer)
               :relative-base relative-base
               :memory        (if (= (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                     (b-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}))
                                (assoc
                                  memory
                                  (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                  1)
                                (assoc
                                  memory
                                  (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
                                  0))
               :stopped?      stopped?
               :recur?        recur?})
          9 (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base (+ (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) relative-base)
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?})
          "Unknown opcode")))))

;;part a
;(def tv (make-tv "resources/day09.csv"))
;
;
;(def answer ((op-code {:input         1
;                       :output        0
;                       :phase         -1
;                       :pointer       0
;                       :relative-base 0
;                       :memory        tv
;                       :stopped?      false
;                       :recur?        true})
;             :output))
;
;(println answer)
;
;; 3780860499
;
;; part b
;(def answer-2 ((op-code {:input         2
;                         :output        0
;                         :phase         -1
;                         :pointer       0
;                         :relative-base 0
;                         :memory        tv
;                         :stopped?      false
;                         :recur?        true})
;               :output))
;
;(println answer-2)
;
;; 33343
