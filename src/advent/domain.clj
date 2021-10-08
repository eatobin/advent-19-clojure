(ns advent.domain
  (:require [clojure.spec.alpha :as s]))

(s/def ::module int?)
(s/def ::fuel int?)
