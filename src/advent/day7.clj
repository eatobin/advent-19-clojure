(ns advent.day7
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day7.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def a [4, 3, 2, 1, 0])
(def b [3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0])

(def c [0,1,2,3,4])
(def d [3,23,3,24,1002,24,10,24,1002,23,-1,23,
        101,5,23,23,1,24,23,23,4,23,99,0,0])

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

(def answer (op-code 0 0 tv))

;11981754
