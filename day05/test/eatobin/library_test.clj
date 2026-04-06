(ns eatobin.library-test
  (:require
   [clojure.test :refer [deftest is testing]] ;; sut = system under test
   [eatobin.library :as sut]))

(def instruction1 {:a 0, :b 0, :c 0, :d 0, :e 6})
(def instruction3 {:a 0, :b 0, :c 4, :d 5, :e 6})
(def instruction5 {:a 2, :b 3, :c 4, :d 5, :e 6})

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
