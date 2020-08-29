(ns clojurefetch.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn Distro []
  (def file (slurp "/etc/os-release"))
  (def file_vector (str/split file #"\n"))
  (def line (nth file_vector 0))
  (def line_vector (str/split line #"="))
  (str/trim-newline (nth line_vector 1)))

(defn Hostname []
  (str/trim-newline (slurp "/etc/hostname")))

(defn Kernel []
  (str/trim-newline (slurp "/proc/sys/kernel/osrelease")))

(defn -main [& args]
  (def cargs (apply str args))
  (if (str/.contains cargs "help")
    ((println "d     display distro
h     display hostname
k     display kernel
u     display user

help  display help")
    (System/exit 0)))
  (if (str/.contains cargs "d")
    (println (str/join["Distro:    ", (Distro)])))
  (if (str/.contains cargs "h")
    (println (str/join["Hostname:  ", (Hostname)])))
  (if (str/.contains cargs "k")
    (println (str/join["Kernel:    ", (Kernel)])))
  (if (str/.contains cargs "u")
    (println (str/join["User:      ", (System/getenv "USER")]))))
