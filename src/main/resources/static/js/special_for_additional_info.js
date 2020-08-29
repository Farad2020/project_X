
function showAdditionalInformation() {

    let additionalInfo = document.getElementById("additional-info");
    let value = additionalInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") != -1) {
        additionalInfo.setAttribute("class", "additional-info");
    }
}

function closeAdditionalInformation() {
    let additionalInfo = document.getElementById("additional-info");
    let value = additionalInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") == -1) {
        additionalInfo.setAttribute("class", "additional-info hidden");
    }
}

function showAdditionalInformationAboutStudents() {
    let additionalInfo = document.getElementById("additional-info-about-students");
    let value = additionalInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") != -1) {
        additionalInfo.setAttribute("class", "additional-info");
    }
}

function closeAdditionalInformationAboutStudents() {
    let additionalInfo = document.getElementById("additional-info-about-students");
    let value = additionalInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") == -1) {
        additionalInfo.setAttribute("class", "additional-info hidden");
    }
}

function showAdditionalEditInformation() {
    let editInfo = document.getElementById("additional-info-edit");
    let value = editInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") != -1) {
        editInfo.setAttribute("class", "additional-info-edit");
    }
}

function closeAdditionalEditInformation() {
    let editInfo = document.getElementById("additional-info-edit");
    let value = editInfo.attributes.getNamedItem("class").value;

    if (value.indexOf("hidden") == -1) {
        editInfo.setAttribute("class", "additional-info-edit hidden");
    }
}