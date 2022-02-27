(ns advent.day01b-test
  (:require [clojure.test :refer [deftest is]]
            [advent.day01b :as day01b]))

(deftest fuel-plus-test
  (is (= 2
         (day01b/fuel-plus 14)))
  (is (= 966
         (day01b/fuel-plus 1969)))
  (is (= 50346
         (day01b/fuel-plus 100756))))
