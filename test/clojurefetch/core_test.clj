(ns clojurefetch.core-test
  (:require [clojure.test :refer :all]
            [clojurefetch.core :refer :all]))

(deftest uptime-<min-test
  (testing "Uptimes less than 1 minute"
      (is (= "" (uptime->string 0)))
      (is (= "" (uptime->string 20)))
      (is (= "" (uptime->string 45)))
      (is (= "" (uptime->string 59)))))

(deftest uptime-min-boundary-test
  (testing "Uptimes on the minute boundary"
    (is (= "1m" (uptime->string 60)))
    (is (= "1m" (uptime->string 67)))
    (is (= "1m" (uptime->string 90)))
    (is (= "2m" (uptime->string 120)))
    (is (= "3m" (uptime->string 180)))
    (is (= "14m" (uptime->string 899)))
    (is (= "15m" (uptime->string 900)))))

(deftest uptime-hour-boundary-test
  (testing "uptimes on the hour boundary"
    (is (= "1h" (uptime->string 3600)))))

(deftest uptime-day-boundary-test
  (testing "uptimes on the day boundary"
    (is (= "1d" (uptime->string 86400)))))
