(ns advent.day4)

(def pass (range 234208 765870))

(defn explode [num]
  (for [n (str num)]
    (- (byte n) 48)))

(defn increasing? [candidate]
  (apply <= (vec (explode candidate))))

(defn doubles? [candidate]
  (loop [v (explode candidate)]
    (if (empty? v)
      false
      (if (= (first v) (second v))
        true
        (recur (rest v))))))

(def answer (count (for [c pass
                         :when (and (increasing? c) (doubles? c))]
                     c)))
;=> 1246

(defn double-present? [candidate]
  (some #(= 2 %)
        (map count (partition-by identity (explode candidate)))))

(def answer-2 (count (for [c pass
                         :when (and (increasing? c) (double-present? c))]
                     c)))

;814
