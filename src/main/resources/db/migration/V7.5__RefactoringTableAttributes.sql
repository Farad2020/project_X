ALTER TABLE Management_Staff ADD COLUMN is_able_to_delete_schedule BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_add_schedule BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_edit_manager BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_edit_teacher BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_edit_student BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_edit_course BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD COLUMN is_able_to_edit_schedule BOOLEAN NOT NULL DEFAULT(FALSE);