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