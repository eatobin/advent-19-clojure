(ns day02.day02-test
  (:require [clojure.test :refer [deftest is testing]]
            [day02.day02 :as sut]))                         ; system under test

(deftest a-test
  (testing "This is a stand-alone test."
    (is (= 1 1))))

(deftest make-intcode
  (testing "make-intcode makes an intcode"
    (is (= (sut/make-intcode 0 "10,11,12") {:pointer 0 :memory {0 10 1 11 2 12}}))))
