(ns day02.day02
  (:require
   [day02.library :as lib]))

;; part a
(defn updated-memory [memory noun verb]
  (->
   memory
   (assoc 1 noun)
   (assoc 2 verb)))

;; part b
(defn noun-verb [memory]
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate ((:memory (lib/run-op-code {:pointer 0 :memory (updated-memory memory noun verb)})) 0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (let [memory-as-csv-string "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0"
        memory               (lib/make-memory memory-as-csv-string)
        new-memory           (updated-memory memory 12 2)
        answer-a             (get (:memory (lib/run-op-code {:pointer 0 :memory new-memory})) 0)]
    (printf "\nPart A answer: %s, correct: 2890696%n" answer-a)
    (let [winner   (noun-verb memory)
          answer-b (last (first winner))]
      (printf "Part B answer: %s, correct: 8226%n" answer-b))))

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
