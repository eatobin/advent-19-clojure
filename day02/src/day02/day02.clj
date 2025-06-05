; clj -X clojure.core.server/start-server :name repl :port 5555 :accept clojure.core.server/repl :server-daemon false

(ns day02.day02
  (:require
 ;     [malli.error :as me]
 ;     [malli.generator :as mg]
   [malli.instrument :as mi]
 ;     [malli.error :as me]
   [malli.core :as m]
   [malli.dev :as dev]))

; (m/validate
;   [:map
;    [:x :int]
;    [::m/default [:map
;                  [:y :int]
;                  [::m/default [:map-of :int :int]]]]]
;   {:x 1, :y 2, 1 1, 2 2})
; ;; => true

; (m/validate
;   [:map [:x :int]]
;   {:x 1, :extra "key"})
; ;; => true

; (m/validate
;   [:map {:closed true} [:x :int]]
;   {:x 1, :extra "key"})
; ;; => false

; (defn kikka
;   "schema via var metadata"
;   {:malli/schema [:=> [:cat :int] :int]}
;   [x] (inc x))

; (defn kukka
;   "schema via separate declaration"
;   [x] (inc x))
; (m/=> kukka [:=> [:cat :int] :int])

; #_:clj-kondo/ignore
; (comment
;   (kikka 42)
;   (kikka 1 2)
;   (kukka 1 2)
;   (kikka "1")
;   (kukka "1")
;   (kikka 1.0)
;   (kikka 1))


; (def arg<ret
;   (m/schema
;     [:=>
;      [:cat :int]
;      :int
;      [:fn {:error/message "argument should be less than return"}
;       (fn [[[arg] ret]] (< arg ret))]]
;     {::m/function-checker mg/function-checker}))

; (m/explain arg<ret (fn [x] (inc x)))
; ;; => nil

; (m/explain arg<ret (fn [x] x))

; (me/humanize (m/explain arg<ret (fn [x] (inc x))))

; (me/humanize (m/explain arg<ret (fn [x] x)))

; (def pow
;   (m/-instrument
;     {:schema [:=> [:cat :int] [:int {:max 6}]]}
;     (fn [x] (* x x))))

; (comment
;   (pow 2)
;   ; => 4
;   (pow "2")
;   ; =throws=> :malli.core/invalid-input {:input [:cat :int], :args ["2"], :schema [:=> [:cat :int] [:int {:max 6}]]}
;   (pow 4)
;   ; =throws=> :malli.core/invalid-output {:output [:int {:max 6}], :value 16, :args [4], :schema [:=> [:cat :int] [:int {:max 6}]]}
;   (pow 4 2)
;   )

; (def small-int [:int {:max 6}])

; (defn plus1 [x] (inc x))
; ;(m/=> plus1 [:=> [:cat :int] small-int])
; (m/=> plus1 [:=> [:cat [:int {:max 5}]] [:int {:max 6}]])

; (m/function-schemas)

; (mi/instrument!)
; (comment (plus1 10))
; (mi/unstrument!)

; (plus1 10)

; (defn minus-me
;   "a normal clojure function, no dependencies to malli"
;   ;{:malli/schema [:=> [:cat :int] small-int]}
;   {:malli/schema [:=> [:cat [:int {:max 7}]] small-int]}
;   [x]
;   (dec x))

; (mi/collect!)
; ; => #{#'user/minus}

; (m/function-schemas)
; ;{user {plus1 {:schema [:=> [:cat :int] [:int {:max 6}]]
; ;              :ns user
; ;              :name plus1},
; ;       minus {:schema [:=> [:cat :int] [:int {:min 6}]]
; ;              :ns user
; ;              :name minus}}}

; (mi/instrument!)
; ; =stdout=> ..instrumented #'user/plus1
; ; =stdout=> ..instrumented #'user/minus

; (comment
;   (minus-me 6)
;   (minus-me 66)
;   )

; (mi/check)




; Development Instrumentation
; For better DX, there is malli.dev namespace.

