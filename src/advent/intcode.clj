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

(defn a-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :a)
    0 (memory (+ pointer OFFSET-A))                         ; a-p-w
    2 (+ (memory (+ pointer OFFSET-A)) relative-base)))     ; a-r-w

(defn b-param [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :b)
    0 (memory (memory (+ pointer OFFSET-B)))                ; b-p-r
    1 (memory (+ pointer OFFSET-B))                         ; b-i-r
    2 (memory (+ (memory (+ pointer OFFSET-B)) relative-base)))) ; b-r-r

(defn c-param [{:keys [instruction pointer memory relative-base]}]
  (if (= 3 (instruction :e))
    (case (instruction :c)
      0 (memory (+ pointer OFFSET-C))                       ; c-p-w
      2 (+ (memory (+ pointer OFFSET-C)) relative-base))    ; c-r-w
    (case (instruction :c)
      0 (memory (memory (+ pointer OFFSET-C)))              ; c-p-r
      1 (memory (+ pointer OFFSET-C))                       ; c-i-r
      2 (memory (+ (memory (+ pointer OFFSET-C)) relative-base))))) ; c-r-r

(defn op-code [{:keys [input output phase pointer relative-base memory stopped? recur?]}]
  (if stopped?
    {:input input :output output :phase phase :pointer pointer :relative-base relative-base :memory memory :stopped? stopped? :recur? recur?}
    (let [instruction (pad-5 (memory pointer))]
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
             :memory        (if (some? phase)
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
        9 (if (= (instruction :d) 9)
            (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       pointer
               :relative-base relative-base
               :memory        memory
               :stopped?      true
               :recur?        recur?})
            (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base (+ (c-param {:instruction instruction :pointer pointer :memory memory :relative-base relative-base}) relative-base)
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?}))
        "Unknown opcode"))))
