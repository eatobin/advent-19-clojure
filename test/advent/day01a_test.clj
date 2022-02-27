(ns advent.day01a-test
  (:require [clojure.test :refer [deftest is]]
            [advent.day01a :as day01a]
            [malli.core :as m]
            [malli.generator :as mg]))

(def =>fuel
  (m/schema
    day01a/=>fuel
    {::m/function-checker mg/function-checker}))

(m/validate =>fuel day01a/fuel)
(m/validate =>fuel (str 88))

(m/explain =>fuel day01a/fuel)
(m/explain =>fuel (str 88))

(deftest fuel-test
  (is (= 2
         (day01a/fuel 12)))
  (is (= 2
         (day01a/fuel 14)))
  (is (= 654
         (day01a/fuel 1969)))
  (is (= 33583
         (day01a/fuel 100756))))
