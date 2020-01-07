(ns advent.day5
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "day5.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def ex1 [3 0 4 0 99])
(def ex2 [3 0 104 63 99])
(def ex3 [1002 4 3 4 33])

(defn op-code [input memory]
  (loop [pointer 0
         memory memory
         exit-code 0]
    (let [instruction (memory (+ 0 pointer))
          pm1 (memory (+ 1 pointer))
          im1 (+ 1 pointer)
          pm2 (memory (+ 2 pointer))
          im2 (+ 2 pointer)
          pm3 (memory (+ 3 pointer))
          im3 (+ 3 pointer)]
      (case instruction
        99 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory pm1) (memory (memory (+ 2 pointer)))))
            exit-code)
        101 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1001 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory pm1) (memory (+ 2 pointer))))
               exit-code)
        1101 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory pm1) (memory (memory (+ 2 pointer)))))
            exit-code)
        102 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1002 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory pm1) (memory (+ 2 pointer))))
               exit-code)
        1102 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        3 (recur
            (+ 2 pointer)
            (assoc memory (memory (+ 1 pointer)) input)
            exit-code)
        4 (recur
            (+ 2 pointer)
            memory
            (memory pm1))
        104 (recur
              (+ 2 pointer)
              memory
              (memory (+ 1 pointer)))))))

(def answer (op-code 1 tv))

;9025675

;part b

(defn op-code-2 [input memory]
  (loop [pointer 0
         memory memory
         exit-code 0]
    (let [instruction (memory (+ 0 pointer))
          pm1 (memory (+ 1 pointer))
          im1 (+ 1 pointer)
          pm2 (memory (+ 2 pointer))
          im2 (+ 2 pointer)
          pm3 (memory (+ 3 pointer))
          im3 (+ 3 pointer)]
      (case instruction
        99 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory pm1) (memory (memory (+ 2 pointer)))))
            exit-code)
        101 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1001 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory pm1) (memory (+ 2 pointer))))
               exit-code)
        1101 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory pm1) (memory (memory (+ 2 pointer)))))
            exit-code)
        102 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code)
        1002 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory pm1) (memory (+ 2 pointer))))
               exit-code)
        1102 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code)
        3 (recur
            (+ 2 pointer)
            (assoc memory (memory (+ 1 pointer)) input)
            exit-code)
        4 (recur
            (+ 2 pointer)
            memory
            (memory pm1))
        104 (recur
              (+ 2 pointer)
              memory
              (memory (+ 1 pointer)))
        5 (recur
            (if (= 0 (memory pm1))
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
               (if (= 0 (memory pm1))
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
            (if (not= 0 (memory pm1))
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
               (if (not= 0 (memory pm1))
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
            (if (< (memory pm1) (memory (memory (+ 2 pointer))))
              (assoc memory (memory (memory (+ 3 pointer))) 1)
              (assoc memory (memory (memory (+ 3 pointer))) 0))
            exit-code)
        107 (recur
              (+ 4 pointer)
              (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (memory (+ 3 pointer))) 1)
                (assoc memory (memory (memory (+ 3 pointer))) 0))
              exit-code)))))
