(ns day02.day02-test
  (:require [clojure.test :refer [deftest is testing]]
            [day02.library :as sut]))                       ; system under test

(def instruction-1 {:a 0, :b 0, :c 0, :d 0, :e 6})
(def instruction-3 {:a 0, :b 0, :c 4, :d 5, :e 6})
(def instruction-5 {:a 2, :b 3, :c 4, :d 5, :e 6})
(def memory-as-csv-string "10,11,1")
(def this-memory [10 11 1])
(def intcode {:pointer 0
              :memory  this-memory
              :actions '()})
(def this-memory-x [0 3 2 33])
(def intcode-x {:pointer 0
                :memory  this-memory-x
                :actions '()})



(deftest a-stand-alone-test
  (testing "This is a stand-alone test."
    (is (=
         1
         1))))

(deftest make-instructions
  (testing "make various instructions"
    (testing "make-instruction makes a 1 digit instruction"
      (is (=
           instruction-1
           (sut/make-instruction 6))))
    (testing "make-instruction makes a 3 digit instruction"
      (is (=
           instruction-3
           (sut/make-instruction 456))))
    (testing "make-instruction makes a 5 digit instruction"
      (is (=
           instruction-5
           (sut/make-instruction 23456))))))

(deftest make-memory
  (testing "make a Memory"
    (is (=
         this-memory
         (sut/make-memory memory-as-csv-string)))))

(deftest memory-lookup
  (testing "lookup various memory locations"
    (testing "lookup a valid Memory index - -p-w"
      (is (= 1
             (sut/-p-w intcode 2))))
    (testing "lookup a valid Memory index - -p-r"
      (is (= 11
             (sut/-p-r intcode 2))))
    (testing "lookup an invalid Memory index - -p-w"
      (is (nil?
           (sut/-p-w intcode 33))))))

(deftest params-lookup
  (testing "lookup various params"
    (testing "lookup a valid aParam"
      (is (= 33
             (sut/a-param {:instruction instruction-1 :pointer 0 :memory this-memory-x}))))
    (testing "lookup a valid bParam"
      (is (= 2
             (sut/b-param {:instruction instruction-1 :pointer 0 :memory this-memory-x}))))
    (testing "lookup a valid cParam"
      (is (= 33
             (sut/c-param {:instruction instruction-1 :pointer 0 :memory this-memory-x}))))))
