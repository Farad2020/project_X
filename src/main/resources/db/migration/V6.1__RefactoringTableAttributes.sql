ALTER TABLE Companies RENAME name TO company_name;
ALTER TABLE Companies RENAME email to company_email;
ALTER TABLE Companies RENAME telephone to company_telephone;

ALTER TABLE User_Students RENAME id TO user_student_id;
ALTER TABLE User_Students RENAME name TO user_student_name;
ALTER TABLE User_Students RENAME surname TO user_student_surname;
ALTER TABLE User_Students RENAME lastname TO user_student_lastname;
ALTER TABLE User_Students RENAME login TO user_student_login;
ALTER TABLE User_Students RENAME password TO user_student_password;
ALTER TABLE User_Students RENAME email TO user_student_email;
ALTER TABLE User_Students RENAME telephone TO user_student_telephone;

ALTER TABLE Management_Staff RENAME id TO management_staff_id;
ALTER TABLE Management_Staff RENAME name TO management_staff_name;
ALTER TABLE Management_Staff RENAME surname TO management_staff_surname;
ALTER TABLE Management_Staff RENAME lastname TO management_staff_lastname;
ALTER TABLE Management_Staff RENAME login TO management_staff_login;
ALTER TABLE Management_Staff RENAME password TO management_staff_password;
ALTER TABLE Management_Staff RENAME email TO management_staff_email;
ALTER TABLE Management_Staff RENAME telephone TO management_staff_telephone;

ALTER TABLE Admins RENAME id TO admin_id;
ALTER TABLE Admins RENAME login TO admin_login;
ALTER TABLE Admins RENAME password TO admin_password;