(ns clojurefetch.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn Kernel []
  (str/trim-newline (slurp "/proc/sys/kernel/osrelease")))

(defn -main []
  (def kernel (str/join["Kernel: ", (Kernel)]))
  (println kernel))
