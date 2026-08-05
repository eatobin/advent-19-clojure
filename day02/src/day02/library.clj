(ns day02.library
  (:require
   [clojure.string :as str]))

; [eric@eric-minisforum day02](dev)$ clojure -M:repl/rebel
; nREPL server started on port 45677 on host localhost - nrepl://localhost:45677
; [Rebel readline] Type :repl/help for online help info
; user=> (require '[day02.day02 :as day02])
; nil
; user=> *ns*
; #namespace[user]
; user=> (day02/answer-b)
; 8226

;; Instruction:
;; ABCDE
;; 01234
;; 01002
;; 34(DE) - two-digit opcode,      02 == opcode 2
;;  2(C) - mode of 1st parameter,  0 == position mode
;;  1(B) - mode of 2nd parameter,  1 == immediate mode
;;  0(A) - mode of 3rd parameter,  0 == position mode,
;;                                   omitted due to being a leading zero
;; 0 1 or 2 = left-to-right position after 2 digit opcode
;; p i or r = position, immediate or relative mode
;; r or w = read or write

(def POINTER-OFFSET-C 1)
(def POINTER-OFFSET-B 2)
(def POINTER-OFFSET-A 3)

(defn make-instruction [instruction]
  (zipmap [:a :b :c :d :e]
          (for [character (format "%05d" instruction)]
            (- (byte character) 48))))

(defn make-memory [memory-as-csv-string]
  (->> (str/split memory-as-csv-string #",")
       (map parse-long)
       (apply vector)))

(defn updated-memory [noun verb memory]
  (->
   memory
   (assoc 1 noun)
   (assoc 2 verb)))

(defn key-to-key [{:keys [pointer memory]} pointer-offset]
  (get memory (+ pointer pointer-offset)))

(defn -p-w [{:keys [pointer memory]} pointer-offset]
  (key-to-key {:pointer pointer :memory memory} pointer-offset))

(defn -p-r [{:keys [pointer memory]} pointer-offset]
  (get memory (key-to-key {:pointer pointer :memory memory} pointer-offset)))

(defn a-param [instruction {:keys [pointer memory]}]
  (case (:a instruction)
    0 (-p-w {:pointer pointer :memory memory} POINTER-OFFSET-A) ; a-p-w
    (throw (ex-info "no a-param match"
                    {:error-type :bad-a-param-choice}))))

(defn b-param [instruction {:keys [pointer memory]}]
  (case (:b instruction
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-B) ; b-p-r
    (throw (ex-info "no b-param match"
                    {:error-type :bad-b-param-choice}))))

(defn c-param [instruction {:keys [pointer memory]}]
  (case (:c instruction)
    0 (-p-r {:pointer pointer :memory memory} POINTER-OFFSET-C) ; c-p-r
    (throw (ex-info "no c-param match"
                    {:error-type :bad-c-param-choice}))))

(defn add [instruction {:keys [pointer memory actions]}]
  {:pointer (+ 4 pointer)
   :memory  (assoc
             memory
             (a-param instruction {:pointer pointer :memory memory})
             (+ (c-param instruction {:pointer pointer :memory memory})
                (b-param instruction {:pointer pointer :memory memory})))
   :actions (cons :add actions)})

(defn multiply [instruction {:keys [pointer memory actions]}]
  {:pointer (+ 4 pointer)
   :memory  (assoc
             memory
             (a-param instruction {:pointer pointer :memory memory})
             (* (c-param instruction {:pointer pointer :memory memory})
                (b-param instruction {:pointer pointer :memory memory})))
   :actions (cons :multiply actions)})

(defn exit [{:keys [pointer memory actions]}]
  {:pointer pointer
   :memory  memory
   :actions (cons :exit actions)})

(defn run-op-code [{:keys [pointer memory actions]}]
  (let [instruction (make-instruction (memory pointer))]
    (case (instruction :e)
      1 (recur
         (add instruction {:keys [pointer memory actions]}))
      2 (recur
         (multiply instruction {:keys [pointer memory actions]}))
      9 (exit instruction {:keys [pointer memory actions]})
      (throw (ex-info "run-op-code failed"
                      {:error-type :bad-run-op-code-choice})))))
