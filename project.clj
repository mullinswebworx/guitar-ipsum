(defproject guitar-ipsum "0.1.0-SNAPSHOT"
  :description "Lorem Ipsum, except with guitars!"
  :url "https://guitar-ipsum.herokuapp.com/"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
  				[compojure "1.4.0"]
  				[ring/ring-jetty-adapter "1.4.0"]
  				[environ "1.0.0"]]
  :main ^:skip-aot guitar-ipsum.core
  :target-path "target/%s"
  :profiles {:production {:env {:production true}}})