(ns eatobin.day07
  (:require
   [eatobin.intcode :as ic]))

;part a
(def memory (ic/make-memory "resources/day07.csv"))

(def possibles (into ()
                     (reverse
                      (for [a (range 0 5)
                            b (range 0 5)
                            c (range 0 5)
                            d (range 0 5)
                            e (range 0 5)
                            :when (distinct? a b c d e)]
                        [a b c d e]))))

(defn pass [i-code-memory [a b c d e]]
  (let [op-a {:input         0
              :output        []
              :phase         a
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-b {:input         (last ((ic/op-code op-a) :output))
              :output        []
              :phase         b
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-c {:input         (last ((ic/op-code op-b) :output))
              :output        []
              :phase         c
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-d {:input         (last ((ic/op-code op-c) :output))
              :output        []
              :phase         d
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-e {:input         (last ((ic/op-code op-d) :output))
              :output        []
              :phase         e
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}]
    (last ((ic/op-code op-e) :output))))

(defn passes [i-code-memory]
  (map #(pass i-code-memory %) possibles))

(def answer (apply max (passes memory)))

(comment
  answer)

;368584

;part b
(def possibles-2 (into ()
                       (reverse
                        (for [a (range 5 10)
                              b (range 5 10)
                              c (range 5 10)
                              d (range 5 10)
                              e (range 5 10)
                              :when (distinct? a b c d e)]
                          [a b c d e]))))

(defn to-amps-list [phases-vector memory]
  (letfn [(to-amps [phases]
            {1 (atom {:input 0 :output [] :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             2 (atom {:input nil :output [] :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             3 (atom {:input nil :output [] :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             4 (atom {:input nil :output [] :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})
             5 (atom {:input nil :output [] :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false})})]
    (map to-amps phases-vector)))

(defn runner [five-amps]
  (loop [amps           five-amps
         current-amp-no 1
         next-amp-no    (+ 1 (mod current-amp-no 5))]
    (if (and (= 5 current-amp-no) (:stopped? @(amps current-amp-no)))
      (last (:output @(amps current-amp-no)))
      (do (swap! (amps current-amp-no) ic/op-code)
          (swap! (amps next-amp-no) assoc :input (last (:output @(amps current-amp-no))))
          (recur
           (assoc amps current-amp-no (amps current-amp-no) next-amp-no (amps next-amp-no))
           next-amp-no
           (+ 1 (mod next-amp-no 5)))))))

(def answer-2 (apply max (map runner (to-amps-list possibles-2 memory))))

(comment
  answer-2)

;35993240

(comment
  (def mv [((vec possibles-2) 0)])
  mv
  [[5 6 7 8 9]]
  (def five-amps (first (to-amps-list mv memory)))
  (runner five-amps)
  33807717)

(comment
  (to-amps-list
   [[5 6 7 8 9]]
   {0 3, 1 15, 2 3, 3 16, 4 1002, 5 16, 6 10, 7 16, 8 1, 9 16, 10 15, 11 15, 12 4, 13 15, 14 99, 15 0, 16 0}))

(defn pass-2 [i-code-memory [a b c d e]]
  (let [op-a {:input         0
              :output        []
              :phase         a
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-b {:input         (last ((ic/op-code op-a) :output))
              :output        []
              :phase         b
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-c {:input         (last ((ic/op-code op-b) :output))
              :output        []
              :phase         c
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-d {:input         (last ((ic/op-code op-c) :output))
              :output        []
              :phase         d
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}
        op-e {:input         (last ((ic/op-code op-d) :output))
              :output        []
              :phase         e
              :pointer       0
              :relative-base 0
              :memory        i-code-memory
              :stopped?      false
              :recur?        true}]
    (last ((ic/op-code op-e) :output))))

(defn to-amps-list-2 [a-single-phases-vector memory]
  (into {}
        (letfn [(to-amps [phases]
                  {1 {:input 0 :output [] :phase (phases 0) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false}
                   2 {:input nil :output [] :phase (phases 1) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false}
                   3 {:input nil :output [] :phase (phases 2) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false}
                   4 {:input nil :output [] :phase (phases 3) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false}
                   5 {:input nil :output [] :phase (phases 4) :pointer 0 :relative-base 0 :memory memory :stopped? false :recur? false}})]
          (map to-amps a-single-phases-vector))))

(comment
  (to-amps-list-2
   [[5 6 7 8 9]]
   {0 3, 1 15, 2 3, 3 16, 4 1002, 5 16, 6 10, 7 16, 8 1, 9 16, 10 15, 11 15, 12 4, 13 15, 14 99, 15 0, 16 0}))

;Here are some example programs:
;
;Max thruster signal 139629729 (from phase setting sequence 9,8,7,6,5):
;
;3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5

;Max thruster signal 18216 (from phase setting sequence 9,7,8,5,6):
;
;3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10
