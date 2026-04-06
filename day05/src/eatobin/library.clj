(ns eatobin.library)

(defn char-to-int [char-as-byte]
  (- char-as-byte 48))

(defn make-instruction [integer]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" integer)]
            (char-to-int (byte character)))))
(comment
  (make-instruction 6))
