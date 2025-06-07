; clj -X clojure.core.server/start-server :name repl :port 5555 :accept clojure.core.server/repl :server-daemon false
; clojure -M:repl/rebel-nrepl:eat/malli:eat/test

(ns day02.malli
  (:require
    ;     [malli.error :as me]
    ;     [malli.generator :as mg]
    [malli.core :as m]
    [malli.dev :as dev]
    [malli.instrument :as mi]
    [sci.core :as sci]))

(m/validate string? "kikka")
(sci/eval-string "(println \"hello\")" {:namespaces {'clojure.core {'println println}}})

(def my-schema
  [:and
   [:map
    [:x int?]
    [:y int?]]
   [:fn '(fn [{:keys [x y]}] (> x y))]])
((fn [coll] (= (count coll) 3)) [4 5 6])

(def your-schema
  [:and
   [vector?]
   [:fn '(fn [coll] (= (count coll) 3))]])
(m/validate your-schema [44 55 66])
(m/validate your-schema '(44 55 66))

(m/validate my-schema {:x 1, :y 0})
; => true

(m/validate my-schema {:x 1, :y 2})
; => false


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
(m/=> plus1 [:=> [:cat [:int {:max 5}]] [:int {:max 6}]])
;(m/=> plus1 [:=> [:cat :int] [:int {:max 6}]])

(dev/start!)
; malli: instrumented 1 function var
; malli: dev-mode started

(comment
  #_:clj-kondo/ignore
  (plus1 "6"))
; =throws=> :malli.core/invalid-input {:input [:cat :int], :args ["6"], :schema [:=> [:cat :int] [:int {:max 6}]]}

(comment
  (plus1 6))
; =throws=> :malli.core/invalid-output {:output [:int {:max 6}], :value 9, :args [8], :schema [:=> [:cat :int] [:int {:max 6}]]}

;(m/=> plus1 [:=> [:cat :int] :int])
; =stdout=> ..instrumented #'user/plus1

(mi/check)
(m/function-schemas)

(comment
  (dev/stop!))
; malli: unstrumented 1 function vars
; malli: dev-mode stopped
