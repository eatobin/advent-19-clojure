(ns advent.day4)

(def pass (range 234208 765870))

(defn increasing? [candidate]
  (apply <= (vec (for [n (str candidate)]
                   (- (byte n) 48)))))

(defn doubles? [candidate]
  (loop [v (for [n (str candidate)]
             (- (byte n) 48))]
    (if (empty? v)
      false
      (if (= (first v) (second v))
        true
        (recur (rest v))))))

(def answer (count (for [c pass
                         :when (and (increasing? c) (doubles? c))]
                     c)))
;=> 1246
