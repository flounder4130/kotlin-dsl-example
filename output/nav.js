window.addEventListener('DOMContentLoaded', function() {
    let summaries = document.querySelectorAll('.method-summary');
    summaries.forEach(summary => {
        let link = document.createElement('a');
        link.href = '#' + summary.id;
        link.innerText = summary.innerText;
        document.querySelector('nav').appendChild(link);
    })
    addListeners()
});
