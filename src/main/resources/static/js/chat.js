var target = document.querySelectorAll('.btn-open');
var btnPopClose = document.querySelectorAll('.pop_wrap .btn_close');
var targetID;

// 팝업 열기
for (var i = 0; i < target.length; i++) {
    target[i].addEventListener('click', function () {
        targetID = this.getAttribute('href');
        document.querySelector(targetID).style.display = 'block';
    });
}

// 팝업 닫기
for (var j = 0; j < target.length; j++) {
    btnPopClose[j].addEventListener('click', function () {
        this.parentNode.parentNode.style.display = 'none';
    });
}

const button = document.getElementById('button_copy');
button.addEventListener('click', copyToClipboard);

function copyToClipboard(val) {
    const str = "https://" + location.host + "/chat/chatting?id=";
    navigator.clipboard.writeText(str);
    alert("URL이 클립보드에 복사되었습니다");
}

