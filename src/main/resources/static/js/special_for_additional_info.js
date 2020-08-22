
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