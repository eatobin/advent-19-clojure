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
    ; a-p-w
    0 (memory (+ pointer OFFSET-A))))

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    ; b-p-r
    0 (memory (memory (+ pointer OFFSET-B)))
    ; b-i-r
    1 (memory (+ pointer OFFSET-B))))

(defn c-param [{:keys [instruction pointer memory]}]
  (if (= 3 (instruction :e))
    (case (instruction :c)
      ; c-p-w
      0 (memory (+ pointer OFFSET-C)))
    (case (instruction :c)
      ; c-p-r
      0 (memory (memory (+ pointer OFFSET-C)))
      ; c-i-r
      1 (memory (+ pointer OFFSET-C)))))

(defn op-code [{:keys [input output phase pointer memory stopped? recur?]}]
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
               :memory   (if (not= phase -1)
                           (if (= 0 pointer)
                             (assoc
                               memory
                               (c-param {:instruction instruction :pointer pointer :memory memory})
                               phase)
                             (assoc
                               memory
                               (c-param {:instruction instruction :pointer pointer :memory memory})
                               input))
                           (assoc
                             memory
                             (c-param {:instruction instruction :pointer pointer :memory memory})
                             input))
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
                                                                                                 :output   0
                                                                                                 :phase    a
                                                                                                 :pointer  0
                                                                                                 :memory   i-code
                                                                                                 :stopped? false
                                                                                                 :recur?   true}) :output)
                                                                            :output   0
                                                                            :phase    b
                                                                            :pointer  0
                                                                            :memory   i-code
                                                                            :stopped? false
                                                                            :recur?   true}) :output)
                                                       :output   0
                                                       :phase    c
                                                       :pointer  0
                                                       :memory   i-code
                                                       :stopped? false
                                                       :recur?   true}) :output)
                                  :output   0
                                  :phase    d
                                  :pointer  0
                                  :memory   i-code
                                  :stopped? false
                                  :recur?   true}) :output)
             :output   0
             :phase    e
             :pointer  0
             :memory   i-code
             :stopped? false
             :recur?   true}) :output))

(defn passes [i-code]
  (map #(pass % i-code) possibles))

(def answer (apply max (passes tv)))

(println answer)

;368584

(comment
  (def v-o-p [[4 3 2 1 0]])
  (def mem [3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0])
  (defn passes [mem]
    (map #(pass % mem) v-o-p))
  (passes mem)
  (def v-o-p [[0 1 2 3 4]])
  (def mem [3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23, 101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0])
  (passes mem)
  (def v-o-p [[1, 0, 4, 3, 2]])
  (def mem [3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33, 1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0])
  (passes mem))

;part b
(def possibles-2 (for [a (range 5 10)
                       b (range 5 10)
                       c (range 5 10)
                       d (range 5 10)
                       e (range 5 10)
                       :when (distinct? a b c d e)]
                   [a b c d e]))

(defn to-amps-list [phases-vector memory]
  (letfn [(to-amps [phases]
            {1 (atom {:input 0 :output 0 :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             2 (atom {:input 0 :output 0 :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             3 (atom {:input 0 :output 0 :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             4 (atom {:input 0 :output 0 :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             5 (atom {:input 0 :output 0 :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})})]
    (map to-amps phases-vector)))

(defn runner [five-amps]
  (loop [amps five-amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (:stopped? @(amps current-amp-no)))
      (:output @(amps current-amp-no))
      (do (swap! (amps current-amp-no) op-code)
          (swap! (amps next-amp-no) assoc :input (:output @(amps current-amp-no)))
          (recur
            (assoc amps current-amp-no (amps current-amp-no) next-amp-no (amps next-amp-no))
            next-amp-no
            (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (map runner (to-amps-list possibles-2 tv))))

(println answer-2)

;35993240

(comment
  (def v-o-p [[9, 8, 7, 6, 5]])
  (def mem [3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26, 27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5])
  (def five-amps (to-amps-list v-o-p mem))
  (apply max (map runner five-amps))
  (def v-o-p [[9, 7, 8, 5, 6]])
  (def mem [3, 52, 1001, 52, -5, 52, 3, 53, 1, 52, 56, 54, 1007, 54, 5, 55, 1005, 55, 26, 1001, 54, -5, 54, 1105, 1, 12, 1, 53, 54, 53, 1008, 54, 0, 55, 1001, 55, 1, 55, 2, 53, 55, 53, 4, 53, 1001, 56, -1, 56, 1005, 56, 6, 99, 0, 0, 0, 0, 10])
  (def five-amps (to-amps-list v-o-p mem))
  (apply max (map runner five-amps)))

(comment
  (def mv [((vec possibles-2) 0)])
  mv
  [[5 6 7 8 9]]
  (def five-amps (first (to-amps-list mv tv)))
  (runner five-amps)
  33807717)

(comment
  (def vec-of-poss [[9 8 7 6 5] [1 2 3 4 5]])
  (def mem [11 22 33 44 55])
  (to-amps-list vec-of-poss mem))
