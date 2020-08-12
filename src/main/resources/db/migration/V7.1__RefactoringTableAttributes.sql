ALTER TABLE Management_Staff ADD is_able_to_delete_course BOOLEAN NOT NULL DEFAULT(FALSE);
ALTER TABLE Management_Staff ADD is_able_to_add_course BOOLEAN NOT NULL DEFAULT(FALSE);