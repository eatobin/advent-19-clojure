(ns advent.day7
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a

(def tv (->> (first (with-open [reader (io/reader "resources/day7.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn op-code [phase input memory]
  (loop [pointer 0
         memory memory
         exit-code 0]
    (let [instruction (memory pointer)]
      (case instruction
        99 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            exit-code)
        101 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1001 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               exit-code)
        1101 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            exit-code)
        102 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1002 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               exit-code)
        1102 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        3 (recur
            (+ 2 pointer)
            (if (= 0 pointer)
              (assoc memory (memory (+ 1 pointer)) phase)
              (assoc memory (memory (+ 1 pointer)) input))
            exit-code)
        4 (recur
            (+ 2 pointer)
            memory
            (memory (memory (+ 1 pointer))))
        104 (recur
              (+ 2 pointer)
              memory
              (memory (+ 1 pointer)))
        5 (recur
            (if (= 0 (memory (memory (+ 1 pointer))))
              (+ 3 pointer)
              (memory (memory (+ 2 pointer))))
            memory
            exit-code)
        105 (recur
              (if (= 0 (memory (+ 1 pointer)))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory
              exit-code)
        1005 (recur
               (if (= 0 (memory (memory (+ 1 pointer))))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code)
        1105 (recur
               (if (= 0 (memory (+ 1 pointer)))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code)
        6 (recur
            (if (not= 0 (memory (memory (+ 1 pointer))))
              (+ 3 pointer)
              (memory (memory (+ 2 pointer))))
            memory
            exit-code)
        106 (recur
              (if (not= 0 (memory (+ 1 pointer)))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory
              exit-code)
        1006 (recur
               (if (not= 0 (memory (memory (+ 1 pointer))))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code)
        1106 (recur
               (if (not= 0 (memory (+ 1 pointer)))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code)
        7 (recur
            (+ 4 pointer)
            (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            exit-code)
        107 (recur
              (+ 4 pointer)
              (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))
              exit-code)
        1007 (recur
               (+ 4 pointer)
               (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code)
        1107 (recur
               (+ 4 pointer)
               (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code)
        10007 (recur
                (+ 4 pointer)
                (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        10107 (recur
                (+ 4 pointer)
                (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        11007 (recur
                (+ 4 pointer)
                (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        11107 (recur
                (+ 4 pointer)
                (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        8 (recur
            (+ 4 pointer)
            (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            exit-code)
        108 (recur
              (+ 4 pointer)
              (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))
              exit-code)
        1008 (recur
               (+ 4 pointer)
               (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code)
        1108 (recur
               (+ 4 pointer)
               (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code)
        10008 (recur
                (+ 4 pointer)
                (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        10108 (recur
                (+ 4 pointer)
                (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        11008 (recur
                (+ 4 pointer)
                (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)
        11108 (recur
                (+ 4 pointer)
                (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code)))))

(def possibles (for [a (range 0 5)
                     b (range 0 5)
                     c (range 0 5)
                     d (range 0 5)
                     e (range 0 5)
                     :when (distinct? a b c d e)]
                 [a b c d e]))

(defn pass [[a b c d e] i-code]
  (op-code e (op-code d (op-code c (op-code b (op-code a 0 i-code) i-code) i-code) i-code) i-code))

(defn passes [i-code]
  (vec (map #(pass % i-code) possibles)))

(def answer (apply max (passes tv)))

;368584

;part b

(defn op-code-2 [[input phase pointer memory running?]]
  (if running?
    (loop [pointer pointer
           memory memory]
      (let [instruction (memory pointer)]
        (case instruction
          99 [input phase pointer memory false]
          1 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))
          101 (recur
                (+ 4 pointer)
                (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))))
          1001 (recur
                 (+ 4 pointer)
                 (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))))
          1101 (recur
                 (+ 4 pointer)
                 (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer)))))
          2 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))))
          102 (recur
                (+ 4 pointer)
                (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))))
          1002 (recur
                 (+ 4 pointer)
                 (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))))
          1102 (recur
                 (+ 4 pointer)
                 (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer)))))
          3 (recur
              (+ 2 pointer)
              (if (= 0 pointer)
                (assoc memory (memory (+ 1 pointer)) phase)
                (assoc memory (memory (+ 1 pointer)) input)))
          4 [(memory (memory (+ 1 pointer))) phase (+ 2 pointer) memory true]
          104 [(memory (+ 1 pointer)) phase (+ 2 pointer) memory true]
          5 (recur
              (if (= 0 (memory (memory (+ 1 pointer))))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory)
          105 (recur
                (if (= 0 (memory (+ 1 pointer)))
                  (+ 3 pointer)
                  (memory (memory (+ 2 pointer))))
                memory)
          1005 (recur
                 (if (= 0 (memory (memory (+ 1 pointer))))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 memory)
          1105 (recur
                 (if (= 0 (memory (+ 1 pointer)))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 memory)
          6 (recur
              (if (not= 0 (memory (memory (+ 1 pointer))))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory)
          106 (recur
                (if (not= 0 (memory (+ 1 pointer)))
                  (+ 3 pointer)
                  (memory (memory (+ 2 pointer))))
                memory)
          1006 (recur
                 (if (not= 0 (memory (memory (+ 1 pointer))))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 memory)
          1106 (recur
                 (if (not= 0 (memory (+ 1 pointer)))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 memory)
          7 (recur
              (+ 4 pointer)
              (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          107 (recur
                (+ 4 pointer)
                (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0)))
          1007 (recur
                 (+ 4 pointer)
                 (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          1107 (recur
                 (+ 4 pointer)
                 (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          10007 (recur
                  (+ 4 pointer)
                  (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          10107 (recur
                  (+ 4 pointer)
                  (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11007 (recur
                  (+ 4 pointer)
                  (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11107 (recur
                  (+ 4 pointer)
                  (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          8 (recur
              (+ 4 pointer)
              (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          108 (recur
                (+ 4 pointer)
                (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0)))
          1008 (recur
                 (+ 4 pointer)
                 (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          1108 (recur
                 (+ 4 pointer)
                 (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          10008 (recur
                  (+ 4 pointer)
                  (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          10108 (recur
                  (+ 4 pointer)
                  (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11008 (recur
                  (+ 4 pointer)
                  (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11108 (recur
                  (+ 4 pointer)
                  (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0))))))
    [input phase pointer memory false]))

(def a [4, 3, 2, 1, 0])
(def b [3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0])

(def c [0, 1, 2, 3, 4])
(def d [3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
        101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0])

(def e [1, 0, 4, 3, 2])
(def f [3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
        1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0])

(op-code-2 [0 4 0 b true])
;=> [4 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] true]
(op-code-2 [4 3 0 b true])
;=>[43 3 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43 40] true]
(op-code-2 [43 2 0 b true])
;=> [432 2 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 432 430] true]
(op-code-2 [432 1 0 b true])
;=> [4321 1 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4321 4320] true]
(op-code-2 [4321 0 0 b true])
;=> [43210 0 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43210 43210] true]

(op-code-2 [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] true])
;=> [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] false]
(op-code-2 [43210 3 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43 40] true])
;=> [43210 3 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43 40] false]
(op-code-2 [43210 2 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 432 430] true])
;=> [43210 2 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 432 430] false]
(op-code-2 [43210 1 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4321 4320] true])
;=> [43210 1 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4321 4320] false]
(op-code-2 [43210 0 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43210 43210] true])
;=> [43210 0 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 43210 43210] false]

(def aa [9, 8, 7, 6, 5])
(def bb [3, 26, 1001, 26, -4, 26, 3, 27, 1002, 27, 2, 27, 1, 27, 26,
         27, 4, 27, 1001, 28, -1, 28, 1005, 28, 6, 99, 0, 0, 5])

(op-code-2 [0 9 0 bb true])
;=> [5 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 5 5] true]
(op-code-2 [5 8 0 bb true])
;=> [14 8 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 4 14 5] true]
(op-code-2 [14 7 0 bb true])
;=> [31 7 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 3 31 5] true]
(op-code-2 [31 6 0 bb true])
;=> [64 6 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 2 64 5] true]
(op-code-2 [64 5 0 bb true])
;=> [129 5 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 1 129 5] true]

(op-code-2 [129 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 5 5] true])
;=> [263 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 263 4] true]

(op-code-2 [263 8 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 4 14 5]])
(op-code-2 [530 7 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 3 31 5]])
(op-code-2 [1063 6 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 2 64 5]])
(op-code-2 [2128 5 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 1 129 5]])
(op-code-2 [4257 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 263 4]])
(op-code-2 [8519 8 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 4 530 4]])
(op-code-2 [17042 7 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 3 1063 4]])
(op-code-2 [34087 6 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 2 2128 4]])
(op-code-2 [68176 5 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 1 4257 4]])
(op-code-2 [136353 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 8519 3]])
(op-code-2 [272711 8 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 4 17042 3]])
(op-code-2 [545426 7 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 3 34087 3]])
(op-code-2 [1090855 6 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 2 68176 3]])
(op-code-2 [2181712 5 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 1 136353 3]])
(op-code-2 [4363425 9 18 [3 26 1001 26 -4 26 3 27 1002 27 2 27 1 27 26 27 4 27 1001 28 -1 28 1005 28 6 99 5 272711 2]])

(op-code-2 [0 4 0 b true])
;=> [4 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] true]
(op-code-2 [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] true])
;=> [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] false]
(op-code-2 [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] false])
;=> [43210 4 14 [3 15 3 16 1002 16 10 16 1 16 15 15 4 15 99 4 0] false]


