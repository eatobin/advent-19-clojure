(ns eatobin.library-test
  (:require
   ;; sut = system under test
   [clojure.test :refer [deftest is testing]]
   [eatobin.library :as sut]))

(def instruction1 {:a 0, :b 0, :c 0, :d 0, :e 6})
(def instruction3 {:a 0, :b 0, :c 4, :d 5, :e 6})
(def instruction5 {:a 2, :b 3, :c 4, :d 5, :e 6})

(def memory-as-csv-string "10,11,1")
(def this-memory {0 10, 1 11, 2 1})

(deftest a-test-alone
  (testing "a test all by itself"
    (is (= 78 78))))

(deftest make-instruction-1
  (testing "make an Instruction 1"
    (is (= (sut/make-instruction 6) instruction1))))

(deftest make-instruction-3
  (testing "make an Instruction 3"
    (is (= (sut/make-instruction 456) instruction3))))

(deftest make-instruction-5
  (testing "make an Instruction 5"
    (is (= (sut/make-instruction 23456) instruction5))))

(deftest make-memory
  (testing "make a memory"
    (is (= (sut/make-memory memory-as-csv-string) this-memory))))
