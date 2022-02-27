(ns advent.day01a-test
  (:require [clojure.test :refer [deftest is]]
            [advent.day01a :as day01a]))

(deftest fuel-test
  (is (= 2
         (day01a/fuel 12)))
  (is (= 2
         (day01a/fuel 14)))
  (is (= 654
         (day01a/fuel 1969)))
  (is (= 33583
         (day01a/fuel 100756))))