(def amp1 (atom 0))
(def amp2 (atom 0))
(def amp3 (atom 0))
(def amp4 (atom 0))
(def amp5 (atom 0))
(def amp1 (atom [0 9 0 bb]))
(def amp2 (atom [nil 8 0 bb]))
(def amp3 (atom [nil 7 0 bb]))
(def amp4 (atom [nil 6 0 bb]))
(def amp5 (atom [nil 5 0 bb]))
(def amps {1 amp1, 2 amp2, 3 amp3, 4 amp4, 5 amp5})

(def next-amp {1 2, 2 3, 3 4, 4 5, 5 1})

(defn runner [amps]
  (loop [amps amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))
         current-amp @(amps current-amp-no)
         next-amp @(amps next-amp-no)
         thrust 0]
    (if (and (= 0 @(amps 1)) (= 0 @(amps 2)) (= 0 @(amps 3)) (= 0 @(amps 4)) (= 0 @(amps 5)))
      thrust
      (if (and (not= 0 current-amp) (not= 0 next-amp))
        (let [result (op-code-2 current-amp)
              new-next-amp (assoc next-amp 0 (result 0))
              new-thrust (result 0)]
          (recur
            (assoc amps current-amp-no (atom result))
            next-amp-no
            (+ 1 (mod current-amp-no 5))
            new-next-amp
            @(amps next-amp-no)
            new-thrust))
        true))))