; (require '[malli.dev :as dev])
; It's main entry points is dev/start!, taking same options as mi/instrument!. It runs mi/instrument! and mi/collect! (for all loaded namespaces) once and starts watching the function registry for changes. Any change that matches the filters will cause automatic re-instrumentation for the functions. dev/stop! removes all instrumentation and stops watching the registry.

(defn plus1 [x] (inc x))
(m/=> plus1 [:=> [:cat :int] [:int {:max 6}]])

(dev/start!)
; malli: instrumented 1 function var
; malli: dev-mode started

(comment
  (plus1 "6"))
; =throws=> :malli.core/invalid-input {:input [:cat :int], :args ["6"], :schema [:=> [:cat :int] [:int {:max 6}]]}

(comment
  (plus1 6))
; =throws=> :malli.core/invalid-output {:output [:int {:max 6}], :value 9, :args [8], :schema [:=> [:cat :int] [:int {:max 6}]]}

;; (m/=> plus1 [:=> [:cat :int] :int])
(m/=> plus1 [:=> [:cat [:int {:max 5}]] [:int {:max 6}]])
; =stdout=> ..instrumented #'user/plus1

(comment
  (plus1 6))
; => 7

(mi/check)
(m/function-schemas)

;; (dev/stop!)
; malli: unstrumented 1 function vars
; malli: dev-mode stopped




(def OFFSET-C 1)
(def OFFSET-B 2)
(def OFFSET-A 3)

;part a
(defn op-code [{:keys [pointer memory]}]
  (case (get memory pointer)
    1 (recur
       {:pointer (+ 4 pointer)
        :memory  (assoc
                  memory
                  (get memory (+ pointer OFFSET-A))
                  (+ (get memory (get memory (+ pointer OFFSET-C)))
                     (get memory (get memory (+ pointer OFFSET-B)))))})
    2 (recur
       {:pointer (+ 4 pointer)
        :memory  (assoc
                  memory
                  (get memory (+ pointer OFFSET-A))
                  (* (get memory (get memory (+ pointer OFFSET-C)))
                     (get memory (get memory (+ pointer OFFSET-B)))))})
    99 {:pointer pointer
        :memory  memory}))

(def memory [1, 0, 0, 3, 1, 1, 2, 3, 1, 3, 4, 3, 1, 5, 0, 3, 2, 10, 1, 19, 2, 9, 19, 23, 2, 13, 23, 27, 1, 6, 27, 31, 2, 6, 31, 35, 2, 13, 35, 39, 1, 39, 10, 43, 2, 43, 13, 47, 1, 9, 47, 51, 1, 51, 13, 55, 1, 55, 13, 59, 2, 59, 13, 63, 1, 63, 6, 67, 2, 6, 67, 71, 1, 5, 71, 75, 2, 6, 75, 79, 1, 5, 79, 83, 2, 83, 6, 87, 1, 5, 87, 91, 1, 6, 91, 95, 2, 95, 6, 99, 1, 5, 99, 103, 1, 6, 103, 107, 1, 107, 2, 111, 1, 111, 5, 0, 99, 2, 14, 0, 0])

(defn updated-memory [noun verb]
  (->
   memory
   (assoc 1 noun)
   (assoc 2 verb)))

(defn answer-a []
  (get (:memory (op-code {:pointer 0 :memory (updated-memory 12 2)})) 0))

(defn print-a
  "Invoke me with clojure -X day02.day02/print-a"
  [_]
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush))

;part b
(def noun-verb
  (vec (for [noun (range 0 100)
             verb (range 0 100)
             :let [candidate ((:memory (op-code {:pointer 0 :memory (updated-memory noun verb)})) 0)]
             :when (= candidate 19690720)]
         [candidate noun verb (+ (* 100 noun) verb)])))

(defn answer-b []
  (last (first noun-verb)))

(defn print-b
  "Invoke me with clojure -X day02.day02/print-b"
  [_]
  (printf "Part B answer: %s, correct: 8226%n" (answer-b))
  (flush))

(comment
  (print-a nil)
  (print-b nil))

(defn -main
  "Invoke me with clojure -M -m day02.day02"
  [& _]
  (printf "Part A answer: %s, correct: 2890696%n" (answer-a))
  (flush)
  (printf "Part B answer: %s, correct: 8226%n" (answer-b))
  (flush))

(comment
  (-main))
