SELECT * from TBL_USERS;
SELECT COUNT(*) FROM TBL_CINEMAS;
SELECT * FROM TBL_CINEMAS;
SELECT * FROM TBL_CINEMA_AMENITIES;
SELECT MAX(CINEMA_ID) FROM TBL_CINEMAS;
SELECT * FROM TBL_CINEMAS as a JOIN TBL_CINEMA_AMENITIES as b ON a.CINEMA_ID = b.AMENITY_CINEMA;
SELECT * from TBL_MOVIES;