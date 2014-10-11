 --Import the database
--Change result del to wherever it is locaed on local machine.
  CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE 
   (null,'TBL_MOVIES','result.del',null,null,null,0);