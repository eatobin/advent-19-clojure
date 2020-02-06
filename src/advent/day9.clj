(ns advent.day9
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a

(def tv (->> (first (with-open [reader (io/reader "resources/day9.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn op-code [phase input memory]
  (loop [pointer 0
         memory memory
         exit-code 0
         relative-base 0]
    (let [instruction (memory pointer)]
      (case instruction
        99 exit-code
        1 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            exit-code
            relative-base)
        101 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code
              relative-base)
        1001 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               exit-code
               relative-base)
        1101 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code
               relative-base)
        2 (recur
            (+ 4 pointer)
            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
            exit-code
            relative-base)
        102 (recur
              (+ 4 pointer)
              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
              exit-code
              relative-base)
        1002 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
               exit-code
               relative-base)
        1102 (recur
               (+ 4 pointer)
               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
               exit-code
               relative-base)
        3 (recur
            (+ 2 pointer)
            (if (= 0 pointer)
              (assoc memory (memory (+ 1 pointer)) phase)
              (assoc memory (memory (+ 1 pointer)) input))
            exit-code
            relative-base)
        4 (recur
            (+ 2 pointer)
            memory
            (memory (memory (+ 1 pointer)))
            relative-base)
        104 (recur
              (+ 2 pointer)
              memory
              (memory (+ 1 pointer))
              relative-base)
        5 (recur
            (if (= 0 (memory (memory (+ 1 pointer))))
              (+ 3 pointer)
              (memory (memory (+ 2 pointer))))
            memory
            exit-code
            relative-base)
        105 (recur
              (if (= 0 (memory (+ 1 pointer)))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory
              exit-code
              relative-base)
        1005 (recur
               (if (= 0 (memory (memory (+ 1 pointer))))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code
               relative-base)
        1105 (recur
               (if (= 0 (memory (+ 1 pointer)))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code
               relative-base)
        6 (recur
            (if (not= 0 (memory (memory (+ 1 pointer))))
              (+ 3 pointer)
              (memory (memory (+ 2 pointer))))
            memory
            exit-code
            relative-base)
        106 (recur
              (if (not= 0 (memory (+ 1 pointer)))
                (+ 3 pointer)
                (memory (memory (+ 2 pointer))))
              memory
              exit-code
              relative-base)
        1006 (recur
               (if (not= 0 (memory (memory (+ 1 pointer))))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code
               relative-base)
        1106 (recur
               (if (not= 0 (memory (+ 1 pointer)))
                 (+ 3 pointer)
                 (memory (+ 2 pointer)))
               memory
               exit-code
               relative-base)
        7 (recur
            (+ 4 pointer)
            (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            exit-code
            relative-base)
        107 (recur
              (+ 4 pointer)
              (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))
              exit-code
              relative-base)
        1007 (recur
               (+ 4 pointer)
               (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code
               relative-base)
        1107 (recur
               (+ 4 pointer)
               (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code
               relative-base)
        10007 (recur
                (+ 4 pointer)
                (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        10107 (recur
                (+ 4 pointer)
                (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        11007 (recur
                (+ 4 pointer)
                (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        11107 (recur
                (+ 4 pointer)
                (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        8 (recur
            (+ 4 pointer)
            (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
              (assoc memory (memory (+ 3 pointer)) 1)
              (assoc memory (memory (+ 3 pointer)) 0))
            exit-code
            relative-base)
        108 (recur
              (+ 4 pointer)
              (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))
              exit-code
              relative-base)
        1008 (recur
               (+ 4 pointer)
               (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code
               relative-base)
        1108 (recur
               (+ 4 pointer)
               (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                 (assoc memory (memory (+ 3 pointer)) 1)
                 (assoc memory (memory (+ 3 pointer)) 0))
               exit-code
               relative-base)
        10008 (recur
                (+ 4 pointer)
                (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        10108 (recur
                (+ 4 pointer)
                (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        11008 (recur
                (+ 4 pointer)
                (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        11108 (recur
                (+ 4 pointer)
                (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0))
                exit-code
                relative-base)
        9 (recur
            (+ 2 pointer)
            memory
            exit-code
            (+ (memory (memory (+ 1 pointer))) relative-base))
        109 (recur
              (+ 2 pointer)
              memory
              exit-code
              (+ (memory (+ 1 pointer)) relative-base))))))
