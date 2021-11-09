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
