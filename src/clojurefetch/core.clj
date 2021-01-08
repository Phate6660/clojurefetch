(ns clojurefetch.core
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell])
  (:gen-class))

(defn Distro []
  (->> "/etc/os-release"
       slurp
       (re-find #"PRETTY_NAME=\"([\w ]+)\"")
       ;; Get capture group
       last))

(def trim-and-slurp (comp str/trim-newline slurp))

(defn get-env [s]
  (let [env (str/upper-case s)]
    (or (System/getenv env)
        (format "$%s is not set!" env))))

(defn GPU []
  (->>
   (shell/sh "sh" "-c" "lspci | grep -I 'VGA\\|Display\\|3D'")
   :out
   (re-find #": ([\w ]+)")
   last))

(defn Portage []
  (let [list (shell/sh "qlist" "-I")
        total (count (str/split-lines (str (:out list))))]
    (str total " (portage)")))

(defn read-uptime []
  ;; /proc/uptime contains two fields separated by a space:
  ;;   1. The system's uptime represented in seconds
  ;;   2. The sum of each core's idle time
  ;; Example: 1994.80 3679.13
  ;; To calculate the uptime we only want the first value without the fractional part.
  (->> "/proc/uptime"
       java.io.FileReader.
       trim-and-slurp
       (re-find #"^\d+")
       Integer/parseInt))

(defn uptime->string [uptime]
  (if (< uptime 60)
    "<1m" ;; Return "<1m" if uptime is under a minute.
    (let [days (if (>= uptime 86400)
                 (str (int (/ uptime 60 60 24)) "d ")
                 "")
          hours (if (>= uptime 3600)
                  (str (int (rem (/ uptime 60 60) 24)) "h ")
                  "")
          minutes (if (>= uptime 60)
                    (str (int (rem (/ uptime 60) 60)) "m")
                    "")]
      (str days hours minutes))))

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
          (println (str "Editor:    " (get-env "EDITOR"))))
        (when (str/includes? cargs "g")
          (println (str "GPU:       " (GPU)))
          ;; Program will hang without shutdown-agents due to "future" used by the shell call.
          (shutdown-agents))
        (when (str/includes? cargs "h")
          (println (str "Hostname:  " (trim-and-slurp "/etc/hostname"))))
        (when (str/includes? cargs "k")
          (println (str "Kernel:    " (trim-and-slurp "/proc/sys/kernel/osrelease"))))
        (when (str/includes? cargs "p")
          (println (str "Packages   " (Portage))))
        (when (str/includes? cargs "s")
          (println (str "Shell:     " (get-env "SHELL"))))
        (when (str/includes? cargs "U")
          (println (str "User:      " (get-env "USER"))))
        (when (str/includes? cargs "u")
          (println (str "Uptime:    " (uptime->string (read-uptime)))))))))
