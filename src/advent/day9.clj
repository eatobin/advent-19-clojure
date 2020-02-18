(ns advent.day9
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;part a

(def tv (->> (first (with-open [reader (io/reader "resources/day9.csv")]
                      (doall
                        (csv/read-csv reader))))
             (map #(Integer/parseInt %))
             (into [])
             (zipmap (range))))

(def sample [109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99])
(def good (zipmap (range) sample))

(def sample-2 [1102, 34915192, 34915192, 7, 4, 7, 99, 0])
(def good-2 (zipmap (range) sample-2))

(def sample-3 [104, 1125899906842624, 99])
(def good-3 (zipmap (range) sample-3))


(defn pad-5 [n]
  (zipmap [:a :b :c :d :e]
          (for [n (format "%05d" n)]
            (- (byte n) 48))))

(defn param-mode-c [instruction pointer memory relative-base]
  (case (instruction :c)
    0 (get memory (memory (+ 1 pointer)) 0)
    1 (memory (+ 1 pointer))
    2 (get memory (+ (memory (+ 1 pointer)) relative-base) 0)))

(defn param-mode-b [instruction pointer memory relative-base]
  (case (instruction :b)
    0 (get memory (memory (+ 2 pointer)) 0)
    1 (memory (+ 2 pointer))
    2 (get memory (+ (memory (+ 2 pointer)) relative-base) 0)))

(defn param-mode-a [instruction pointer memory relative-base]
  (case (instruction :a)
    0 (memory (+ 3 pointer))
    2 (get memory (+ (memory (+ 3 pointer)) relative-base) 0)))

(defn op-code [[input phase pointer relative-base memory stopped?]]
  (if stopped?
    [input phase pointer relative-base memory true]
    (loop [pointer pointer
           relative-base relative-base
           memory memory]
      (let [instruction (pad-5 (memory pointer))]
        (case (instruction :e)
          9 (if (= (instruction :d) 9)
              [input phase pointer relative-base memory true]
              (recur
                (+ 2 pointer)
                (+ (param-mode-c instruction pointer memory relative-base) relative-base)
                memory))
          1 (recur
              (+ 4 pointer)
              relative-base
              (assoc memory (param-mode-a instruction pointer memory relative-base)
                            (+ (param-mode-c instruction pointer memory relative-base)
                               (param-mode-b instruction pointer memory relative-base))))
          2 (recur
              (+ 4 pointer)
              relative-base
              (assoc memory (param-mode-a instruction pointer memory relative-base)
                            (* (param-mode-c instruction pointer memory relative-base)
                               (param-mode-b instruction pointer memory relative-base))))
          3 (recur
              (+ 2 pointer)
              relative-base
              (if (= 0 pointer)
                (assoc memory (memory (+ 1 pointer)) phase)
                (assoc memory (memory (+ 1 pointer)) input)))
          4 [(param-mode-c instruction pointer memory relative-base) phase (+ 2 pointer) relative-base memory false]
          5 (recur
              (if (= 0 (param-mode-c instruction pointer memory relative-base))
                (+ 3 pointer)
                (param-mode-b instruction pointer memory relative-base))
              relative-base
              memory)
          6 (recur
              (if (not= 0 (param-mode-c instruction pointer memory relative-base))
                (+ 3 pointer)
                (param-mode-b instruction pointer memory relative-base))
              relative-base
              memory)
          7 (recur
              (+ 4 pointer)
              relative-base
              (if (< (param-mode-c instruction pointer memory relative-base) (param-mode-b instruction pointer memory relative-base))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0)))
          8 (recur
              (+ 4 pointer)
              relative-base
              (if (= (param-mode-c instruction pointer memory relative-base) (param-mode-b instruction pointer memory relative-base))
                (assoc memory (memory (+ 3 pointer)) 1)
                (assoc memory (memory (+ 3 pointer)) 0))))))))
