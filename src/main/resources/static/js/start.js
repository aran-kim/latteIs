const mbti = document.querySelector("#mbti");
const qna = document.querySelector("#qna");
const result = document.querySelector("#result");

const endPoint = 12;
const select = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

function setResult(){
  const resultName = document.querySelector('.resultname');

  const num = [0,0,0,0];
  num[0] = Number(select[0]) + Number(select[1]) + Number(select[2]);
  num[1] = Number(select[3]) + Number(select[4]) + Number(select[5]);
  num[2] = Number(select[6]) + Number(select[7]) + Number(select[8]);
  num[3] = Number(select[9]) + Number(select[10]) + Number(select[11]);

  var str = ['I_E', 'S_N', 'T_F', 'J_P'];
  var mbti;
  for(let i = 0; i < 4; i++){
    if(num[i] > 1)
      str[i] = str[i].substring(str[i].lastIndexOf("_") + 1);
    else
      str[i] = str[i].substring(0,1);
  }
  mbti = str[0]+str[1]+str[2]+str[3];

  resultName.innerHTML = mbti;
  $.ajax({
    url : "/mbtiProc",
    type : "POST",
    cache : false,
    data : { mbti : mbti},
    success : function(data){
        alter("질문에 답해주세요!");
        window.location.href = "/question";
    }
  })
}

function goResult(){
  qna.style.WebkitAnimation = "fadeOut 1s";
  qna.style.animation = "fadeOut 1s";
  setTimeout(() => {
    result.style.WebkitAnimation = "fadeIn 1s";
    result.style.animation = "fadeIn 1s";
    setTimeout(() => {
      qna.style.display = "none";
      result.style.display = "block"
    }, 450)})
    setResult();
}

function addAnswer(answerText, qIdx, idx){
  var a = document.querySelector('.answerBox');
  var answer = document.createElement('button');
  answer.classList.add('answerList');
  answer.classList.add('my-3');
  answer.classList.add('py-3');
  answer.classList.add('mx-auto');
  answer.classList.add('fadeIn');

  a.appendChild(answer);
  answer.innerHTML = answerText;

  answer.addEventListener("click", function(){
    var children = document.querySelectorAll('.answerList');
    for(let i = 0; i < children.length; i++){
      children[i].disabled = true;
      children[i].style.WebkitAnimation = "fadeOut 0.5s";
      children[i].style.animation = "fadeOut 0.5s";
    }
    setTimeout(() => {
      select[qIdx] = idx;

      for(let i = 0; i < children.length; i++){
        children[i].style.display = 'none';
      }
      goNext(++qIdx);
    },450)
  }, false);
}

function goNext(qIdx){
  if(qIdx === endPoint){
    goResult();
    return;
  }

  var q = document.querySelector('.qBox');
  q.innerHTML = qnaList[qIdx].q;
  for(let i in qnaList[qIdx].a){
    addAnswer(qnaList[qIdx].a[i].answer, qIdx, i);
  }
  var status = document.querySelector('.statusBar');
  status.style.width = (100/endPoint) * (qIdx+1) + '%';
}

function begin(){
  mbti.style.WebkitAnimation = "fadeOut 1s";
  mbti.style.animation = "fadeOut 1s";
  setTimeout(() => {
    qna.style.WebkitAnimation = "fadeIn 1s";
    qna.style.animation = "fadeIn 1s";
    setTimeout(() => {
      mbti.style.display = "none";
      qna.style.display = "block"
    }, 450)
    let qIdx = 0;
    goNext(qIdx);
  }, 450);
}
