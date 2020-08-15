ALTER TABLE Schedule DROP COLUMN student_course_id;
ALTER TABLE Schedule ADD course_id UUID REFERENCES Courses(course_id);