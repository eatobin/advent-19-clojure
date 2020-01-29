(ns advent.day4)

(def pass (range 234208 765870))

(defn explode [num]
  (for [n (str num)]
    (- (byte n) 48)))

(defn increasing? [candidate]
  (apply <= (vec (explode candidate))))

(defn doubles-or-more [candidate]
  (some #(<= 2 %)
        (map count (partition-by identity (explode candidate)))))

(def answer (count (for [c pass
                         :when (and (increasing? c) (doubles-or-more c))]
                     c)))

(println answer)

;=> 1246

(defn doubles? [candidate]
  (some #(= 2 %)
        (map count (partition-by identity (explode candidate)))))

(def answer-2 (count (for [c pass
                           :when (and (increasing? c) (doubles? c))]
                       c)))

(println answer-2)

;814
