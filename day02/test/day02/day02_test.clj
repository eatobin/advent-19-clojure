(ns day02.day02-test
  (:require [clojure.test :refer [deftest is testing]]
            [day02.day02 :as sut]))                         ; system under test

(deftest a-test
  (testing "This is a stand-alone test."
    (is (= 1 1))))

(deftest make-intcode
  (testing "make-intcode makes an intcode"
    (is (= (sut/make-intcode 0 "10,11,12") {:pointer 0 :memory {0 10 1 11 2 12}}))))

(deftest make-instruction-1
  (testing "make-instruction makes a 1 digit instruction"
    (is (= (sut/make-instruction 1) {:a 0, :b 0, :c 0, :d 0, :e 1}))))

(deftest make-instruction-5
  (testing "make-instruction makes a 5 digit instruction"
    (is (= (sut/make-instruction 12345) {:a 1, :b 2, :c 3, :d 4, :e 5}))))
