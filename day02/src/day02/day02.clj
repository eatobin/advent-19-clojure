(ns day02.day02)

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

(def memory [1, 0, 0, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 10, 1, 19, 2, 9, 19, 23, 2, 13, 23, 27, 1, 6, 27, 31, 2, 6, 31, 35, 2, 13, 35, 39, 1, 39, 10, 43, 2, 43, 13, 47, 1, 9, 47, 51, 1, 51, 13, 55, 1, 55, 13, 59, 2, 59, 13, 63, 1, 63, 6, 67, 2, 6, 67, 71, 1, 5, 71, 75, 2, 6, 75, 79, 1, 5, 79, 83, 2, 83, 6, 87, 1, 5, 87, 91, 1, 6, 91, 95, 2, 95, 6, 99, 1, 5, 99, 103, 1, 6, 103, 107, 1, 107, 2, 111, 1, 111, 5, 0, 99, 2, 14, 0, 0])

(defn char-to-int [char-as-byte]
  (if (or (< char-as-byte 48)
          (> char-as-byte 57))
    "Char is not an integer"
    (- char-as-byte 48)))

(defn pad-5 [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (char-to-int (byte character)))))

(def OFFSET-C 1)
(def OFFSET-B 2)
(def OFFSET-A 3)

(defn write-to-read-from-index [{:keys [pointer memory]} offset]
  (get memory (+ pointer offset)))

(defn -p-w [{:keys [pointer memory]} offset]
  (write-to-read-from-index {:pointer pointer :memory memory} offset))

(defn -p-r [{:keys [pointer memory]} offset]
  (get memory (write-to-read-from-index {:pointer pointer :memory memory} offset)))

(defn a-param [{:keys [instruction pointer memory]}]
  (case (instruction :a)
    0 (-p-w {:pointer pointer :memory memory} OFFSET-A)))   ; a-p-w

(defn b-param [{:keys [instruction pointer memory]}]
  (case (instruction :b)
    0 (-p-r {:pointer pointer :memory memory} OFFSET-B)))   ; b-p-r

(defn c-param [{:keys [instruction pointer memory]}]
  (case (instruction :c)
    0 (-p-r {:pointer pointer :memory memory} OFFSET-C)))   ; c-p-r

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
  (let [instruction (pad-5 (memory pointer))]
    (case (instruction :e)
      1 (recur
         (add {:instruction instruction :pointer pointer :memory memory}))
      2 (recur
         (multiply {:instruction instruction :pointer pointer :memory memory}))
      9 {:instruction instruction :pointer pointer :memory memory})))

(defn updated-memory [noun verb]
  (->
   memory
   (assoc 1 noun)
   (assoc 2 verb)))

(defn answer-a []
  (get (:memory (op-code {:pointer 0 :memory (updated-memory 12 2)})) 0))

(defn print-a
  "Invoke me with clojure -X day02.day02/print-a"
  [_]
  (println)
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush))

;;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate ((:memory (op-code {:pointer 0 :memory (updated-memory noun verb)})) 0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(defn answer-b []
  (last (first noun-verb)))

(defn print-b
  "Invoke me with clojure -X day02.day02/print-b"
  [_]
  (println)
  (printf "Part B answer: %s, correct: 8226%n" (answer-b))
  (flush))

(comment
  (print-a nil)
  (print-b nil))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (println)
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush)
  (printf "Part B answer: %s, correct: 8226%n" (answer-b))
  (flush))

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
