(ns day02.library
  (:require
   [clojure.string :as str]))

; [eric@eric-minisforum day02](dev)$ clojure -M:repl/rebel
; nREPL server started on port 45677 on host localhost - nrepl://localhost:45677
; [Rebel readline] Type :repl/help for online help info
; user=> (require '[day02.day02 :as day02])
; nil
; user=> *ns*
; #namespace[user]
; user=> (day02/answer-b)
; 8226

;; Instruction:
;; ABCDE
;; 01234
;; 01002
;; 34(DE) - two-digit opcode,      02 == opcode 2
;;  2(C) - mode of 1st parameter,  0 == position mode
;;  1(B) - mode of 2nd parameter,  1 == immediate mode
;;  0(A) - mode of 3rd parameter,  0 == position mode,
;;                                   omitted due to being a leading zero
;; 0 1 or 2 = left-to-right position after 2 digit opcode
;; p i or r = position, immediate or relative mode
;; r or w = read or write

(def POINTER-OFFSET-C 1)
(def POINTER-OFFSET-B 2)
(def POINTER-OFFSET-A 3)

(defn make-ints-list [memory-as-csv-string]
  (->> (str/split memory-as-csv-string #",")
       (map parse-long)
       (apply list)))

(defn make-memory [memory-as-csv-string]
  (zipmap (range) (make-ints-list memory-as-csv-string)))

(defn make-intcode [pointer memory-as-csv-string]
  {:pointer pointer :memory (make-memory memory-as-csv-string)})

(defn make-instruction [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (- (byte character) 48))))

(defn key-to-key [{:keys [pointer memory]} pointer-offset]
  (get memory (+ pointer pointer-offset)))

(defn -p-w [{:keys [pointer memory]} pointer-offset]
  (key-to-key {:pointer pointer :memory memory} pointer-offset))

(defn -p-r [{:keys [pointer memory]} pointer-offset]
  (get memory (key-to-key {:pointer pointer :memory memory} pointer-offset)))

(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    0 (-p-w {:pointer pointer :memory memory} POINTER-OFFSET-A))) ; a-p-w

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-B))) ; b-p-r

(defn c-param [{:keys [instruction pointer memory]}]
  (case (instruction :c)
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-C))) ; c-p-r

(defn add [{:keys [instruction pointer memory]}]
  {:pointer (+ 4 pointer)
   :memory  (assoc
             memory
             (a-param {:instruction instruction :pointer pointer :memory memory})
             (+ (c-param {:instruction instruction :pointer pointer :memory memory})
                (b-param {:instruction instruction :pointer pointer :memory memory})))})

(defn multiply [{:keys [instruction pointer memory]}]
  {:pointer (+ 4 pointer)
   :memory  (assoc
             memory
             (a-param {:instruction instruction :pointer pointer :memory memory})
             (* (c-param {:instruction instruction :pointer pointer :memory memory})
                (b-param {:instruction instruction :pointer pointer :memory memory})))})

(defn run-op-code [{:keys [pointer memory]}]
  (let [instruction (make-instruction (memory pointer))]
    (case (instruction :e)
      1 (recur
         (add {:instruction instruction :pointer pointer :memory memory}))
      2 (recur
         (multiply {:instruction instruction :pointer pointer :memory memory}))
      9 {:instruction instruction :pointer pointer :memory memory})))
