document.getElementById('change-theme-btn').addEventListener('click', function(){
    if(document.documentElement.getAttribute('data-theme') == 'light' ){
        trans()
        document.documentElement.setAttribute('data-theme', 'dark')
    }else{
        trans()
        document.documentElement.setAttribute('data-theme', 'light')

    }
});

let trans = () => {
    document.documentElement.classList.add('transition');
    window.setTimeout(() => {
        document.documentElement.classList.remove('transition')
    }, 100)
}

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
  }