ALTER TABLE User_Students DROP COLUMN profile_image_id;
ALTER TABLE User_Teachers DROP COLUMN profile_image_id;
ALTER TABLE Management_Staff DROP COLUMN profile_image_id;

DROP TABLE Profile_Images;

ALTER TABLE User_Students ADD COLUMN user_student_profile_image OID;
ALTER TABLE User_Teachers ADD COLUMN user_teacher_profile_image OID;
ALTER TABLE Management_Staff ADD COLUMN management_staff_profile_image OID;