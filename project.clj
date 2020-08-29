(defproject clojurefetch "0.1.0-SNAPSHOT"
  :description "I have a problem, ok. I know I do. This is yet another *fetch program, this time in clojure."
  :url "https://github.com/Phate6660/clojurefetch"
  :license {:name "Unlicense"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot clojurefetch.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
