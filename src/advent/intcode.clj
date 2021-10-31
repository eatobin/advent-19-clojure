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

(defn pad-5 [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (- (byte character) 48))))

; y1
(defn a-p-w [{:keys [pointer memory]}]
  (memory (+ 3 pointer)))

; y2, y9
(defn b-p-r-b-r-r [{:keys [pointer memory relative-base]}]
  (get memory (+
                (memory (+ 2 pointer))
                relative-base)
       0))

; y3, y10
(defn c-p-r-c-r-r [{:keys [pointer memory relative-base]}]
  (get memory (+
                (memory (+ 1 pointer))
                relative-base)
       0))

; y4, y6
(defn c-p-w-c-i-r [{:keys [pointer memory]}]
  (memory (+ 1 pointer)))

; y5
(defn b-i-r [{:keys [pointer memory]}]
  (memory (+ 2 pointer)))

; y7
(defn a-r-w [{:keys [pointer memory relative-base]}]
  (+
    (memory (+ 3 pointer))
    relative-base))

; y8
(defn c-r-w [{:keys [pointer memory relative-base]}]
  (+
    (memory (+ 1 pointer))
    relative-base))

(defn param-maker-c [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :e)
    (1 2 4 5 6 7 8 9)
    (case (instruction :c)
      0 (c-p-r-c-r-r {:pointer       pointer
                      :memory        memory
                      :relative-base 0})
      1 (c-p-w-c-i-r {:pointer pointer
                      :memory  memory})
      2 (c-p-r-c-r-r {:pointer       pointer
                      :memory        memory
                      :relative-base relative-base}))
    3 (case (instruction :c)
        0 (c-p-w-c-i-r {:pointer pointer
                        :memory  memory})
        2 (c-r-w {:pointer       pointer
                  :memory        memory
                  :relative-base relative-base}))))

(defn param-maker-b [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :b)
    0 (b-p-r-b-r-r {:pointer       pointer
                    :memory        memory
                    :relative-base 0})
    1 (b-i-r {:pointer pointer
              :memory  memory})
    2 (b-p-r-b-r-r {:pointer       pointer
                    :memory        memory
                    :relative-base relative-base})))

(defn param-maker-a [{:keys [instruction pointer memory relative-base]}]
  (case (instruction :a)
    0 (a-p-w {:pointer pointer
              :memory  memory})
    2 (a-r-w {:pointer       pointer
              :memory        memory
              :relative-base relative-base})))

(defn op-code [{:keys [input output phase pointer relative-base memory stopped? recur?]}]
  (if stopped?
    {:input         input
     :output        output
     :phase         phase
     :pointer       pointer
     :relative-base relative-base
     :memory        memory
     :stopped?      stopped?
     :recur?        recur?}
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (=
                (instruction :d)
                9)
            {:input         input
             :output        output
             :phase         phase
             :pointer       pointer
             :relative-base relative-base
             :memory        memory
             :stopped?      true
             :recur?        recur?}
            (recur
              {:input         input
               :output        output
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base (+
                                (param-maker-c {:instruction   instruction
                                                :pointer       pointer
                                                :memory        memory
                                                :relative-base relative-base})
                                relative-base)
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?}))
        1 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (+ 4 pointer)
             :relative-base relative-base
             :memory        (assoc memory (param-maker-a {:instruction   instruction
                                                          :pointer       pointer
                                                          :memory        memory
                                                          :relative-base relative-base})
                                          (+
                                            (param-maker-c {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            (param-maker-b {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})))
             :stopped?      stopped?
             :recur?        recur?})
        2 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (+ 4 pointer)
             :relative-base relative-base
             :memory        (assoc memory (param-maker-a {:instruction   instruction
                                                          :pointer       pointer
                                                          :memory        memory
                                                          :relative-base relative-base})
                                          (*
                                            (param-maker-c {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            (param-maker-b {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})))
             :stopped?      stopped?
             :recur?        recur?})
        3 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (+ 2 pointer)
             :relative-base relative-base
             :memory        (if (some? phase)
                              (if (=
                                    0
                                    pointer)
                                (assoc memory (param-maker-c {:instruction   instruction
                                                              :pointer       pointer
                                                              :memory        memory
                                                              :relative-base relative-base})
                                              phase)
                                (assoc memory (param-maker-c {:instruction   instruction
                                                              :pointer       pointer
                                                              :memory        memory
                                                              :relative-base relative-base})
                                              input))
                              (assoc memory (param-maker-c {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            input))
             :stopped?      stopped?
             :recur?        recur?})
        4 (if recur?
            (recur
              {:input         input
               :output        (param-maker-c {:instruction   instruction
                                              :pointer       pointer
                                              :memory        memory
                                              :relative-base relative-base})
               :phase         phase
               :pointer       (+ 2 pointer)
               :relative-base relative-base
               :memory        memory
               :stopped?      stopped?
               :recur?        recur?})
            {:input         input
             :output        (param-maker-c {:instruction   instruction
                                            :pointer       pointer
                                            :memory        memory
                                            :relative-base relative-base})
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
             :pointer       (if (=
                                  0
                                  (param-maker-c {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base}))
                              (+ 3 pointer)
                              (param-maker-b {:instruction   instruction
                                              :pointer       pointer
                                              :memory        memory
                                              :relative-base relative-base}))
             :relative-base relative-base
             :memory        memory
             :stopped?      stopped?
             :recur?        recur?})
        6 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (if (not=
                                  0
                                  (param-maker-c {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base}))
                              (+ 3 pointer)
                              (param-maker-b {:instruction   instruction
                                              :pointer       pointer
                                              :memory        memory
                                              :relative-base relative-base}))
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
             :memory        (if (<
                                  (param-maker-c {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base})
                                  (param-maker-b {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base}))
                              (assoc memory (param-maker-a {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            1)
                              (assoc memory (param-maker-a {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            0))
             :stopped?      stopped?
             :recur?        recur?})
        8 (recur
            {:input         input
             :output        output
             :phase         phase
             :pointer       (+ 4 pointer)
             :relative-base relative-base
             :memory        (if (=
                                  (param-maker-c {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base})
                                  (param-maker-b {:instruction   instruction
                                                  :pointer       pointer
                                                  :memory        memory
                                                  :relative-base relative-base}))
                              (assoc memory (param-maker-a {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            1)
                              (assoc memory (param-maker-a {:instruction   instruction
                                                            :pointer       pointer
                                                            :memory        memory
                                                            :relative-base relative-base})
                                            0))
             :stopped?      stopped?
             :recur?        recur?})
        "Unknown opcode"))))

;(defn univ [{:keys [pointer a-b-c memory relative-base]}]
;  (get memory (+
;                (memory (+ a-b-c pointer))
;                relative-base)
;       0))

(defn univ [{:keys [value pointer a-b-c memory relative-base]}]
  (if value (memory (+ a-b-c pointer))
            (get memory (+
                          (memory (+ a-b-c pointer))
                          relative-base)
                 0)))
