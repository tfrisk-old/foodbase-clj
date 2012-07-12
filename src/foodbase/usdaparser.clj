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
(defn insert-db-entry [entry]
  (insert FOOD_DES (values entry)))

; ----------------------------------------------

;sqlite3 foodbase.db
"create table FOOD_DES (
NDB_No INTEGER PRIMARY KEY,
FdGrp_Cd INTEGER,
Long_Desc TEXT,
Short_Desc TEXT,
ComName TEXT,
ManufacName TEXT,
Survey TEXT,
Ref_desc TEXT,
Refuse FLOAT,
SciName TEXT,
N_Factor FLOAT,
Pro_Factor FLOAT,
Fat_Factor FLOAT,
CHO_Factor FLOAT
);"

; column names for FOOD_DES file  
(def FOOD_DES-dbcolumns '(
  "NDB_No","FdGrp_Cd","Long_Desc","Short_Desc","ComName","ManufacName","Survey",
  "Ref_desc","Refuse","SciName","N_Factor","Pro_Factor","Fat_Factor","CHO_Factor"))

;"~11790~^~1100~^~Kale, cooked, boiled, drained, with salt~^~KALE,CKD,BLD,DRND,W/SALT~^~~^~~^~~^~~^0^~~^6.25^2.44^8.37^3.57"
;put values to list
(defn parse-list [line]
  (str/split line #"\^"))

;trim ~ characters
(defn trim-tildes [line]
  (str/replace line #"~" ""))

; map keys to values
(defn map-raw-lines [line]
  (zipmap FOOD_DES-dbcolumns
    (parse-list
      (trim-tildes line))))

(defn -main [& args]
(with-open [rdr (jio/reader "./src/foodbase/FOOD_DES.txt")]
  (doseq [line (line-seq rdr)]
    ;(clojure.pprint/pprint (parse-list (trim-tildes line)))
    ;(clojure.pprint/pprint (map-raw-lines line))
    ;(print (map-raw-lines line) "\n")
    (insert-db-entry (map-raw-lines line))
    ))
)
;(with-open [rdr(jio/reader "./src/foodbase/FOOD_DES.txt")]
;  (->> rdr
;    (line-seq)
;    (map parse-list)
;    ))
