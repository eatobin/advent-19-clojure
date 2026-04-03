(ns lib-test
  (:require [clojure.test :refer [deftest is testing]]
   ; system under test
            [lib :as sut]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest add-5
  (testing "What? add-5??"
    (is (= 10 (sut/add-5 5)))))
