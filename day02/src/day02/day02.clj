(ns day02.day02
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

;;change to generic with offset
(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    0 (-p-w {:pointer pointer :memory memory} POINTER-OFFSET-A))) ; a-p-w

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-B))) ; b-p-r

(defn c-param [{:keys [instruction pointer memory]}]
  (case (instruction :c)
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-C))) ; c-p-r

;;part a
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

(defn op-code [{:keys [pointer memory]}]
  (let [instruction (make-instruction (memory pointer))]
    (case (instruction :e)
      1 (recur
         (add {:instruction instruction :pointer pointer :memory memory}))
      2 (recur
         (multiply {:instruction instruction :pointer pointer :memory memory}))
      9 {:instruction instruction :pointer pointer :memory memory})))

(defn updated-memory [memory noun verb]
  (->
   memory
   (assoc 1 noun)
   (assoc 2 verb)))


;
;(defn print-a
;  "Invoke me with clojure -X day02.day02/print-a"
;  []
;  (printf "\nPart A answer: %s, correct: 2890696%n" (answer-a)))
;
;;;part b
;(def noun-verb
;  (vec (for [noun (range 0 100)
;             verb (range 0 100)
;             :let [candidate ((:memory (op-code {:pointer 0 :memory (updated-memory noun verb)})) 0)]
;             :when (= candidate 19690720)]
;         [candidate noun verb (+ (* 100 noun) verb)])))
;
;(defn answer-b []
;  (last (first noun-verb)))
;
;(defn print-b
;  "Invoke me with clojure -X day02.day02/print-b"
;  []
;  (printf "\nPart B answer: %s, correct: 8226%n" (answer-b)))
;
;(comment
;  (print-a)
;  (print-b))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (let [memory-as-csv-string "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0"
        memory               (make-memory memory-as-csv-string)
        new-memory           (updated-memory memory 12 2)
        answer-a             (get (:memory (op-code {:pointer 0 :memory new-memory})) 0)]
    (printf "\nPart A answer: %s, correct: 2890696%n" answer-a)))

;(printf "Part B answer: %s, correct: 8226%n\n" (answer-b)))

(comment
  (-main)
  *ns*)

(comment
  ;; clojure -M:repl/reloaded
  (require '[portal.api :as p])
  (def p (p/open))
  (add-tap #'p/submit)
  (tap> {:nope
         [{:name "jen" :email "jen@jen.com"}
          {:name "sara" :email "sara@sara.com"}
          {:name "ericky" :email "eatobin@gmail.com"}]})
  (p/clear)
  (remove-tap #'p/submit)
  (p/close)
  (p/docs))
