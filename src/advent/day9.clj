(ns advent.day9
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a

(def tv (->> (first (with-open [reader (io/reader "resources/day7.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])))

(def sample [109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99])

(def good (zipmap (range) sample))
(sort-by key < good)
(assoc good 100 700)
(sort-by key < (assoc good 100 700))
(good 100)
(get good 100 0)
(get sample 100 0)
(get sample 0 0)

;(get memory (+ (memory (+ 1 pointer)) relative-base) 0)

(defn op-code [[input phase pointer relative-base memory stopped?]]
  (if stopped?
    [input phase pointer relative-base memory true]
    (loop [pointer pointer
           relative-base relative-base
           memory memory]
      (let [instruction (memory pointer)]
        (case instruction
          99 [input phase pointer relative-base memory true]
          1 (recur
              (+ 4 pointer)
              relative-base
              (assoc memory (memory (+ 3 pointer)) (+ (get memory (memory (+ 1 pointer)) 0) (memory (get memory (memory (+ 2 pointer)) 0)))))
          101 (recur
                (+ 4 pointer)
                relative-base
                (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (get memory (memory (+ 2 pointer)) 0)))))
          201 (recur
                (+ 4 pointer)
                relative-base
                (assoc memory (memory (+ 3 pointer)) (+ (get memory (+ (memory (+ 1 pointer)) relative-base) 0) (get memory (memory (+ 2 pointer)) 0))))
          1001 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (+ (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))))
          2001 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (+ (get memory (memory (+ 1 pointer)) 0) (get memory (+ (memory (+ 2 pointer)) relative-base) 0))))
          1101 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (+ (memory (+ 1 pointer)) (memory (+ 2 pointer)))))
          2201 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (+ (get memory (+ (memory (+ 1 pointer)) relative-base) 0) (get memory (+ (memory (+ 2 pointer)) relative-base) 0))))
          2 (recur
              (+ 4 pointer)
              relative-base
              (assoc memory (memory (+ 3 pointer)) (* (get memory (memory (+ 1 pointer)) 0) (get memory (memory (+ 2 pointer)) 0))))
          102 (recur
                (+ 4 pointer)
                relative-base
                (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (get memory (memory (+ 2 pointer)) 0))))
          1002 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (* (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))))
          1102 (recur
                 (+ 4 pointer)
                 relative-base
                 (assoc memory (memory (+ 3 pointer)) (* (memory (+ 1 pointer)) (memory (+ 2 pointer)))))
          3 (recur
              (+ 2 pointer)
              relative-base
              (if (= 0 pointer)
                (assoc memory (memory (+ 1 pointer)) phase)
                (assoc memory (memory (+ 1 pointer)) input)))
          4 [(memory (memory (+ 1 pointer))) phase (+ 2 pointer) relative-base memory false]
          104 [(memory (+ 1 pointer)) phase (+ 2 pointer) relative-base memory false]
          204 [(memory (+ (memory (+ 1 pointer)) relative-base)) phase (+ 2 pointer) relative-base memory false]
          5 (recur
              (if (= 0 (get memory (memory (+ 1 pointer)) 0))
                (+ 3 pointer)
                (get memory (memory (+ 2 pointer)) 0))
              relative-base
              memory)
          105 (recur
                (if (= 0 (memory (+ 1 pointer)))
                  (+ 3 pointer)
                  (get memory (memory (+ 2 pointer)) 0))
                relative-base
                memory)
          1005 (recur
                 (if (= 0 (get memory (memory (+ 1 pointer)) 0))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 relative-base
                 memory)
          1105 (recur
                 (if (= 0 (memory (+ 1 pointer)))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 relative-base
                 memory)
          6 (recur
              (if (not= 0 (get memory (memory (+ 1 pointer)) 0))
                (+ 3 pointer)
                (get memory (memory (+ 2 pointer)) 0))
              relative-base
              memory)
          106 (recur
                (if (not= 0 (memory (+ 1 pointer)))
                  (+ 3 pointer)
                  (get memory (memory (+ 2 pointer)) 0))
                relative-base
                memory)
          1006 (recur
                 (if (not= 0 (get memory (memory (+ 1 pointer)) 0))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 relative-base
                 memory)
          1106 (recur
                 (if (not= 0 (memory (+ 1 pointer)))
                   (+ 3 pointer)
                   (memory (+ 2 pointer)))
                 relative-base
                 memory)
          7 (recur
              (+ 4 pointer)
              relative-base
              (if (< (get memory (memory (+ 1 pointer)) 0) (get memory (memory (+ 2 pointer)) 0))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          107 (recur
                (+ 4 pointer)
                relative-base
                (if (< (memory (+ 1 pointer)) (get memory (memory (+ 2 pointer)) 0))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0)))
          1007 (recur
                 (+ 4 pointer)
                 relative-base
                 (if (< (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          1107 (recur
                 (+ 4 pointer)
                 relative-base
                 (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          10007 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (< (get memory (memory (+ 1 pointer)) 0) (get memory (memory (+ 2 pointer)) 0))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          10107 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (< (memory (+ 1 pointer)) (get memory (memory (+ 2 pointer)) 0))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11007 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (< (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11107 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (< (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          8 (recur
              (+ 4 pointer)
              relative-base
              (if (= (get memory (memory (+ 1 pointer)) 0) (get memory (memory (+ 2 pointer)) 0))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          108 (recur
                (+ 4 pointer)
                relative-base
                (if (= (memory (+ 1 pointer)) (get memory (memory (+ 2 pointer)) 0))
                  (assoc memory (memory (+ 3 pointer)) 1)
                  (assoc memory (memory (+ 3 pointer)) 0)))
          1008 (recur
                 (+ 4 pointer)
                 relative-base
                 (if (= (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          1108 (recur
                 (+ 4 pointer)
                 relative-base
                 (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                   (assoc memory (memory (+ 3 pointer)) 1)
                   (assoc memory (memory (+ 3 pointer)) 0)))
          10008 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (= (get memory (memory (+ 1 pointer)) 0) (get memory (memory (+ 2 pointer)) 0))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          10108 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (= (memory (+ 1 pointer)) (get memory (memory (+ 2 pointer)) 0))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11008 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (= (get memory (memory (+ 1 pointer)) 0) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          11108 (recur
                  (+ 4 pointer)
                  relative-base
                  (if (= (memory (+ 1 pointer)) (memory (+ 2 pointer)))
                    (assoc memory (memory (+ 3 pointer)) 1)
                    (assoc memory (memory (+ 3 pointer)) 0)))
          9 (recur
              (+ 2 pointer)
              (+ (get memory (memory (+ 1 pointer)) 0) relative-base)
              memory)
          109 (recur
                (+ 2 pointer)
                (+ (memory (+ 1 pointer)) relative-base)
                memory))))))
