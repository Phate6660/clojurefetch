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
  (if (str/.contains cargs "-d")
    (def distro (str/join["Distro:    ", (Distro)]))
    (def distro ""))
  (if (str/.contains cargs "-h")
    (def hostname (str/join["Hostname:  ", (Hostname)]))
    (def hostname ""))
  (if (str/.contains cargs "-k")
    (def kernel (str/join["Kernel:    ", (Kernel)]))
    (def kernel ""))
  (if (str/.contains cargs "-u")
    (def user (str/join["User:      ", (System/getenv "USER")]))
    (def user ""))
  (println distro)
  (println hostname)
  (println kernel)
  (println user))
