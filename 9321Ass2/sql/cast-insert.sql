INSERT INTO TBL_USERS VALUES (1,'Roidi','ezkatka', 'noemail.com','', 'R', 'P', 'admin');
INSERT INTO TBL_USERS VALUES (2,'test','test', 'test.com','', '', '', 'active');

INSERT INTO TBL_CINEMAS VALUES (1, 'YOPO', 1000);
INSERT INTO TBL_CINEMA_AMENITIES VALUES (1, 'TV', 1);
INSERT INTO TBL_CINEMA_AMENITIES VALUES (2, 'Popcorn Machine', 1);

UPDATE TBL_MOVIE_SHOWTIMES SET BOOKED = 0 WHERE MOVIE_SHOWTIME_ID = 1;

 --Import the database
--Change movies.del to wherever it is locaed on local machine.
  CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE 
   (null,'TBL_MOVIES','C://movies.del',null,null,null,0);