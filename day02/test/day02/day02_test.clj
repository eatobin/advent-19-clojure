(ns day02.day02-test
  (:require [clojure.test :refer [deftest is testing]]
            [day02.library :as sut]))                       ; system under test

(def instruction-1 {:a 0, :b 0, :c 0, :d 0, :e 6})
(def instruction-3 {:a 0, :b 0, :c 4, :d 5, :e 6})
(def instruction-5 {:a 2, :b 3, :c 4, :d 5, :e 6})
(def memory-as-csv-string "10,11,1")
(def this-memory [10 11 1])
(def intCode {:pointer 0
              :memory  this-memory
              :actions '()})
(def this-memory-x [0 3 2 33])



(deftest a-stand-alone-test
  (testing "This is a stand-alone test."
    (is (=
         1
         1))))

(deftest make-instruction-1
  (testing "make-instruction makes a 1 digit instruction"
    (is (=
         instruction-1
         (sut/make-instruction 6)))))

(deftest make-instruction-3
  (testing "make-instruction makes a 3 digit instruction"
    (is (=
         instruction-3
         (sut/make-instruction 456)))))

(deftest make-instruction-5
  (testing "make-instruction makes a 5 digit instruction"
    (is (=
         instruction-5
         (sut/make-instruction 23456)))))

;(deftest -p-w
;  (testing "-p-w"
;    (is (= 1
;           (sut/-p-w intCode 2)))))
;
;(deftest -p-r
;  (testing "-p-r"
;    (is (=
;         11
;         (sut/-p-r intCode 2)))))
