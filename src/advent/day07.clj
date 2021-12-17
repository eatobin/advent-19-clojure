(ns advent.day07
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

;; {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? stopped? :recur? recur?}

(defn op-code [{:keys [input output phase pointer memory stopped? :recur?]}]
  (if stopped?
    {:input input :output output :phase phase :pointer pointer :memory memory :stopped? stopped? :recur? recur?}
    (let [instruction (pad-5 (memory pointer))]
      (if (= 9 (instruction :d))
        {:input input :output output :phase phase :pointer pointer :memory memory :stopped? true :recur? recur?}
        (case (instruction :e)
          1 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (+ 4 pointer)
               :memory   (assoc
                           memory
                           (a-param {:instruction instruction :pointer pointer :memory memory})
                           (+ (c-param {:instruction instruction :pointer pointer :memory memory})
                              (b-param {:instruction instruction :pointer pointer :memory memory})))
               :stopped? stopped?
               :recur?   recur?})
          2 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (+ 4 pointer)
               :memory   (assoc
                           memory
                           (a-param {:instruction instruction :pointer pointer :memory memory})
                           (* (c-param {:instruction instruction :pointer pointer :memory memory})
                              (b-param {:instruction instruction :pointer pointer :memory memory})))
               :stopped? stopped?
               :recur?   recur?})
          3 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (+ 2 pointer)
               :memory   (assoc
                           memory
                           (c-param {:instruction instruction :pointer pointer :memory memory})
                           input)
               :stopped? stopped?
               :recur?   recur?})
          4 (if recur?
              (recur
                {:input    input
                 :output   (c-param {:instruction instruction :pointer pointer :memory memory})
                 :phase    phase
                 :pointer  (+ 2 pointer)
                 :memory   memory
                 :stopped? stopped?
                 :recur?   recur?})
              {:input    input
               :output   (c-param {:instruction instruction :pointer pointer :memory memory})
               :phase    phase
               :pointer  (+ 2 pointer)
               :memory   memory
               :stopped? stopped?
               :recur?   recur?})
          5 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (if (= 0 (c-param {:instruction instruction :pointer pointer :memory memory}))
                           (+ 3 pointer)
                           (b-param {:instruction instruction :pointer pointer :memory memory}))
               :memory   memory
               :stopped? stopped?
               :recur?   recur?})
          6 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (if (not= 0 (c-param {:instruction instruction :pointer pointer :memory memory}))
                           (+ 3 pointer)
                           (b-param {:instruction instruction :pointer pointer :memory memory}))
               :memory   memory
               :stopped? stopped?
               :recur?   recur?})
          7 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (+ 4 pointer)
               :memory   (if (< (c-param {:instruction instruction :pointer pointer :memory memory})
                                (b-param {:instruction instruction :pointer pointer :memory memory}))
                           (assoc
                             memory
                             (a-param {:instruction instruction :pointer pointer :memory memory})
                             1)
                           (assoc
                             memory
                             (a-param {:instruction instruction :pointer pointer :memory memory})
                             0))
               :stopped? stopped?
               :recur?   recur?})
          8 (recur
              {:input    input
               :output   output
               :phase    phase
               :pointer  (+ 4 pointer)
               :memory   (if (= (c-param {:instruction instruction :pointer pointer :memory memory})
                                (b-param {:instruction instruction :pointer pointer :memory memory}))
                           (assoc
                             memory
                             (a-param {:instruction instruction :pointer pointer :memory memory})
                             1)
                           (assoc
                             memory
                             (a-param {:instruction instruction :pointer pointer :memory memory})
                             0))
               :stopped? stopped?
               :recur?   recur?})
          "Unknown opcode")))))


;part a
(def tv (make-tv "resources/day07.csv"))

(def possibles (for [a (range 0 5)
                     b (range 0 5)
                     c (range 0 5)
                     d (range 0 5)
                     e (range 0 5)
                     :when (distinct? a b c d e)]
                 [a b c d e]))

(defn pass [[a b c d e] i-code]
  ((op-code {:input    ((op-code {:input    ((op-code {:input    ((op-code {:input    ((op-code {:input    0
                                                                                                 :output   nil
                                                                                                 :phase    a
                                                                                                 :pointer  0
                                                                                                 :memory   i-code
                                                                                                 :stopped? false
                                                                                                 :recur?   true}) :output)
                                                                            :output   nil
                                                                            :phase    b
                                                                            :pointer  0
                                                                            :memory   i-code
                                                                            :stopped? false
                                                                            :recur?   true}) :output)
                                                       :output   nil
                                                       :phase    c
                                                       :pointer  0
                                                       :memory   i-code
                                                       :stopped? false
                                                       :recur?   true}) :output)
                                  :output   nil
                                  :phase    d
                                  :pointer  0
                                  :memory   i-code
                                  :stopped? false
                                  :recur?   true}) :output)
             :output   nil
             :phase    e
             :pointer  0
             :memory   i-code
             :stopped? false
             :recur?   true}) :output))

(defn passes [i-code]
  (map #(pass % i-code) possibles))

(def answer (apply max (passes tv)))

;(println answer)

;368584

;part b
;(def possibles-2 (for [a (range 5 10)
;                       b (range 5 10)
;                       c (range 5 10)
;                       d (range 5 10)
;                       e (range 5 10)
;                       :when (distinct? a b c d e)]
;                   [a b c d e]))
;
;(defn to-amps-list [phases-vector memory]
;  (letfn [(to-amps [phases]
;            {1 (atom {:input 0 :output nil :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
;             2 (atom {:input nil :output nil :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
;             3 (atom {:input nil :output nil :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
;             4 (atom {:input nil :output nil :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
;             5 (atom {:input nil :output nil :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})})]
;    (map to-amps phases-vector)))
;
;(defn runner [five-amps]
;  (loop [amps five-amps
;         current-amp-no 1
;         next-amp-no (+ 1 (mod current-amp-no 5))]
;    (if (and (= 5 current-amp-no) (:stopped? @(amps current-amp-no)))
;      (:output @(amps current-amp-no))
;      (do (swap! (amps current-amp-no) op-code)
;          (swap! (amps next-amp-no) assoc :input (:output @(amps current-amp-no)))
;          (recur
;            (assoc amps current-amp-no (amps current-amp-no) next-amp-no (amps next-amp-no))
;            next-amp-no
;            (+ 1 (mod next-amp-no 5)))))))
;
;(def answer-2 (apply max (map runner (to-amps-list possibles-2 tv))))
;
;(println answer-2)

;35993240

;(comment
;  (def mv [((vec possibles-2) 0)])
;  mv
;  [[5 6 7 8 9]]
;  (def five-amps (first (to-amps-list mv tv)))
;  (runner five-amps)
;  33807717)
;
;(comment
;  (to-amps-list
;    [[5 6 7 8 9]]
;    {0 3, 1 15, 2 3, 3 16, 4 1002, 5 16, 6 10, 7 16, 8 1, 9 16, 10 15, 11 15, 12 4, 13 15, 14 99, 15 0, 16 0}))
