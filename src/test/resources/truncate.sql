SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE answer RESTART IDENTITY;
TRUNCATE TABLE question RESTART IDENTITY;
TRUNCATE TABLE user RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;