(ns day01.day01)

(defn exec
  "Invoke me with clojure -X day01.day01/exec"
  [opts]
  (println "exec with" opts))

(defn -main
  "Invoke me with clojure -M -m day01.day01"
  [& args]
  (println "-main with" args))
