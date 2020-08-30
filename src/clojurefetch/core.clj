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

(defn display-help []
  (println "d     display distro
h     display hostname
k     display kernel
u     display user

help  display help"))

(defn -main [& args]
  (let [cargs (apply str args)]
    (if (str/includes? cargs "help")
      (display-help)
      (do 
        (when (str/includes? cargs "d")
          (println (str "Distro:    " (Distro))))
        (when (str/includes? cargs "h")
          (println (str "Hostname:  " (trim-and-slurp "/etc/hostname"))))
        (when (str/includes? cargs "k")
          (println (str "Kernel:    " (trim-and-slurp "/proc/sys/kernel/osrelease"))))
        (when (str/includes? cargs "u")
          (println (str "User:      " (System/getenv "USER"))))))))
