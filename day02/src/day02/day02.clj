(ns day02.day02
  (:require
   [day02.library :as lib]))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (let [memory-as-csv-string "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,10,1,19,2,9,19,23,2,13,23,27,1,6,27,31,2,6,31,35,2,13,35,39,1,39,10,43,2,43,13,47,1,9,47,51,1,51,13,55,1,55,13,59,2,59,13,63,1,63,6,67,2,6,67,71,1,5,71,75,2,6,75,79,1,5,79,83,2,83,6,87,1,5,87,91,1,6,91,95,2,95,6,99,1,5,99,103,1,6,103,107,1,107,2,111,1,111,5,0,99,2,14,0,0"
        initial-memory       (lib/make-memory memory-as-csv-string)
        initial-state        {:pointer 0 :memory (lib/updated-memory 12 2 initial-memory)}
        final-state-a        (lib/run-op-code initial-state)
        answer-1             (get (:memory final-state-a) 0)]

    ;; part a
    (printf "\nPart A answer: %s, correct: 2890696%n" answer-1)

    ;; part b
    (let [answer-2 (last (first (for [noun (range 0 100)
                                      verb (range 0 100)
                                      :let [candidate ((:memory (lib/run-op-code {:pointer 0 :memory (lib/updated-memory noun verb initial-memory)})) 0)]
                                      :when (= candidate 19690720)]
                                  [candidate noun verb (+ (* 100 noun) verb)])))]
      (printf "Part B answer: %s, correct: 8226%n" answer-2))))

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
