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

;address-is-value
(defn a-i-v [{:keys [pointer a-b-c memory relative-base]}]
  (let [a-b-c (cond
                (= :c a-b-c) 1
                (= :b a-b-c) 2
                :else 3)]
    (+
      (memory (+ a-b-c pointer))
      relative-base)))

;address-is-pointer
(defn a-i-p [{:keys [pointer a-b-c memory relative-base]}]
  (get memory (a-i-v {:pointer       pointer
                      :a-b-c         a-b-c
                      :memory        memory
                      :relative-base relative-base}
                     )
       0))

(defn param-maker [{:keys [a-b-c instruction pointer memory relative-base]}]
  (let [target (str (name a-b-c) (instruction a-b-c) (and (= a-b-c :c) (= (instruction :e) 3)))]
    (case target
      "a0false" (a-i-v {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base 0})
      "a2false" (a-i-v {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base relative-base})
      "b0false" (a-i-p {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base 0})
      "b1false" (a-i-v {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base 0})
      "b2false" (a-i-v {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base relative-base})
      "c0true" (a-i-v {:pointer       pointer
                       :a-b-c         a-b-c
                       :memory        memory
                       :relative-base 0})
      "c0false" (a-i-p {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base 0})
      "c2true" (a-i-v {:pointer       pointer
                       :a-b-c         a-b-c
                       :memory        memory
                       :relative-base relative-base})
      "c2false" (a-i-p {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base relative-base})
      "c1false" (a-i-v {:pointer       pointer
                        :a-b-c         a-b-c
                        :memory        memory
                        :relative-base 0})
      "invalid param")))

;(defn op-code [{:keys [input output phase pointer relative-base memory stopped? recur?]}]
;  (if stopped?
;    {:input         input
;     :output        output
;     :phase         phase
;     :pointer       pointer
;     :relative-base relative-base
;     :memory        memory
;     :stopped?      stopped?
;     :recur?        recur?}
;    (let [instruction (pad-5 (memory pointer))]
;      (case (instruction :e)
;        9 (if (=
;                (instruction :d)
;                9)
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       pointer
;             :relative-base relative-base
;             :memory        memory
;             :stopped?      true
;             :recur?        recur?}
;            (recur
;              {:input         input
;               :output        output
;               :phase         phase
;               :pointer       (+ 2 pointer)
;               :relative-base (+
;                                (param-maker-c {:instruction   instruction
;                                                :pointer       pointer
;                                                :memory        memory
;                                                :relative-base relative-base})
;                                relative-base)
;               :memory        memory
;               :stopped?      stopped?
;               :recur?        recur?}))
;        1 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (+ 4 pointer)
;             :relative-base relative-base
;             :memory        (assoc memory (param-maker-a {:instruction   instruction
;                                                          :pointer       pointer
;                                                          :memory        memory
;                                                          :relative-base relative-base})
;                                          (+
;                                            (param-maker-c {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            (param-maker-b {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})))
;             :stopped?      stopped?
;             :recur?        recur?})
;        2 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (+ 4 pointer)
;             :relative-base relative-base
;             :memory        (assoc memory (param-maker-a {:instruction   instruction
;                                                          :pointer       pointer
;                                                          :memory        memory
;                                                          :relative-base relative-base})
;                                          (*
;                                            (param-maker-c {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            (param-maker-b {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})))
;             :stopped?      stopped?
;             :recur?        recur?})
;        3 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (+ 2 pointer)
;             :relative-base relative-base
;             :memory        (if (some? phase)
;                              (if (=
;                                    0
;                                    pointer)
;                                (assoc memory (param-maker-c {:instruction   instruction
;                                                              :pointer       pointer
;                                                              :memory        memory
;                                                              :relative-base relative-base})
;                                              phase)
;                                (assoc memory (param-maker-c {:instruction   instruction
;                                                              :pointer       pointer
;                                                              :memory        memory
;                                                              :relative-base relative-base})
;                                              input))
;                              (assoc memory (param-maker-c {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            input))
;             :stopped?      stopped?
;             :recur?        recur?})
;        4 (if recur?
;            (recur
;              {:input         input
;               :output        (param-maker-c {:instruction   instruction
;                                              :pointer       pointer
;                                              :memory        memory
;                                              :relative-base relative-base})
;               :phase         phase
;               :pointer       (+ 2 pointer)
;               :relative-base relative-base
;               :memory        memory
;               :stopped?      stopped?
;               :recur?        recur?})
;            {:input         input
;             :output        (param-maker-c {:instruction   instruction
;                                            :pointer       pointer
;                                            :memory        memory
;                                            :relative-base relative-base})
;             :phase         phase
;             :pointer       (+ 2 pointer)
;             :relative-base relative-base
;             :memory        memory
;             :stopped?      stopped?
;             :recur?        recur?})
;        5 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (if (=
;                                  0
;                                  (param-maker-c {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base}))
;                              (+ 3 pointer)
;                              (param-maker-b {:instruction   instruction
;                                              :pointer       pointer
;                                              :memory        memory
;                                              :relative-base relative-base}))
;             :relative-base relative-base
;             :memory        memory
;             :stopped?      stopped?
;             :recur?        recur?})
;        6 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (if (not=
;                                  0
;                                  (param-maker-c {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base}))
;                              (+ 3 pointer)
;                              (param-maker-b {:instruction   instruction
;                                              :pointer       pointer
;                                              :memory        memory
;                                              :relative-base relative-base}))
;             :relative-base relative-base
;             :memory        memory
;             :stopped?      stopped?
;             :recur?        recur?})
;        7 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (+ 4 pointer)
;             :relative-base relative-base
;             :memory        (if (<
;                                  (param-maker-c {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base})
;                                  (param-maker-b {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base}))
;                              (assoc memory (param-maker-a {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            1)
;                              (assoc memory (param-maker-a {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            0))
;             :stopped?      stopped?
;             :recur?        recur?})
;        8 (recur
;            {:input         input
;             :output        output
;             :phase         phase
;             :pointer       (+ 4 pointer)
;             :relative-base relative-base
;             :memory        (if (=
;                                  (param-maker-c {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base})
;                                  (param-maker-b {:instruction   instruction
;                                                  :pointer       pointer
;                                                  :memory        memory
;                                                  :relative-base relative-base}))
;                              (assoc memory (param-maker-a {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            1)
;                              (assoc memory (param-maker-a {:instruction   instruction
;                                                            :pointer       pointer
;                                                            :memory        memory
;                                                            :relative-base relative-base})
;                                            0))
;             :stopped?      stopped?
;             :recur?        recur?})
;        "Unknown opcode"))))
