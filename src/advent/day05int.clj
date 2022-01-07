(ns advent.day05int
  (:require [advent.intcode :as ic]))

;part a
(def tv (ic/make-tv "resources/day05.csv"))

(def answer ((ic/op-code {:input         1
                          :output        0
                          :phase         -1
                          :pointer       0
                          :relative-base 0
                          :memory        tv
                          :stopped?      false
                          :recur?        true})
             :output))

(println answer) answer

;9025675

;part b
(def answer-2 ((ic/op-code {:input         5
                            :output        0
                            :phase         -1
                            :pointer       0
                            :relative-base 0
                            :memory        tv
                            :stopped?      false
                            :recur?        true})
               :output))

(println answer-2)

;11981754
