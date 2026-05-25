(ns eatobin.testing)

(def op-a {:input         0
           :output        0
           :phase         0
           :pointer       0
           :relative-base 0
           :memory        0
           :stopped?      false
           :recur?        true})
(def op-b {:input         0
           :output        0
           :phase         0
           :pointer       0
           :relative-base 0
           :memory        0
           :stopped?      false
           :recur?        true})
(def op-c {:input         0
           :output        0
           :phase         0
           :pointer       0
           :relative-base 0
           :memory        0
           :stopped?      false
           :recur?        true})
(def op-d {:input         0
           :output        0
           :phase         0
           :pointer       0
           :relative-base 0
           :memory        0
           :stopped?      false
           :recur?        true})
(def op-e {:input         0
           :output        60
           :phase         0
           :pointer       0
           :relative-base 0
           :memory        0
           :stopped?      false
           :recur?        true})
(def fiver {1 op-a 2 op-b 3 op-c 4 op-d 5 op-e})

(defn grab-my-input-from-prior-output [index target-map]
  (if (= index 1)
    (assoc-in target-map [index :input] (get-in target-map [5 :output]))
    (assoc-in target-map [index :input] (get-in target-map [(dec index) :output]))))

(defn increment-my-output-from-my-input [index target-map]
  (assoc-in target-map [index :output] (inc (get-in target-map [index :input]))))

(defn grab-and-increment [index target-map]
  (->>
   target-map
   (grab-my-input-from-prior-output index)
   (increment-my-output-from-my-input index)))

(defn map-of-five [fiver]
  (loop [index 1
         fiver fiver]
    (if (= (get-in fiver [5 :output]) 220)
      fiver
      (recur
       (inc (mod index 5)) (grab-and-increment index fiver)))))

(comment
  (map-of-five fiver)
  :rcf)

(defn pass [i-code-memory [a b c d e]]
  (let [ic {1 {:input         0
               :output        []
               :phase         a
               :pointer       0
               :relative-base 0
               :memory        i-code-memory
               :stopped?      false
               :recur?        true}
            2 {:input         0
               :output        0
               :phase         0
               :pointer       0
               :relative-base 0
               :memory        0
               :stopped?      false
               :recur?        true}
            3 {:input         0
               :output        0
               :phase         0
               :pointer       0
               :relative-base 0
               :memory        0
               :stopped?      false
               :recur?        true}
            4 {:input         0
               :output        0
               :phase         0
               :pointer       0
               :relative-base 0
               :memory        0
               :stopped?      false
               :recur?        true}
            5 {:input         0
               :output        0
               :phase         0
               :pointer       0
               :relative-base 0
               :memory        0
               :stopped?      false
               :recur?        true}}]))
