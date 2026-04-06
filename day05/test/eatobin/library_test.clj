(ns eatobin.library-test
  (:require
   [clojure.test :refer [deftest is testing]] ;; sut = system under test
   [eatobin.library :as sut]))

(def instruction1 {:a 0, :b 0, :c 0, :d 0, :e 6})

(deftest a-test-alone
  (testing "a test all by itself"
    (is (= 78 78))))

(deftest make-instruction
  (testing "make an Instruction 1"
    (is (= (sut/make-instruction 6) instruction1))))
