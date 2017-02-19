(ns web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
            [clojure.string :as str]))

(def Gibson '("ES-335", "Les Paul", "Flying V", "SG", "Explorer", "Firebird", "ES-175"))

(def Fender '("Stratocaster", "Telecaster", "Jaguar", "Mustang"))

(def Ibanez '("RG", "RGA", "S", "Mikro", "RGD", "SA", "FR", "Talman", "AR", "ARZ", "Iceman", "Gio"))

(def Schecter '("Damien", "Hellraiser", "Demon", "Blackjack", "Apocalypse", "Omen", "Retro", "Sustainiac", "Standard", "Special Edition", "Sun Valley Super Shredder"))

(def Jackson '("X", "JS", "Soloist", "Dinky", "Rhoads", "Juggernaut", "Baritone", "Monarkh", "Warrior", "Demmelition", "Dominion", "Star"))

(defn randomize [n b]
  (apply str (interpose " " (repeatedly n #(rand-nth b)))))

(defn default []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (randomize 50 (concat Gibson, Fender, Ibanez, Schecter, Jackson))})

(defn guitars [x]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (randomize x (concat Gibson, Fender, Ibanez, Schecter, Jackson))})

(defroutes app
  (GET "/" []
       (default))
  (GET "/guitars=:input" {{input :input} :params}
    (guitars (Integer/parseInt input)))
  ;;(GET "/brand=:input" {{input :input} :params}
    ;;(randomize 50 (var input)))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))

;; For interactive development:
;; (.stop server)
;; (def server (-main))