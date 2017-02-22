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
   :body (randomize 1000 (concat Gibson, Fender, Ibanez, Schecter, Jackson))})

(defn guitar [x]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (randomize x (concat Gibson, Fender, Ibanez, Schecter, Jackson))})

(defroutes app
  (GET "/" [guitars brand]
    ;; if the number of guitars is specified, check brand
    (if (not= guitars nil)
      (case brand
        "Gibson" (randomize (Integer/parseInt guitars) Gibson)
        "Fender" (randomize (Integer/parseInt guitars) Fender)
        "Ibanez" (randomize (Integer/parseInt guitars) Ibanez)
        "Schecter" (randomize (Integer/parseInt guitars) Schecter)
        "Jackson" (randomize (Integer/parseInt guitars) Jackson)
        nil (guitar (Integer/parseInt guitars))) ;; case where brand isn't specified
      ;; ELSE there is no number of guitars specified, go simply by default number and brand:
      (case brand
        "Gibson" (randomize 1000 Gibson)
        "Fender" (randomize 1000 Fender)
        "Ibanez" (randomize 1000 Ibanez)
        "Schecter" (randomize 1000 Schecter)
        "Jackson" (randomize 1000 Jackson))))

  (GET "/" [] ;; DEFAULT: no input or parameters given
    (default))

  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))