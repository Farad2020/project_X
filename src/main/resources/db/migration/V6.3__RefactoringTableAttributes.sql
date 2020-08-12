ALTER TABLE Management_Staff ADD is_able_to_delete_manager BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_delete_teacher BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_delete_student BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_add_manager BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_add_teacher BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_add_student BOOLEAN NOT NULL DEFAULT(FALSE);