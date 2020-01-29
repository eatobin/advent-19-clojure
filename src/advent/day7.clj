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

(println answer)

;368584

;part b

(defn op-code-2 [[input phase pointer memory stopped?]]
  (if stopped?
    [input phase pointer memory true]
    (loop [pointer pointer
           memory memory]
      (let [instruction (memory pointer)]
        (case instruction
          99 [input phase pointer memory true]
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
          4 [(memory (memory (+ 1 pointer))) phase (+ 2 pointer) memory false]
          104 [(memory (+ 1 pointer)) phase (+ 2 pointer) memory false]
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
                    (assoc memory (memory (+ 3 pointer)) 0))))))))

(def possibles-2 (for [a (range 5 10)
                       b (range 5 10)
                       c (range 5 10)
                       d (range 5 10)
                       e (range 5 10)
                       :when (distinct? a b c d e)]
                   [a b c d e]))

(defn to-amps-vector [phases-vector memory]
  (letfn [(to-amps [phases]
            {1 (atom [0 (phases 0) 0 memory false])
             2 (atom [nil (phases 1) 0 memory false])
             3 (atom [nil (phases 2) 0 memory false])
             4 (atom [nil (phases 3) 0 memory false])
             5 (atom [nil (phases 4) 0 memory false])})]
    (map to-amps phases-vector)))

(defn runner [amps]
  (loop [amps amps
         current-amp-no 1
         next-amp-no (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (last @(amps current-amp-no)))
      (first @(amps current-amp-no))
      (let [op-this (atom (swap! (amps current-amp-no) op-code-2))
            op-next (atom (swap! (amps next-amp-no) assoc 0 (first @op-this)))]
        (recur
          (assoc amps current-amp-no op-this next-amp-no op-next)
          next-amp-no
          (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (vec (map runner (to-amps-vector possibles-2 tv)))))

(println answer-2)

;35993240
