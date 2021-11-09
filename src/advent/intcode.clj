(ns advent.intcode
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

(def offset-c 1)
(def offset-b 2)
(def offset-a 3)

;(defn offset-c
;  ([] initial-offset-c)
;  ([relative-base] (+ initial-offset-c relative-base)))
;
;(defn offset-b
;  ([] initial-offset-b)
;  ([relative-base] (+ initial-offset-b relative-base)))
;
;(defn offset-a
;  ([] initial-offset-a)
;  ([relative-base] (+ initial-offset-a relative-base)))

(defn pad-5 [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (- (byte character) 48))))

(defn a-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :a)
    ; a-p-w
    0 (memory (+ pointer offset-a))
    ; a-r-w
    2 (+ (memory (+ pointer offset-a)) relative-base)))

(defn b-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :b)
    ; b-p-r
    0 (get memory (memory (+ pointer offset-b)) 0)
    ;b-i-r
    1 (memory (+ pointer offset-b))
    ; b-r-r
    2 (get memory (+ (memory (+ pointer offset-b)) relative-base) 0)))

(defn c-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :e)
    3 (case (instruction :c)
        ; c-p-w
        0 (memory (+ pointer offset-c))
        ; c-r-w
        2 (+ (memory (+ pointer offset-c)) relative-base))
    (case (instruction :c)
      ; c-p-r
      0 (get memory (memory (+ pointer offset-c)) 0)
      ; c-i-r
      1 (memory (+ pointer offset-c))
      ; c-r-r
      2 (get memory (+ (memory (+ pointer offset-c)) relative-base) 0))))

;(defn c-param [{:keys [instruction pointer memory relative-base]}]
;  (case (instruction :e)
;    (1 2 4 5 6 7 8 9) (case (instruction :c)
;                        0 (get memory (memory (+ pointer offset-c)) 0)
;                        1 (get memory (+ pointer offset-c) 0)
;                        2 (get memory (+ (memory (+ pointer offset-c)) relative-base) 0))
;    3 (case (instruction :c)
;        0 (get memory (+ pointer offset-c) 0)
;        2 (+ (memory (+ pointer offset-c)) relative-base))))

;(comment
;  (def memory {0 0, 1 11, 2 22, 3 3, 4 44, 11 111, 22 222}))

;; y1
;(defn a-p-w [{:keys [pointer memory]}]
;  (memory (+ 3 pointer)))
;
;; y2, y9
;(defn b-p-r-b-r-r [{:keys [pointer memory relative-base]}]
;  (get memory (+ (memory (+ 2 pointer)) relative-base) 0))

;; y3, y10
;(defn c-p-r-c-r-r [{:keys [pointer memory relative-base]}]
;  (get memory (+ (memory (+ 1 pointer)) relative-base) 0))
;
;; y4, y6
;(defn c-p-w-c-i-r [{:keys [pointer memory]}]
;  (memory (+ 1 pointer)))
;
;; y5
;(defn b-i-r [{:keys [pointer memory]}]
;  (memory (+ 2 pointer)))
;
;; y7
;(defn a-r-w [{:keys [pointer memory relative-base]}]
;  (+ (memory (+ 3 pointer)) relative-base))
;
;; y8
;(defn c-r-w [{:keys [pointer memory relative-base]}]
;  (+ (memory (+ 1 pointer)) relative-base))
;
;(defn param-maker-c [{:keys [instruction pointer memory relative-base]}]
;  (case (instruction :e)
;    (1 2 4 5 6 7 8 9) (case (instruction :c)
;                        0 (c-p-r-c-r-r {:pointer pointer :memory memory :relative-base 0})
;                        1 (c-p-w-c-i-r {:pointer pointer :memory memory})
;                        2 (c-p-r-c-r-r {:pointer pointer :memory memory :relative-base relative-base}))
;    3 (case (instruction :c)
;        0 (c-p-w-c-i-r {:pointer pointer :memory memory})
;        2 (c-r-w {:pointer pointer :memory memory :relative-base relative-base}))))
;
;(defn param-maker-b [{:keys [instruction pointer memory relative-base]}]
;  (case (instruction :b)
;    0 (b-p-r-b-r-r {:pointer pointer :memory memory :relative-base 0})
;    1 (b-i-r {:pointer pointer :memory memory})
;    2 (b-p-r-b-r-r {:pointer pointer :memory memory :relative-base relative-base})))
;
;(defn param-maker-a [{:keys [instruction pointer memory relative-base]}]
;  (case (instruction :a)
;    0 (a-p-w {:pointer pointer :memory memory})
;    2 (a-r-w {:pointer pointer :memory memory :relative-base relative-base})))
;
(defn op-code [{:keys [input output phase pointer relative-base memory stopped? recur?]}]
  (if stopped?
    {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? stopped? :recur? recur?}
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? true :recur? recur?}
            (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base (+ (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) relative-base)
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?}))
        1 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (+ 4 pointer)
             :relative-base relative-base
             :memory        (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
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
             :memory        (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base})
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
             :memory        (if (some? phase)
                              (if (= 0 pointer)
                                (assoc memory (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) phase)
                                (assoc memory (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) input))
                              (assoc memory (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) input))
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
            {:input input :output (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) :phase phase :pointer (+ 2 pointer) :relative-base relative-base :memory memory :stopped? stopped? :recur? recur?})
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
                              (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) 1)
                              (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) 0))
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
                              (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) 1)
                              (assoc memory (a-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) 0))
             :stopped?      stopped?
             :recur?        recur?})
        "Unknown opcode"))))
