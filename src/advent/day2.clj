(ns advent.day2)

(defn mods [m]
  (loop [offset 0
         m m
         ops (m (+ 0 offset))]
    (if (= ops 99)
      m
      (recur
       (+ offset 4)
       m))))
