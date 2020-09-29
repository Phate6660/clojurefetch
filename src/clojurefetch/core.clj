(ns clojurefetch.core
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell])
  (:gen-class))

(defn Distro []
  (let [file (slurp "/etc/os-release")
        file_vector (str/split-lines file)
        line (first file_vector)
        line_vector (str/split line #"=")]
    (str/trim-newline (second line_vector))))

(def trim-and-slurp (comp str/trim-newline slurp))

(def trim-blanks-and-newlines (comp str/trim-newline str/trim))

(defn GPU []
  (let [model (trim-blanks-and-newlines (nth (str/split (:out (shell/sh "sh" "-c" "lspci | grep -I 'VGA\\|Display\\|3D'")) #":") 2))]
    (str model)))

(defn Portage []
  (let [list (shell/sh "qlist" "-I")
        total (count (str/split-lines (str (:out list))))]
    (str total " (portage)")))

(defn read-uptime []
  (let [uptime_raw (str (trim-and-slurp (java.io.FileReader. "/proc/uptime")))
        uptime_vector (str/split uptime_raw #"\.")
        uptime (Integer. (first uptime_vector))]
    uptime))

(defn uptime->string [uptime]
  (let [days (if (> uptime 86400)
               (str (int (/ uptime 60 60 24)) "d")
               "")
        hours (if (> uptime 3600)
                (str (int (rem (/ uptime 60 60) 24)) "h")
                "")
        minutes (if (> uptime 60)
                  (str (int (rem (/ uptime 60) 60)) "m")
                  "")]
  (str/trim (str days " " hours " " minutes))))

(defn display-help []
  (println "general fields:
---------------
D     display device name
d     display distro
e     display editor (requires $EDITOR to be set)
g     display gpu (requires lspci and grep)
h     display hostname
k     display kernel
s     display shell
U     display user
u     display uptime

help  display help

package counts:
---------------
p     portage (requires qlist until I can figure out globbing)"))

(defn -main [& args]
  (let [cargs (apply str args)]
    (if (str/includes? cargs "help")
      (display-help)
      (do
        (when (str/includes? cargs "D")
          (println (str "Device:    " (trim-and-slurp "/sys/devices/virtual/dmi/id/product_name"))))
        (when (str/includes? cargs "d")
          (println (str "Distro:    " (Distro))))
        (when (str/includes? cargs "e")
          (println (str "Editor:    " (System/getenv "EDITOR"))))
        (when (str/includes? cargs "g")
          (println (str "GPU:       " (GPU))))
        (when (str/includes? cargs "h")
          (println (str "Hostname:  " (trim-and-slurp "/etc/hostname"))))
        (when (str/includes? cargs "k")
          (println (str "Kernel:    " (trim-and-slurp "/proc/sys/kernel/osrelease"))))
        (when (str/includes? cargs "p")
          (println (str "Packages   " (Portage))))
        (when (str/includes? cargs "s")
          (println (str "Shell:     " (System/getenv "SHELL"))))
        (when (str/includes? cargs "U")
          (println (str "User:      " (System/getenv "USER"))))
        (when (str/includes? cargs "u")
          (println (str "Uptime:    " (uptime->string (read-uptime)))))))))
