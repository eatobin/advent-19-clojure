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

;(deftest fuel-plus-test
;  (is (= 2
;         (day01/gas-plus 14)))
;  (is (= 966
;         (day01/gas-plus 1969)))
;  (is (= 50346
;         (day01/gas-plus 100756))))
