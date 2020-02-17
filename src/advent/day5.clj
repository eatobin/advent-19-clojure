(ns advent.day5
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a
(def tv (->> (first (with-open [reader (io/reader "resources/day5.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))

(defn param-mode-c [instruction pointer memory]
  (case (instruction :c)
    0 (memory (memory (+ 1 pointer)))
    1 (memory (+ 1 pointer))))

(defn param-mode-b [instruction pointer memory]
  (case (instruction :b)
    0 (memory (memory (+ 2 pointer)))
    1 (memory (+ 2 pointer))))

(defn param-mode-a [instruction pointer memory]
  (case (instruction :a)
    0 (memory (+ 3 pointer))))

(def tester [3, 0, 4, 0, 99])
(def tester-2 [1002, 4, 3, 4, 33])

(defn op-code [[input memory]]
  (loop [input input
         memory memory
         pointer 0]
    (let [instruction (pad-5 (memory pointer))]
      (case (instruction :e)
        9 (if (= (instruction :d) 9)
            input
            memory)
        1 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (+ (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        2 (recur
            input
            (assoc memory (param-mode-a instruction pointer memory)
                          (* (param-mode-c instruction pointer memory)
                             (param-mode-b instruction pointer memory)))
            (+ 4 pointer))
        3 (recur
            input
            (assoc memory (memory (+ 1 pointer)) input)
            (+ 2 pointer))
        4 (recur
            (memory (memory (+ 1 pointer)))
            memory
            (+ 2 pointer))))))

(def answer (op-code [1 tv]))

(println answer) answer

;9025675

;part b

;(defn op-code-2 [input memory]
;  (loop [pointer 0
;         memory memory
;         input 0]
;    (let [instruction (memory pointer)]
;      (case instruction
;        99 input
;        1 (recur
;            (+ 4 pointer)
;            (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
;            input)
;        101 (recur
;              (+ 4 pointer)
;              (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
;              input)
;        1001 (recur
;               (+ 4 pointer)
;               (assoc memory (memory (+ 3 pointer)) (+ (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
;               input)
;        1101 (recur
;               (+ 4 pointer)
;               (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer))))
;               input)
;        2 (recur
;            (+ 4 pointer)
;            (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer)))))
;            input)
;        102 (recur
;              (+ 4 pointer)
;              (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (memory (+ 2 pointer)))))
;              input)
;        1002 (recur
;               (+ 4 pointer)
;               (assoc memory (memory (+ 3 pointer)) (* (memory (memory (+ 1 pointer))) (memory (+ 2 pointer))))
;               input)
;        1102 (recur
;               (+ 4 pointer)
;               (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer))))
;               input)
;        3 (recur
;            (+ 2 pointer)
;            (assoc memory (memory (+ 1 pointer)) input)
;            input)
;        4 (recur
;            (+ 2 pointer)
;            memory
;            (memory (memory (+ 1 pointer))))
;        104 (recur
;              (+ 2 pointer)
;              memory
;              (memory (+ 1 pointer)))
;        5 (recur
;            (if (= 0 (memory (memory (+ 1 pointer))))
;              (+ 3 pointer)
;              (memory (memory (+ 2 pointer))))
;            memory
;            input)
;        105 (recur
;              (if (= 0 (memory (+ 1 pointer)))
;                (+ 3 pointer)
;                (memory (memory (+ 2 pointer))))
;              memory
;              input)
;        1005 (recur
;               (if (= 0 (memory (memory (+ 1 pointer))))
;                 (+ 3 pointer)
;                 (memory (+ 2 pointer)))
;               memory
;               input)
;        1105 (recur
;               (if (= 0 (memory (+ 1 pointer)))
;                 (+ 3 pointer)
;                 (memory (+ 2 pointer)))
;               memory
;               input)
;        6 (recur
;            (if (not= 0 (memory (memory (+ 1 pointer))))
;              (+ 3 pointer)
;              (memory (memory (+ 2 pointer))))
;            memory
;            input)
;        106 (recur
;              (if (not= 0 (memory (+ 1 pointer)))
;                (+ 3 pointer)
;                (memory (memory (+ 2 pointer))))
;              memory
;              input)
;        1006 (recur
;               (if (not= 0 (memory (memory (+ 1 pointer))))
;                 (+ 3 pointer)
;                 (memory (+ 2 pointer)))
;               memory
;               input)
;        1106 (recur
;               (if (not= 0 (memory (+ 1 pointer)))
;                 (+ 3 pointer)
;                 (memory (+ 2 pointer)))
;               memory
;               input)
;        7 (recur
;            (+ 4 pointer)
;            (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
;              (assoc memory (memory (+ 3 pointer)) 1)
;              (assoc memory (memory (+ 3 pointer)) 0))
;            input)
;        107 (recur
;              (+ 4 pointer)
;              (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
;                (assoc memory (memory (+ 3 pointer)) 1)
;                (assoc memory (memory (+ 3 pointer)) 0))
;              input)
;        1007 (recur
;               (+ 4 pointer)
;               (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
;                 (assoc memory (memory (+ 3 pointer)) 1)
;                 (assoc memory (memory (+ 3 pointer)) 0))
;               input)
;        1107 (recur
;               (+ 4 pointer)
;               (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
;                 (assoc memory (memory (+ 3 pointer)) 1)
;                 (assoc memory (memory (+ 3 pointer)) 0))
;               input)
;        10007 (recur
;                (+ 4 pointer)
;                (if (< (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        10107 (recur
;                (+ 4 pointer)
;                (if (< (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        11007 (recur
;                (+ 4 pointer)
;                (if (< (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        11107 (recur
;                (+ 4 pointer)
;                (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        8 (recur
;            (+ 4 pointer)
;            (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
;              (assoc memory (memory (+ 3 pointer)) 1)
;              (assoc memory (memory (+ 3 pointer)) 0))
;            input)
;        108 (recur
;              (+ 4 pointer)
;              (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
;                (assoc memory (memory (+ 3 pointer)) 1)
;                (assoc memory (memory (+ 3 pointer)) 0))
;              input)
;        1008 (recur
;               (+ 4 pointer)
;               (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
;                 (assoc memory (memory (+ 3 pointer)) 1)
;                 (assoc memory (memory (+ 3 pointer)) 0))
;               input)
;        1108 (recur
;               (+ 4 pointer)
;               (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
;                 (assoc memory (memory (+ 3 pointer)) 1)
;                 (assoc memory (memory (+ 3 pointer)) 0))
;               input)
;        10008 (recur
;                (+ 4 pointer)
;                (if (= (memory (memory (+ 1 pointer))) (memory (memory (+ 2 pointer))))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        10108 (recur
;                (+ 4 pointer)
;                (if (= (memory (+ 1 pointer)) (memory (memory (+ 2 pointer))))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        11008 (recur
;                (+ 4 pointer)
;                (if (= (memory (memory (+ 1 pointer))) (memory (+ 2 pointer)))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)
;        11108 (recur
;                (+ 4 pointer)
;                (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
;                  (assoc memory (memory (+ 3 pointer)) 1)
;                  (assoc memory (memory (+ 3 pointer)) 0))
;                input)))))
;
;(def answer-2 (op-code-2 5 tv))
;
;(println answer-2)

;11981754
