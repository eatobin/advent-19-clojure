(ns advent.day01b-test
  (:require [clojure.test :refer [deftest is]]
            [advent.day01b :as day01b]
            [malli.core :as m]
            [malli.generator :as mg]))

(def =>fuel-plus
  (m/schema
    day01b/=>fuel-plus
    {::m/function-checker mg/function-checker}))

(m/validate =>fuel-plus day01b/fuel-plus)
(m/validate =>fuel-plus (str 88))

(m/explain =>fuel-plus day01b/fuel-plus)
(m/explain =>fuel-plus (str 88))

(deftest fuel-plus-test
  (is (= 2
         (day01b/fuel-plus 14)))
  (is (= 966
         (day01b/fuel-plus 1969)))
  (is (= 50346
         (day01b/fuel-plus 100756))))
