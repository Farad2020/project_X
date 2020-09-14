/*
var successHandler = function( data, textStatus, jqXHR ) {
  // After success reload table with JavaScript
  // based on data...
};

var errorHandler = function( jqXHR, textStatus, errorThrown ) {
  // Error handler. AJAX request failed.
  // 404 or KO from server...
  alert('Something bad happened while reloading the table.');
};

var reloadData = function() {
  // Process your request
  var request = new Object();
  request.id = 'some id'; // some data

  // Make the AJAX call
  jQuery.ajax({
    type       : 'POST',
    url        : 'http://domain/context/reload-data-url',
    contentType: 'application/json',
    data       : JSON.stringify(request)
    success    : successHandler,
    error      : errorHandler
  });
};

*/

document.getElementById('change-theme-btn').addEventListener('click', this.changeTheme );


function changeTheme(){
    if(document.documentElement.getAttribute('data-theme') == 'light' ){
        trans()
        document.documentElement.setAttribute('data-theme', 'dark')
        localStorage.setItem('theme', 'dark')
    }else{
        trans()
        document.documentElement.setAttribute('data-theme', 'light')
        localStorage.setItem('theme', 'light')
    }
};

if(localStorage.getItem('theme') == 'dark'){
    document.documentElement.setAttribute('data-theme', 'dark')
}

let trans = () => {
    document.documentElement.classList.add('transition');
    window.setTimeout(() => {
        document.documentElement.classList.remove('transition')
    }, 100)
};

function showMoreAccountInfo() {
    var fullInfo = document.getElementById("account-info-card-text");
    var btnShowAll = document.getElementById("show-full-account-desc-btn");
  
    if (fullInfo.style.display != "inline") {
        fullInfo.style.display = "inline";
        btnShowAll.innerHTML = "Скрыть подробную информацию";
    } else {
        fullInfo.style.display = "none";
        btnShowAll.innerHTML = "Показать подробную информацию"; 
    }
};
/*
const options = {
    bottom: '64px', // default: '32px'
    right: '32px', // default: '32px'
    left: 'unset', // default: 'unset'
    time: '0.5s', // default: '0.3s'
    mixColor: '#fff', // default: '#fff'
    backgroundColor: '#fff',  // default: '#fff'
    buttonColorDark: '#100f2c',  // default: '#100f2c'
    buttonColorLight: '#fff', // default: '#fff'
    saveInCookies: true, // default: true,
    label: '🌓', // default: ''
    autoMatchOsTheme: true // default: true
};

const darkmode =  new Darkmode(options);
darkmode.showWidget();
*/

(document).readyState(function(){
  $('[data-toggle="tooltip"]').tooltip();
});

