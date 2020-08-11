CREATE TABLE Links (
    link_id UUID NOT NULL PRIMARY KEY,
    link_name VARCHAR(100) NOT NULL,
    link_url VARCHAR(300) NOT NULL,
    company_id UUID REFERENCES Companies(company_id)
);

CREATE TABLE User_Teachers (
    user_teacher_id UUID NOT NULL PRIMARY KEY,
    user_teacher_name VARCHAR(100) NOT NULL,
    user_teacher_surname VARCHAR(100),
    user_teacher_lastname VARCHAR(100),
    user_teacher_login VARCHAR(100) NOT NULL,
    user_teacher_password VARCHAR(200) NOT NULL,
    user_teacher_email VARCHAR(100),
    user_teacher_telephone VARCHAR(100),
    is_account_non_expired BOOLEAN NOT NULL DEFAULT(TRUE),
    is_account_non_locked BOOLEAN NOT NULL DEFAULT(TRUE),
    is_credentials_non_expired BOOLEAN NOT NULL DEFAULT(TRUE),
    is_enabled BOOLEAN NOT NULL DEFAULT(TRUE),
    company_id UUID REFERENCES Companies(company_id) NOT NULL
);

CREATE TABLE Posts(
    post_id UUID NOT NULL PRIMARY KEY,
    post_name VARCHAR(300) NOT NULL,
    post_content TEXT NOT NULL,
    post_datetime TIMESTAMP NOT NULL,
    post_author_role SMALLINT NOT NULL,
    post_author_id UUID NOT NULL
);

CREATE TABLE Post_Images (
    post_image_id UUID NOT NULL PRIMARY KEY,
    post_image BYTEA NOT NULL,
    post_id UUID REFERENCES Posts(post_id) NOT NULL
);

CREATE TABLE Courses(
    course_id UUID NOT NULL PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    course_description TEXT NOT NULL,
    course_is_active BOOLEAN NOT NULL,
    course_start_date DATE NOT NULL,
    course_end_date DATE,
    course_price REAL NOT NULL,
    course_payout_num SMALLINT NOT NULL,
    user_teacher_id UUID REFERENCES User_Teachers(user_teacher_id) NOT NULL
);

CREATE TABLE Students_Courses(
    student_course_id UUID NOT NULL PRIMARY KEY,
    student_id UUID REFERENCES User_Students(user_student_id) NOT NULL,
    course_id UUID REFERENCES Courses(course_id) NOT NULL,
    student_course_review_score REAL
);

CREATE TABLE Schedule(
    schedule_id UUID NOT NULL PRIMARY KEY,
    student_course_id UUID REFERENCES Students_Courses(student_course_id) NOT NULL,
    schedule_time_start TIME NOT NULL,
    schedule_time_end TIME NOT NULL,
    schedule_week_day VARCHAR(30) NOT NULL
);

CREATE TABLE Attendance(
    attendance_id UUID NOT NULL PRIMARY KEY,
    student_course_id UUID REFERENCES Students_Courses(student_course_id) NOT NULL,
    attendance_date DATE NOT NULL,
    attendance_type SMALLINT
);

CREATE TABLE Task(
    task_id UUID NOT NULL PRIMARY KEY,
    course_id UUID REFERENCES Courses(course_id) NOT NULL,
    task_start_time TIMESTAMP NOT NULL,
    task_end_time TIMESTAMP NOT NULL,
    task_description TEXT NOT NULL
);

CREATE TABLE Reply(
    reply_id UUID NOT NULL PRIMARY KEY,
    task_id UUID REFERENCES Task(task_id) NOT NULL,
    user_student_id UUID REFERENCES User_Students(user_student_id) NOT NULL,
    reply_text TEXT NOT NULL,
    reply_time TIMESTAMP NOT NULL
);