(ns clojurefetch.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn Distro []
  (let [file (slurp "/etc/os-release")
        file_vector (str/split-lines file)
        line (first file_vector)
        line_vector (str/split line #"=")]
    (str/trim-newline (second line_vector))))

(def trim-and-slurp (comp str/trim-newline slurp))

(defn -main [& args]
  (def cargs (apply str args))
  (when (str/.contains cargs "help")
    (println "d     display distro
h     display hostname
k     display kernel
u     display user

help  display help")
    (System/exit 0))
  (if (str/.contains cargs "d")
    (println (str/join["Distro:    ", (Distro)])))
  (if (str/.contains cargs "h")
    (println (str/join["Hostname:  ", (trim-and-slurp "/etc/hostname")])))
  (if (str/.contains cargs "k")
    (println (str/join["Kernel:    ", (trim-and-slurp "/proc/sys/kernel/osrelease")])))
  (if (str/.contains cargs "u")
    (println (str/join["User:      ", (System/getenv "USER")]))))
