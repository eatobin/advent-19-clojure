(ns advent.day01-test
  (:require [clojure.test :refer [deftest is]]
            [advent.domain :as dom]
            [advent.day01 :as day01]
            [clojure.spec.alpha :as s]))

(s/conform ::dom/module
           42)
(s/conform ::dom/fuel
           42)

(deftest fuel-test
  (is (= 2
         (day01/fuel 12)))
  (is (= 2
         (day01/fuel 14)))
  (is (= 654
         (day01/fuel 1969)))
  (is (= 33583
         (day01/fuel 100756))))
(s/conform ::dom/fuel
           (day01/fuel 12))

(deftest fuel-plus-test
  (is (= 2
         (day01/fuel-plus 14)))
  (is (= 966
         (day01/fuel-plus 1969)))
  (is (= 50346
         (day01/fuel-plus 100756))))
(s/conform ::dom/fuel
           (day01/fuel-plus 14))
