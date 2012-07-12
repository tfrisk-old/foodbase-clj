(ns foodbase.usdaparser
  (:require [clojure.java.io :as jio])
  (:require [clojure.string :as str])
  (use korma.db)
  (use korma.core))

; KORMA ----------------------------------------------
(defdb devel (sqlite3 {:db "foodbase.db"
                      :user "foodbase"
                      :password "foodpass"}))

(defentity FOOD_DES
  (pk :NDB_No)
  (table :FOOD_DES)
  (database devel))

;form sql command
(defn insert-db-entry [entry table]
  (insert table (values entry)))

; ------------------------------------------------------
;database test commands

;sqlite3 foodbase.db "select * from FOOD_DES where NDB_No = 1222"

; ------------------------------------------------------
; database column names  
(def FOOD_DES-dbcolumns '(
  "NDB_No","FdGrp_Cd","Long_Desc","Short_Desc","ComName","ManufacName","Survey",
  "Ref_desc","Refuse","SciName","N_Factor","Pro_Factor","Fat_Factor","CHO_Factor"))

(def FD_GROUP-dbcolumns '(
  "FdGrp_Cd","FdGrp_Desc")) ;FdGrp_Cd linked to FOOD_DES FdGrp_Cd
; ------------------------------------------------------

;put values to list
;"11790^1100^Kale, cooked, boiled, drained, with salt^KALE,CKD,BLD,DRND,W/SALT^^^^^0^^6.25^2.44^8.37^3.57"
(defn split-line [line]
  (str/split line #"\^"))

;trim ~ characters
;"~11790~^~1100~^~Kale, cooked, boiled, drained, with salt~^~KALE,CKD,BLD,DRND,W/SALT~^~~^~~^~~^~~^0^~~^6.25^2.44^8.37^3.57"
(defn trim-tildes [line]
  (str/replace line #"~" ""))

; map keys to values
(defn map-raw-lines [line]
  ;(zipmap (format "%s-dbcolumns" "FOOD_DES")
  (zipmap FOOD_DES-dbcolumns
    (split-line
      (trim-tildes line))))

(defn -main [& args]
(with-open [rdr (jio/reader "./src/foodbase/FOOD_DES.txt")]
  (doseq [line (line-seq rdr)]
    ;(clojure.pprint/pprint (parse-list (trim-tildes line)))
    ;(clojure.pprint/pprint (map-raw-lines line))
    ;(print (map-raw-lines line) "\n")
    (insert-db-entry (map-raw-lines line) FOOD_DES)
    )))
