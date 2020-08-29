CREATE TABLE Profile_Images (
    profile_image_id UUID NOT NULL PRIMARY KEY,
    profile_image_oid OID NOT NULL
);

ALTER TABLE User_Students ADD COLUMN profile_image_id UUID REFERENCES Profile_Images(profile_image_id);
ALTER TABLE User_Teachers ADD COLUMN profile_image_id UUID REFERENCES Profile_Images(profile_image_id);
ALTER TABLE Management_Staff ADD COLUMN profile_image_id UUID REFERENCES Profile_Images(profile_image_id);