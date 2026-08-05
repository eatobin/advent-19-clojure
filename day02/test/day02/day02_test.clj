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
(def this-memory-add-mult [0 2 1 0])
(def aoc-memory-1 "1,0,0,3,99")
(def aoc-memory-2 "1,9,10,3,2,3,11,0,99,30,40,50")
(def aoc-memory-3 "1,0,0,0,99")
(def aoc-memory-4 "2,3,0,3,99")
(def aoc-memory-5 "2,4,4,5,99,0")
(def aoc-memory-6 "1,1,1,4,99,5,6,0,99")
(def intcode-add-mult-exit {:pointer 0
                            :memory  this-memory-add-mult
                            :actions '()})
(def intcode-add {:pointer 4
                  :memory  [3 2 1 0]
                  :actions '(:add)})
(def intcode-mult {:pointer 4
                   :memory  [2 2 1 0]
                   :actions '(:multiply)})
(def intcode-exit {:pointer 0
                   :memory  this-memory-add-mult
                   :actions '(:exit)})

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
             (sut/a-param instruction-1 {:pointer 0 :memory this-memory-x}))))
    (testing "lookup a valid bParam"
      (is (= 2
             (sut/b-param instruction-1 {:pointer 0 :memory this-memory-x}))))
    (testing "lookup a valid cParam"
      (is (= 33
             (sut/c-param instruction-1 {:pointer 0 :memory this-memory-x}))))
    (testing "lookup a valid cParam with a bad instruction"
      (is (thrown-with-msg? clojure.lang.ExceptionInfo #"no c-param match"
                            (sut/c-param instruction-5 {:pointer 0 :memory this-memory-x}))))))

(deftest opcode-actions
  (testing "add and multiply"
    (testing "1 plus 2 should be set at 0 and pointer should be 4"
      (is (= intcode-add
             (sut/add instruction-1 intcode-add-mult-exit))))
    (testing "1 times 2 should be set at 0 and pointer should be 4"
      (is (= intcode-mult
             (sut/multiply instruction-1 intcode-add-mult-exit))))
    (testing "exit should just add an Exit to the actions and return the intCode"
      (is (= intcode-exit
             (sut/exit intcode-add-mult-exit))))))


;; TODO
;; describe "\nAdd/Mult/Exit Tests" $ do
;; it "1 plus 2 should be set at 0 and pointer should be 4" $ do
;; add instruction1 intCodeAddMultExit `shouldBe `intCodeAdd
;; it "1 times 2 should be set at 0 and pointer should be 4" $ do
;; multiply instruction1 intCodeAddMultExit `shouldBe `intCodeMult
;; it "exit should just add an Exit to the actions and return the intCode" $ do
;; exit intCodeAddMultExit `shouldBe `intCodeExit
