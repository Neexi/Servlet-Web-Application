--Export the movies table
CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE 
   (null,'TBL_MOVIES','c:\movies.del',null,null,null);
   
