(ns clojurefetch.core-test
  (:require [clojure.test :refer :all]
            [clojurefetch.core :refer :all]))

(deftest uptime-test
  (testing "Testing uptime->string:"
    (testing "uptimes less than 1 minute"
      (is (= "" (uptime->string 0)))
      (is (= "" (uptime->string 20)))
      (is (= "" (uptime->string 45)))
      (is (= "" (uptime->string 59))))
    (testing "uptimes on the minute boundary"
      (is (= "1m" (uptime->string 60)))
      (is (= "2m" (uptime->string 120))))))
