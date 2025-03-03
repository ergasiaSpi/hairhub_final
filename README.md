![ssss](https://github.com/user-attachments/assets/653b3fee-f886-45e4-9fbf-7a5b16856184)


ΟΔΗΓΙΕΣ : 

 

Το πρόγραμμα θα ζητήσει από τον χρήστη να επιλέξει μία από τις εξής επιλογές: 

Σύνδεση (Sign In) 

Εγγραφή (Sign Up) 

Έξοδος (Exit) 

Πληκτρολογήστε τον αριθμό της επιλογής και πατήστε Enter. 

Για να δημιουργήσετε έναν λογαριασμό, επιλέξτε την επιλογή 2. Sign Up.  

Θα πρέπει να εισάγετε τα απαιτούμενα στοιχεία  

Μετά την ολοκλήρωση της εγγραφής, το πρόγραμμα θα σας ενημερώσει ότι η εγγραφή ήταν επιτυχής και θα σας ζητήσει να συνδεθείτε. 

 

 

Για Admin (Διαχειριστής): Εάν συνδεθείτε ως διαχειριστής, το πρόγραμμα θα εμφανίσει το μενού διαχείρισης. 

Για να επιλέξετε μια επιλογή, πληκτρολογήστε τον αντίστοιχο αριθμό και πατήστε Enter. 

Για Customer (Πελάτης): Εάν συνδεθείτε ως πελάτης, το πρόγραμμα θα σας εμφανίσει το μενού για την κράτηση ραντεβού ή την προβολή του τελευταίου σας ραντεβού. 

Αν επιλέξετε να κλείσετε ραντεβού, το πρόγραμμα θα σας καθοδηγήσει βήμα-βήμα. 

Και για τους δύο ρόλους (admin και customer), μπορείτε να επιλέξετε την επιλογή Αποσύνδεση (Sign out) από το μενού. Το πρόγραμμα θα σας αποσυνδέσει και θα σας επιστρέψει στην αρχική οθόνη όπου μπορείτε να επιλέξετε ξανά να συνδεθείτε ή να εγγραφείτε.   

 

ΜΕΤΑΓΛΩΤΗΣΗ: MAVEN  

Οδηγίες Εκτέλεσης του Προγράμματος HairhubApp μέσω GitHub: 

Απαιτήσεις Συστήματος 

Εγκατεστημένο Java Runtime Environment (JRE) (έκδοση 17 ή νεότερη). 
Μπορείτε να ελέγξετε την έκδοση της Java με την εντολή: 

bash 

CopyEdit 

java -version 

Λήψη του Κώδικα από το GitHub 

Μεταβείτε στο αποθετήριο GitHub που παρέχεται από την ομάδα. 

Πατήστε το κουμπί Code και επιλέξτε Download ZIP για να κατεβάσετε όλα τα αρχεία. 

Εξαγάγετε το αρχείο ZIP στον φάκελο της επιλογής σας. 

Σύνδεση με τη Βάση Δεδομένων 

Το αρχείο SQLite hairhub.db περιλαμβάνεται στο αποθετήριο. 

Βεβαιωθείτε ότι βρίσκεται στον ίδιο φάκελο με τα αρχεία κώδικα ή ενημερώστε τη μεταβλητή DB_URL στο αρχείο HairhubApp.java. 

Μεταγλώττιση και Εκτέλεση 

Ανοίξτε ένα τερματικό (Command Prompt ή Terminal). 

Μεταβείτε στον φάκελο όπου εξαγάγατε τον κώδικα. 

Εκτελέστε τις παρακάτω εντολές για μεταγλώττιση και εκτέλεση: 

bash 

CopyEdit 

javac -cp ".:sqlite-jdbc.jar" com/hairhub/HairhubApp.java 

java -cp ".:sqlite-jdbc.jar" com.hairhub.HairhubApp 

Αν χρησιμοποιείτε Windows, αντικαταστήστε το : με ; στην παραπάνω εντολή. 

Εκτέλεση με JAR (Αν παρεχθεί προ-μεταγλωττισμένο JAR) 

Εναλλακτικά, αν παρέχεται το αρχείο HairhubApp.jar, εκτελέστε: 

bash 

CopyEdit 

java -jar HairhubApp.jar 

Υποστήριξη 
Για τυχόν προβλήματα, μπορείτε να ανοίξετε ένα Issue στο αποθετήριο του GitHub 

 

Παρουσίαση της δομής των περιεχομένων του αποθετηρίου:  

 

/hairhub_final 
│ 
├── /src                         # Πηγαίος κώδικας της εφαρμογής 
│   ├── /com/hairhub             # Πακέτο για τη λογική της εφαρμογής 
│   │   ├── Admin                # Κλάσεις για διαχείριση του admin 
│   │   ├── BookAnAppointment    # Κλάσεις για την κράτηση ραντεβού 
│   │   ├── sign_in_up           # Κλάσεις για την αυθεντικοποίηση χρηστών 
│   │   ├── UserSessionManager   # Διαχείριση συνεδριών χρηστών 
│   │   └── Main.java            # Κλάση εκκίνησης της εφαρμογής 
│ 
├── /resources                   # Πόροι όπως αρχεία διαμόρφωσης 
│   └── hairhub.db               # Βάση δεδομένων (π.χ. SQLite) 
│ 
├── /lib                         # Εξωτερικές βιβλιοθήκες (π.χ. αρχεία JAR) 
│ 
├── /docs                        # Αρχεία τεκμηρίωσης (π.χ. README.md) 
│ 
├── pom.xml                      # Αρχείο διαμόρφωσης Maven (αν υπάρχει) 
└── README.md                    # Περιγραφή του έργου και οδηγίες εκτέλεσης 

 

 

Επισκόπηση των δομών δεδομένων και των αλγορίθμων : 

 

Πίνακες (Tables) της βάσης δεδομένων SQLite (hairhub.db) 

 

Αλγόριθμοι εύρεσης δεδομένων: 

Χρήση SQL ερωτημάτων (π.χ., SELECT με WHERE για φιλτράρισμα). 

Αλγόριθμοι βελτιστοποίησης: 

Εφαρμογή εκπτώσεων αν ένας stylist έχει λιγότερα από 2 ραντεβού την ημέρα. 

Επεξεργασία ημερομηνιών και ωρών: 

Χρήση της Java API (LocalDate, LocalTime) για τον έλεγχο διαθεσιμότητας. 

Αποτροπή επικαλυπτόμενων ραντεβού: 

Έλεγχος χρονικών διαστημάτων για να διασφαλιστεί ότι δεν υπάρχουν επικαλύψεις στις ώρες.


MIT License

Copyright (c) 2025 Giannis Kyriakidis

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
