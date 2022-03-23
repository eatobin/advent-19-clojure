(ns advent.day01-test
  (:require [clojure.test :refer [deftest is]]
            [advent.domain :as dom]
            [advent.day01 :as day01]
            [clojure.spec.alpha :as s]))

(s/conform ::dom/module
           42)
(s/conform ::dom/gas
           42)

(deftest gas-test
  (is (= 2
         (day01/gas 12)))
  (is (= 2
         (day01/gas 14)))
  (is (= 654
         (day01/gas 1969)))
  (is (= 33583
         (day01/gas 100756))))
(s/conform ::dom/gas
           (day01/gas 12))

(deftest gas-plus-test
  (is (= 2
         (day01/gas-plus 14)))
  (is (= 966
         (day01/gas-plus 1969)))
  (is (= 50346
         (day01/gas-plus 100756))))
(s/conform ::dom/gas
           (day01/gas-plus 14))
