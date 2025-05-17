; clj -X clojure.core.server/start-server :name repl :port 5555 :accept clojure.core.server/repl :server-daemon false

(ns day02.day02)

(def OFFSET-C 1)
(def OFFSET-B 2)
(def OFFSET-A 3)

;part a
(defn op-code [{:keys [pointer memory]}]
  (case (get memory pointer)
    1 (recur
       {:pointer       (+ 4 pointer)
        :memory        (assoc
                        memory
                        (get memory (+ pointer OFFSET-A))
                        (+ (get memory (get memory (+ pointer OFFSET-C)))
                           (get memory (get memory (+ pointer OFFSET-B)))))})
    2 (recur
       {:pointer       (+ 4 pointer)
        :memory        (assoc
                        memory
                        (get memory (+ pointer OFFSET-A))
                        (* (get memory (get memory (+ pointer OFFSET-C)))
                           (get memory (get memory (+ pointer OFFSET-B)))))})
    99 {:pointer       pointer
        :memory        memory}))

(def memory [1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0])

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
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush))

;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate ((:memory (op-code {:pointer 0 :memory (updated-memory noun verb)})) 0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(comment
  (print-a nil))
; (print-b nil))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush))
; (printf "Part B answer: %s, correct: 5003788%n" (answer-b))
; (flush))

